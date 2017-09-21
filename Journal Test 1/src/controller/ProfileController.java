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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import journal.test.pkg1.JournalTest1;
import model.Journal;
import model.User;

public class ProfileController implements Initializable {
    @FXML private Label userProfile;
    @FXML private TableView<Journal> journalTable;
    @FXML private TableColumn<Journal, String> nameClm;
    @FXML private TableColumn<Journal, String> dateClm;

    @FXML protected void processLogout() {
        JournalTest1.getInstance().userLogout();
    }
    
    public final User getUser() {
        return JournalTest1.getInstance().getLoggedUser();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      journalTable.setItems(JournalTest1.getInstance().getLoggedUser().getJournals());
      journalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      nameClm.setCellValueFactory(new PropertyValueFactory<>("name"));
      dateClm.setCellValueFactory(new PropertyValueFactory<>("date"));
      //Double click event
      journalTable.setRowFactory(tv-> {
          TableRow<Journal> row = new TableRow<>();
          row.setOnMouseClicked(event -> {
              if (event.getClickCount() == 2 && (! row.isEmpty())) {
                  Journal rowData = row.getItem();
                  JournalTest1.getInstance().returnSelected(rowData);
                  JournalTest1.getInstance().gotoEntry();
              }
          });
        return row;
      }); 
    }
    
    public Journal returnSelected(Journal journal) {
        return journal;
    }
    
    public void handleCreateJournal () {
        JournalTest1.getInstance().gotoCreateJournal();
    }
}
