/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Caldiddy's PC
 */
import database.Database;
import java.io.InvalidObjectException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import journal.test.pkg1.JournalTest1;
import model.Journal;

public class ProfileController implements Initializable {
    @FXML private TableView<Journal> journalTable;
    @FXML private TableColumn<Journal, String> nameClm;
    @FXML private TableColumn<Journal, String> dateClm;
    @FXML private CheckBox showAllChbx;
    private ContextMenu rowMenu = new ContextMenu();

    @FXML protected void processLogout() {
        JournalTest1.getInstance().userLogout();
    }
    
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showAllChbx.setOnAction(e -> {
            try {
                handleshowAllAction(e);
            } catch (SQLException ex) {
                Logger.getLogger(JournalController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidObjectException ex) {
                Logger.getLogger(JournalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        journalTable.setItems(JournalTest1.getInstance().getLoggedUser().getJournals());
        journalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        nameClm.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateClm.setCellValueFactory(new PropertyValueFactory<>("dateFormatted"));
        journalTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                rowMenu.hide();
            }
        });
        //Double click event
        journalTable.setRowFactory(
                new Callback<TableView<Journal>, TableRow<Journal>>() {
                    @Override
                    public TableRow<Journal> call(TableView<Journal> tableView) {
                        final TableRow<Journal> row = new TableRow<Journal>() { 
                        @Override
                            protected void updateItem(Journal journal, boolean empty) {
                        super.updateItem(journal, empty);
                        if(empty) {
                            setStyle("");
                        }
                        else if(this.getItem().isDeleted()) {
                            setStyle("-fx-control-inner-background: lightcoral; ");
                        }
                        else {
                            setStyle("");
                        }
                    }
                        };     
                        //hideItem.setOnAction();
                        MenuItem deleteItem = new MenuItem("Delete");
                        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                            //Delete Function
                            int journalID = row.getItem().getId();
                            Database.changeDeletedStatus(journalID, 1);
                            journalTable.getItems().remove(row.getItem());
                            }
                        });
                        row.setOnMousePressed(event -> {
                            if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty()) && !row.getItem().isDeleted()) {
                                rowMenu.getItems().clear();
                                rowMenu.getItems().add(deleteItem);
                                rowMenu.show(journalTable, event.getScreenX(), event.getScreenY());
                            }
                            if (event.getButton() == MouseButton.PRIMARY && (! row.isEmpty()) && event.getClickCount() == 2) {
                                Journal journal = row.getItem();
                                JournalTest1.getInstance().setJournal(journal);
                                JournalTest1.getInstance().gotoEntry();
                            }
                        });
                        return row;
                    }
                });
    }
    
    public Journal returnSelected(Journal journal) {
        return journal;
    }
    
    public void handleCreateJournal () {
        JournalTest1.getInstance().gotoCreateJournal();
    }
    
    private void handleshowAllAction(ActionEvent e) throws SQLException, InvalidObjectException {
        if(showAllChbx.isSelected()) {
            JournalTest1.getInstance().loadAllJournals();
        }
        if (!showAllChbx.isSelected()) {
            JournalTest1.getInstance().loadJournal();
        }
    }
}
