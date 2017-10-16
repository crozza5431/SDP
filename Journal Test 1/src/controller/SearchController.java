/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.InvalidObjectException;
import java.net.URL;
import java.sql.SQLException;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    String hid = "0";
    String delete = "0";
    String hist = "0";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchName.setText("Search: " + JournalTest1.getInstance().getJournal().getName());
        historyTbl.setItems(JournalTest1.getInstance().getJournal().getEntries());
        historyTbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        nameClm.setCellValueFactory(new PropertyValueFactory<>("name"));
        summaryClm.setCellValueFactory(new PropertyValueFactory<>("entry"));
        dateClm.setCellValueFactory(new PropertyValueFactory<>("date"));
        //CheckBox functionality
        historyChbx.setOnAction(e -> {
            handleButtonAction(e);
        });
        hiddenChbx.setOnAction(e -> {
            handleButtonAction(e);
        });
        deletedChbx.setOnAction(e -> {
            handleButtonAction(e);
        });
    }
    
    @FXML protected void handleBack() {
        JournalTest1.getInstance().gotoEntry();
    }    
    
    @FXML protected void handleSearch() throws SQLException, InvalidObjectException {
        int jID = JournalTest1.getInstance().getJournal().getId();
        
        String keyword = keyWord.getText();
        Date before = null;
        Date after = null;
        if (secondDate.getValue() != null) {
            before = java.sql.Date.valueOf(secondDate.getValue());
        }
        if (firstDate.getValue() != null) {
            after = java.sql.Date.valueOf(firstDate.getValue());
        }
        
        JournalTest1.getInstance().loadSearches(jID, keyword, before, after, hid, delete, hist);
    }

    private void handleButtonAction(ActionEvent e) {
        if(historyChbx.isSelected()) {
            hist = "1";
        }
        if(!historyChbx.isSelected()) {
            hist = "0";
        }
        if(hiddenChbx.isSelected()) {
            hid = "1";
        }
        if(!hiddenChbx.isSelected()) {
            hid = "0";
        }
        if(deletedChbx.isSelected()) {
            delete = "1";
        }
        if(!deletedChbx.isSelected()) {
            delete = "0";
        }
    }
}
