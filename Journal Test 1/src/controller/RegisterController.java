/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.database;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import journal.test.pkg1.JournalTest1;

/**
 *
 * @author User
 */
public class RegisterController {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmedPassword;
    @FXML private Label errorMessage;
    
    @FXML protected void handleRegister() throws SQLException {
        if (errorCheck()) {
            String uName = username.getText();
            String uPass = password.getText();
            String uHint = "HINT";
            String uSalt = "SALT";
            
            database.newUser(uName, uPass, uHint, uSalt);
            /*
            if (JournalTest1.getInstance().addUser(username.getText(), password.getText())) {
                errorMessage.setText("Account successfully created!");
                JournalTest1.getInstance().gotoLogin();
            }
            
            else errorMessage.setText("Username already taken, please choose another!");
        */
        }
        
    }
    
    private boolean errorCheck() {
        if(!requiredCheck()) {
            return passwordCheck(password.getText(), confirmedPassword.getText());
        }  
        errorMessage.setText("Please ensure all fields are filled out");
        return false;
    }
    
    @FXML protected void processBack() {
        JournalTest1.getInstance().gotoLogin();
    }
    
    private boolean passwordCheck(String password, String confirmedpassword) {
        if (password.equals(confirmedpassword) && !password.isEmpty()) {
            return true;
        }
        
        else {
            errorMessage.setText("Please ensure passwords match!");
            return false;
        }
    }
    
    private boolean requiredCheck() {
        return (username.getText().isEmpty() && password.getText().isEmpty() && confirmedPassword.getText().isEmpty());
    }
}
