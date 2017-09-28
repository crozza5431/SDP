/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Caldiddy's PC
 */
 
import database.database;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import journal.test.pkg1.JournalTest1;
 
public class LoginController {
    @FXML private TextField userName;
    @FXML private PasswordField password;
    @FXML private Label errorMessage;
    
    @FXML protected void handleLogin(ActionEvent event) throws SQLException {
        if (!database.checkPassword(userName.getText(), password.getText())) {
            errorMessage.setText("Incorrect Login Details");
        } else {
            System.out.println("Correct");
        }
        //without db
        /**
        if (!JournalTest1.getInstance().userLogging(userName.getText(), password.getText())) {
        errorMessage.setText("Incorrect Login Details");
        }
        */
    }
    
    @FXML protected void handleRegister(ActionEvent event) {
        JournalTest1.getInstance().gotoRegister();
    }

}
