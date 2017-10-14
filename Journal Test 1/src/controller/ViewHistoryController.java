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
import javafx.scene.text.Text;
import journal.test.pkg1.JournalTest1;

/**
 *
 * @author User
 */
public class ViewHistoryController implements Initializable{
    
    @FXML Text entryName;
    @FXML Text entryData;
    
    
    @FXML protected void handleBack() {
        JournalTest1.getInstance().gotoHistory();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       entryName.setText(JournalTest1.getInstance().getCurrentEntry().getName());
       entryData.setText(JournalTest1.getInstance().getCurrentEntry().getEntry());
    }
}
