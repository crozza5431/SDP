/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
    
    public void handleCreateJournal(){
        //Journal journal = new Journal(journalName.getText());
        //JournalTest1.getInstance().getLoggedUser().addJournal(journal);
    }
    
    @FXML protected void processBack() {
        JournalTest1.getInstance().gotoProfile();
    }
}
