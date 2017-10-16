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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    @FXML CheckBox historyChbx;
    @FXML CheckBox hiddenChbx;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
    }
    
    @FXML protected void handleBack() {
        JournalTest1.getInstance().gotoProfile();
    }
}
