/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import journal.test.pkg1.JournalTest1;
import model.Entry;
import model.Journal;

/**
 *
 * @author User
 */
public class JournalController implements Initializable{
    @FXML private TableView<Entry> entryTable;
    @FXML private TableColumn<Entry, String> nameClm;
    @FXML private TableColumn<Entry, String> summaryClm;
    @FXML private TableColumn<Entry, String> dateClm;
    @FXML private CheckBox hiddenChbx;
    @FXML private CheckBox showAllChbx;
    @FXML private Button createEntryBtn;
    private ContextMenu rowMenu = new ContextMenu(); 
    
    
    @FXML protected void processLogout() {
        JournalTest1.getInstance().userLogout();
    }
    
    public final Journal getJournal() {
        return JournalTest1.getInstance().getJournal();
    }
    
     @FXML protected void processSearch() {
        JournalTest1.getInstance().getJournal().clearEntries();
        JournalTest1.getInstance().gotoSearch();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (JournalTest1.getInstance().getJournal().isDeleted()) {
            createEntryBtn.setDisable(true);
        }
        hiddenChbx.setOnAction(e -> {
            try {
                handleButtonAction(e);
            } catch (SQLException ex) {
                Logger.getLogger(JournalController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidObjectException ex) {
                Logger.getLogger(JournalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        showAllChbx.setOnAction(e -> {
            try {
                handleButtonAction(e);
            } catch (SQLException ex) {
                Logger.getLogger(JournalController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidObjectException ex) {
                Logger.getLogger(JournalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        entryTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                rowMenu.hide();
            }
        });
        entryTable.setItems(JournalTest1.getInstance().getJournal().getEntries());
        entryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        nameClm.setCellValueFactory(new PropertyValueFactory<>("name"));
        summaryClm.setCellValueFactory(new PropertyValueFactory<>("entry"));
        dateClm.setCellValueFactory(new PropertyValueFactory<>("dateFormatted"));
        //Double click event
        entryTable.setRowFactory(
                new Callback<TableView<Entry>, TableRow<Entry>>() {
                    
                    @Override
                    public TableRow<Entry> call(TableView<Entry> param) {
                        final TableRow<Entry> row = new TableRow<Entry>() {
                        //Highlight hidden journals
                            @Override
                            protected void updateItem(Entry entry, boolean empty){
                                super.updateItem(entry, empty);
                                if(empty) {
                                    setStyle("");
                                }
                                else if (this.getItem().getHidden()){
                                    setStyle("-fx-control-inner-background: lightsteelblue; ");
                                }
                                
                                else if (this.getItem().isDeleted()){
                                    setStyle("-fx-control-inner-background: lightcoral; ");
                                }
                                else {
                                    setStyle("");
                                }
                            }
                        };
                        //Menu Items + Handlers
                        MenuItem hideItem = new MenuItem("Hide");
                        hideItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                            //Hide Function
                                int entryID = row.getItem().getId();
                                Database.changeHiddenStatus(entryID, 1);
                                JournalTest1.getInstance().gotoEntry();
                            }
                        });
                        MenuItem deleteItem = new MenuItem("Delete");
                        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                            //Delete Function
                            int entryID = row.getItem().getId();
                            Database.setEntryDeletedStatus(entryID, 1);
                            entryTable.getItems().remove(row.getItem());
                            }
                        });
                        MenuItem unhideItem = new MenuItem("Unhide");
                        unhideItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                //Unhide Function
                                int entryID = row.getItem().getId();
                                Database.changeHiddenStatus(entryID, 0);
                                JournalTest1.getInstance().gotoEntry();
                            } 
                        }); 
                        //Double Click feature
                        row.setOnMousePressed(event -> {
                            if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty()) && row.getItem().getHidden()) {
                                rowMenu.getItems().clear();
                                rowMenu.getItems().addAll(unhideItem);
                                rowMenu.show(entryTable, event.getScreenX(), event.getScreenY());
                            }
                            else if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty()) && !row.getItem().isDeleted()) {
                                rowMenu.getItems().clear();
                                rowMenu.getItems().addAll(hideItem, deleteItem);
                                rowMenu.show(entryTable, event.getScreenX(), event.getScreenY());
                            }
                            if (event.getClickCount() == 2 && (! row.isEmpty()) && event.getButton() != MouseButton.SECONDARY) {
                                Entry rowData = row.getItem();
                                JournalTest1.getInstance().currentEntry(rowData);
                                JournalTest1.getInstance().gotoViewEntry();
                            }
                        });
                        return row;  
                    }
                });
    }
    
    public void handleCreateEntry(){
        JournalTest1.getInstance().gotoCreateEntry();
    }
    
    public void processBack() {
        JournalTest1.getInstance().gotoProfile();
    }

    private void handleButtonAction(ActionEvent e) throws SQLException, InvalidObjectException {
        if(hiddenChbx.isSelected() && showAllChbx.isSelected()) {
            JournalTest1.getInstance().loadAllEntries(0);
        }
        else if (hiddenChbx.isSelected() && !showAllChbx.isSelected()) {
            JournalTest1.getInstance().loadHiddenEntry();
        }
        else if (showAllChbx.isSelected() && !hiddenChbx.isSelected()) {
            JournalTest1.getInstance().loadAllEntries(1);
            JournalTest1.getInstance().getJournal().sortEntriesByDateAndGroupByID();
        }
        else {
            JournalTest1.getInstance().loadEntry();
        }
    }

    private void handleshowAllAction(ActionEvent e) throws SQLException, InvalidObjectException {
        if(showAllChbx.isSelected() && hiddenChbx.isSelected()) {
            JournalTest1.getInstance().loadAllEntries(0);
        }
        else if (showAllChbx.isSelected()) {
            JournalTest1.getInstance().loadAllEntries(1);
        }
        if(!showAllChbx.isSelected()) {
            JournalTest1.getInstance().loadEntry();
        }
    }
}