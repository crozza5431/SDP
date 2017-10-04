/*
 * TESTING Netbeans
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. blah
 */
//dragonphlegm test
package controller;
//ass

import database.Database;
import java.io.InvalidObjectException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import journal.test.pkg1.JournalTest1;
import model.Entry;
//test
/**
 *
 * @author User
 */
public class CreateEntryController implements Initializable{
    //Test
    @FXML TextField entryName;
    @FXML TextArea entryContents;
    //further testing
    public void handleCreateEntry() throws SQLException, InvalidObjectException {
        String eName = entryName.getText();
        String data = entryContents.getText();
        int jID = JournalTest1.getInstance().getJournal().getId();
        Database.newEntry(jID, eName, data);
        JournalTest1.getInstance().gotoEntry();
        //Entry entry = new Entry(entryName.getText(), entryContents.getText());
        //JournalTest1.getInstance().getJournal().addEntry(entry);
    }
    
    @FXML protected void processBack() {
        JournalTest1.getInstance().gotoEntry();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
