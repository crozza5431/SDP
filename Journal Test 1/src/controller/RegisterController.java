/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.Database;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import journal.test.pkg1.JournalTest1;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author User
 */
public class RegisterController {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmedPassword;
    @FXML private TextField hint;
    @FXML private Label errorMessage;
    
    @FXML protected void handleRegister() throws SQLException {
        if (errorCheck()) {
            String uName = username.getText();
            String uPass = password.getText();
            String uHint = hint.getText();

            byte[] salt = PasswordHelper.getNewSalt();
            String dbHash = DatatypeConverter.printHexBinary(PasswordHelper.getHash(uPass.toCharArray(), salt));
            String uSalt = DatatypeConverter.printHexBinary(salt);

            Database.INSTANCE.newUser(uName, dbHash, uHint, uSalt);
                JournalTest1.getInstance().gotoLogin();
        }
            else errorMessage.setText("Username already taken, please choose another!");
        
        }
        
    
    private boolean errorCheck() throws SQLException {
        if(!requiredCheck()) {
            if (Database.INSTANCE.checkDupUser(username.getText())) {
                errorMessage.setText("Username has already been taken!");
                return false;
            }
            return passwordCheck(password.getText(), confirmedPassword.getText());
        }  
        errorMessage.setText("Please ensure all fields are filled out");
        return false;
    }
    
    @FXML protected void processBack() {
        JournalTest1.getInstance().gotoLogin();
    }
    
    private boolean passwordCheck(String password, String confirmedpassword) {
        if (!password.isEmpty() && password.equals(confirmedpassword)) {
            return true;
        }
        errorMessage.setText("Please ensure passwords match!");
        return false;
    }
    
    private boolean requiredCheck() {
        return (username.getText().isEmpty() && password.getText().isEmpty() && confirmedPassword.getText().isEmpty());
    }
}
