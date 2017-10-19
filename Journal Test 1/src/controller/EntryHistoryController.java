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
import javafx.scene.text.Text;
import javafx.util.Callback;
import journal.test.pkg1.JournalTest1;
import model.Entry;

/**
 *
 * @author User
 */
public class EntryHistoryController implements Initializable{
    @FXML TableView historyTable;
    @FXML TableColumn nameClm;
    @FXML TableColumn dateClm;
    @FXML TableColumn reasonClm;
    @FXML Text entryName;
    @FXML protected void handleBack() {
        JournalTest1.getInstance().gotoEntry();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entryName.setText("History: " + JournalTest1.getInstance().getCurrentEntry().getName());
        historyTable.setItems(JournalTest1.getInstance().getJournal().getEntries());
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        nameClm.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateClm.setCellValueFactory(new PropertyValueFactory<>("date"));
        reasonClm.setCellValueFactory(new PropertyValueFactory<>("reason"));
        historyTable.setRowFactory(
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
                    else if (!this.getItem().getHistory()){
                        setStyle("-fx-control-inner-background: lightsteelblue; ");
                    }
                    else if (this.getItem().isDeleted()){
                        setStyle("-fx-control-inner-background: lightcoral; ");
                    }
                    else if (this.getItem().getHistory()){
                        setStyle("-fx-control-inner-background: palegreen; ");
                    }
                    else {
                        setStyle("");
                    }
              }
              };  
           
              
              //Double Click feature
              row.setOnMouseClicked(event -> {
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
}
