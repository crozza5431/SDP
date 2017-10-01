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
 
import database.Database;

import java.io.InvalidObjectException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import journal.test.pkg1.JournalTest1;
import model.User;

import javax.xml.bind.DatatypeConverter;

public class LoginController {
    @FXML private TextField userName;
    @FXML private PasswordField password;
    @FXML private Label errorMessage;
    @FXML private Label hintMessage;
    private int count = 0;
    private int previousUserID = -1;
    
    
    // login ###need to create an extra hint line as "Incorrect Login Details" 
    // will replace hint when hint activates
    @FXML protected void handleLogin(ActionEvent event) throws SQLException, InvalidObjectException {
        String uName = userName.getText();
        String uPass = password.getText();

        //get user information from the database
        User user = Database.tryGetUser(uName);
        if (user == null) errorMessage.setText("Incorrect Username or Password");
        else
        {
            // password and salt are stored as HEX strings
            byte[] dbHash = DatatypeConverter.parseHexBinary(user.getPassword());
            byte[] salt = DatatypeConverter.parseHexBinary(user.getSalt());
            if (PasswordHelper.passwordMatches(uPass.toCharArray(), salt, dbHash))
            {
                count = 0;
                JournalTest1.getInstance().userLogging(user);
                System.out.println("Correct");
            }
            else
            {
                errorMessage.setText("Incorrect Username or Password");

                // make sure we're not showing hints for the wrong user
                if (user.getID() == previousUserID && count >= 2)
                {
                    hintMessage.setText("Hint: " + user.getHint());
                    count = 0;
                }
                else
                {
                    previousUserID = user.getID();
                    hintMessage.setText("");
                    count++;
                }
            }
        }
    }
    
    @FXML protected void handleRegister(ActionEvent event) {
        JournalTest1.getInstance().gotoRegister();
    }

}
