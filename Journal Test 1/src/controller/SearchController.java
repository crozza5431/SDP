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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import journal.test.pkg1.JournalTest1;

/**
 *
 * @author User
 */
public class SearchController implements Initializable{
    @FXML TextField keyWord;
    @FXML DatePicker firstDate;
    @FXML DatePicker secondDate;
    @FXML TableView historyTbl;
    @FXML TableColumn nameClm;
    @FXML TableColumn summaryClm;
    @FXML TableColumn dateClm;
    @FXML CheckBox historyChbx;
    @FXML CheckBox hiddenChbx;
    @FXML CheckBox deletedChbx;
    @FXML Text searchName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchName.setText("Search: " + JournalTest1.getInstance().getJournal().getName());
        
        historyChbx.setOnAction(e -> {

        });
        
        hiddenChbx.setOnAction(e -> {

        });
    }
    
    @FXML protected void handleBack() {
        JournalTest1.getInstance().gotoProfile();
    }    
}
