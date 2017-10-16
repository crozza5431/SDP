/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.InvalidObjectException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.util.Callback;
import journal.test.pkg1.JournalTest1;
import model.Entry;

/**
 *
 * @author User
 */
public class SearchController implements Initializable{
    @FXML TextField keyWord;
    @FXML DatePicker firstDate;
    @FXML DatePicker secondDate;
    @FXML TableView searchTbl;
    @FXML TableColumn nameClm;
    @FXML TableColumn summaryClm;
    @FXML TableColumn dateClm;
    @FXML CheckBox historyChbx;
    @FXML CheckBox hiddenChbx;
    @FXML CheckBox deletedChbx;
    @FXML Text searchName;
    int hid = 0;
    int delete = 0;
    int hist = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchName.setText("Search: " + JournalTest1.getInstance().getJournal().getName());
        searchTbl.setItems(JournalTest1.getInstance().getJournal().getEntries());
        searchTbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        
        searchTbl.setRowFactory(
                new Callback<TableView<Entry>, TableRow<Entry>>() {
                    
                    @Override
                    public TableRow<Entry> call(TableView<Entry> param) {
                        final TableRow<Entry> row = new TableRow<Entry>() {
                        //Highlight hidden journals
                            @Override
                            protected void updateItem(Entry entry, boolean empty){
                                super.updateItem(entry, empty);
                                if(empty) {
                                    setStyle("");
                                }
                                else if (this.getItem().getHidden()){
                                    setStyle("-fx-control-inner-background: lightsteelblue; ");
                                }
                                
                                else if (this.getItem().isDeleted()){
                                    setStyle("-fx-control-inner-background: lightcoral; ");
                                }
                                else {
                                    setStyle("");
                                }
                            }
                        };
                        
                        row.setOnMousePressed(event -> {
                            if (event.getClickCount() == 2 && (! row.isEmpty()) && event.getButton() != MouseButton.SECONDARY) {
                                Entry rowData = row.getItem();
                                JournalTest1.getInstance().currentEntry(rowData);
                                JournalTest1.getInstance().gotoViewEntry();
                            }
                        });
                        
                        return row;
    }
                });
                }
    
    @FXML protected void handleBack() {
        JournalTest1.getInstance().gotoEntry();
    }    
    
    @FXML protected void handleSearch() throws SQLException, InvalidObjectException {
        int jID = JournalTest1.getInstance().getJournal().getId();
        
        String keyword = keyWord.getText();
        LocalDate before = null;
        LocalDate after = null;
        if (secondDate.getValue() != null) {
            before = secondDate.getValue();
        }
        if (firstDate.getValue() != null) {
            after = firstDate.getValue();
        }
        
        JournalTest1.getInstance().loadSearches(jID, keyword, before, after, hid, delete, hist);
    }

    private void handleButtonAction(ActionEvent e) {
        if(historyChbx.isSelected()) {
            hist = 1;
        }
        if(!historyChbx.isSelected()) {
            hist = 0;
        }
        if(hiddenChbx.isSelected()) {
            hid = 1;
        }
        if(!hiddenChbx.isSelected()) {
            hid = 0;
        }
        if(deletedChbx.isSelected()) {
            delete = 1;
        }
        if(!deletedChbx.isSelected()) {
            delete = 0;
        }
    }
}
