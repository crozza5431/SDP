/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.Database;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import journal.test.pkg1.JournalTest1;
import model.Entry;
import model.Journal;

/**
 *
 * @author User
 */
public class CreateJournalController {
    @FXML private TextField journalName;
    
    public void handleCreateJournal() throws SQLException, InvalidObjectException {
        String jName = journalName.getText();
        int uID = JournalTest1.getInstance().getLoggedUser().getID();
        Database.newJournal(uID, jName);
        JournalTest1.getInstance().loadJournal();
        JournalTest1.getInstance().gotoProfile();
        
        //Journal journal = new Journal(journalName.getText());
        //JournalTest1.getInstance().getLoggedUser().addJournal(journal);
    }
    
    @FXML protected void processBack() {
        JournalTest1.getInstance().gotoProfile();
    }
}
