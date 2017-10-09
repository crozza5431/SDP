/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import journal.test.pkg1.JournalTest1;

/**
 *
 * @author User
 */
public class ViewEntryUneditableController implements Initializable{
    
    @FXML Text entryName;
    @FXML Text entryData;
    
    @FXML protected void handleEditEntry() {
        JournalTest1.getInstance().gotoEditEntry();
    }
    
    @FXML protected void handleBack() {
        JournalTest1.getInstance().gotoEntry();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       entryName.setText(JournalTest1.getInstance().getCurrentEntry().getName());
       entryData.setText(JournalTest1.getInstance().getCurrentEntry().getEntry());
    }
}
