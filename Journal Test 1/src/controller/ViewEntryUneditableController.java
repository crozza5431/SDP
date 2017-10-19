/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.InvalidObjectException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import journal.test.pkg1.JournalTest1;

/**
 *
 * @author User
 */
public class ViewEntryUneditableController implements Initializable{
    
    @FXML Text entryName;
    @FXML TextArea entryData;
    @FXML Button editEntryBtn;
    @FXML Text reason;
    @FXML TextArea reasonTxt;
    
    @FXML protected void handleEditEntry() {
        JournalTest1.getInstance().gotoEditEntry();
    }
    
    @FXML protected void handleBack() {
        JournalTest1.getInstance().gotoEntry();
    }
    
    @FXML protected void handleEntryHistory() throws SQLException, InvalidObjectException {
        JournalTest1.getInstance().loadHistory();
        JournalTest1.getInstance().gotoHistory();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reasonTxt.setVisible(false);
        reason.setVisible(false);
       entryName.setText(JournalTest1.getInstance().getCurrentEntry().getName());
       entryData.setText(JournalTest1.getInstance().getCurrentEntry().getEntry());
       if(JournalTest1.getInstance().getCurrentEntry().isDeleted()) {
           editEntryBtn.setDisable(true);
       }
       if(JournalTest1.getInstance().getCurrentEntry().getHistory()) {
           reason.setVisible(true);
            reasonTxt.setVisible(true);
            reasonTxt.setText(JournalTest1.getInstance().getCurrentEntry().getReason());
            reasonTxt.setEditable(false);
       }
       entryData.setEditable(false);
    }
}
