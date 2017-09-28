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
    private int count = 0;
    
    
    // login ###need to create an extra hint line as "Incorrect Login Details" 
    // will replace hint when hint activates
    @FXML protected void handleLogin(ActionEvent event) throws SQLException {
        String uName = userName.getText();
        String uPass = password.getText();
        
        if (!database.checkUser(uName)) {
            errorMessage.setText("User Does Not Exist!");
        } else {
            if (count >= 3) {
                errorMessage.setText(database.getHint(uName));
            }
            if (!database.checkPassword(uName, uPass)) {
                count++;
                errorMessage.setText("Incorrect Login Details");
            } else {
                count = 0;
                //userLogging(database.getID(uName));
                System.out.println("Correct");
            }
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
