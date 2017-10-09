/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.Database;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML private TableColumn<Journal, String> nameClm;
    @FXML private TableColumn<Journal, String> summaryClm;
    @FXML private TableColumn<Journal, String> dateClm;
    
    @FXML protected void processLogout() {
        JournalTest1.getInstance().userLogout();
    }
    
    public final Journal getJournal() {
        return JournalTest1.getInstance().getJournal();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
              final TableRow<Entry> row = new TableRow<>();
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
              
              row.setOnMouseClicked(event -> {
              if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty())) {
                  rowMenu.show(entryTable, event.getScreenX(), event.getScreenY());
              }
              if (event.getClickCount() == 2 && (! row.isEmpty())) {
                      Entry rowData = row.getItem();
                      JournalTest1.getInstance().currentEntry(rowData);
                      JournalTest1.getInstance().gotoEntry();
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
}
