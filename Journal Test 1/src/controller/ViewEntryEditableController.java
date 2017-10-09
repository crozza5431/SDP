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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import journal.test.pkg1.JournalTest1;

/**
 *
 * @author User
 */
public class ViewEntryEditableController implements Initializable{
    @FXML TextArea entryData;
    @FXML TextArea entryReason;
    @FXML Text entryName;
    
    
    @FXML protected void handleBack() {
        JournalTest1.getInstance().gotoEntry();
    }
    
    @FXML protected void handleSave() throws SQLException, InvalidObjectException {
        int journalId = JournalTest1.getInstance().getJournal().getId();
        int entryId = JournalTest1.getInstance().getCurrentEntry().getId();
        String name = JournalTest1.getInstance().getCurrentEntry().getName();
        String data = entryData.getText();
        String reason = entryReason.getText();
        
        Database.updateEntry(journalId, entryId, name, data, reason);
        JournalTest1.getInstance().gotoEntry();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entryData.setText(JournalTest1.getInstance().getCurrentEntry().getEntry());
        entryReason.setText(JournalTest1.getInstance().getCurrentEntry().getReason());
        entryName.setText("Edit: " + JournalTest1.getInstance().getCurrentEntry().getName());
    }
}
