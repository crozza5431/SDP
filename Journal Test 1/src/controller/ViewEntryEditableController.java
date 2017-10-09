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
        JournalTest1.getInstance().gotoViewEntry();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entryData.setText(JournalTest1.getInstance().getCurrentEntry().getEntry());
        entryReason.setText(JournalTest1.getInstance().getCurrentEntry().getReason());
        entryName.setText("Edit: " + JournalTest1.getInstance().getCurrentEntry().getName());
    }
}
