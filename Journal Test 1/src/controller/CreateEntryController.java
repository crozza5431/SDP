/*
 * TESTING Netbeans
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//dragonphlegm test
package controller;

import java.net.URL;
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
    
    public void handleCreateEntry(){
        Entry entry = new Entry(entryName.getText(), entryContents.getText());
        JournalTest1.getInstance().getJournal().addEntry(entry);
    }
    
    @FXML protected void processBack() {
        JournalTest1.getInstance().gotoEntry();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
