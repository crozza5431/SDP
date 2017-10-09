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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    
    
    @FXML protected void processLogout() {
        JournalTest1.getInstance().userLogout();
    }
    
    public final Journal getJournal() {
        return JournalTest1.getInstance().getJournal();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hiddenChbx.setOnAction(e -> {
            try {
                handleButtonAction(e);
            } catch (SQLException ex) {
                Logger.getLogger(JournalController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidObjectException ex) {
                Logger.getLogger(JournalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
      entryTable.setItems(JournalTest1.getInstance().getJournal().getEntries());
      entryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      nameClm.setCellValueFactory(new PropertyValueFactory<>("name"));
      summaryClm.setCellValueFactory(new PropertyValueFactory<>("entry"));
      dateClm.setCellValueFactory(new PropertyValueFactory<>("date"));
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
                    else {
                        setStyle("");
                    }
              }
              };
              
              //Menu Items + Handlers
              final ContextMenu rowMenu = new ContextMenu();
              MenuItem hideItem = new MenuItem("Hide");
              hideItem.setOnAction(new EventHandler<ActionEvent>() {
                  
                  @Override
                  public void handle(ActionEvent event) {
                      //Hide Function
                      int entryID = row.getItem().getId();
                      Database.changeHiddenStatus(entryID, 1);
                      entryTable.getItems().remove(row.getItem());
                      System.out.println("Selected Entry ID is: " + entryID);
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
              
              rowMenu.getItems().addAll(hideItem, deleteItem);
              
              //Double Click feature
              row.setOnMouseClicked(event -> {
              if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty())) {
                  rowMenu.show(entryTable, event.getScreenX(), event.getScreenY());
              }
              if (event.getClickCount() == 2 && (! row.isEmpty())) {
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
        if(hiddenChbx.isSelected()) {
            JournalTest1.getInstance().loadHiddenEntry();
        }
        if(!hiddenChbx.isSelected()) {
            JournalTest1.getInstance().loadEntry();
        }
    }
}