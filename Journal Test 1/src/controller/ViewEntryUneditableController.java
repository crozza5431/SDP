/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.fxml.FXML;
import journal.test.pkg1.JournalTest1;

/**
 *
 * @author User
 */
public class ViewEntryUneditableController {
    
    @FXML protected void handleEditEntry() {
        JournalTest1.getInstance().gotoEditEntry();
    }
    
}
