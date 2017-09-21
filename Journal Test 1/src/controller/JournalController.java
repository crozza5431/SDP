/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

    }
    
    public void handleCreateEntry(){
        JournalTest1.getInstance().gotoCreateEntry();
    }
    
    public void processBack() {
        JournalTest1.getInstance().gotoProfile();
    }
}
