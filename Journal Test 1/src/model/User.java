/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import journal.test.pkg1.JournalTest1;

/**
 *
 * @author Caldiddy's PC
 */
public class User {
    
    private StringProperty username = new SimpleStringProperty();
    private String password;
    private ObservableList<Journal> journals = FXCollections.observableArrayList();
    
    public User(String username, String password) {
        this.username.set(username);
        this.password = password;
        journals.add(new Journal("Welcome Journal"));
    }
    
    public static User of(String username) {
        List<User> searchList = JournalTest1.getInstance().getUserList();
        
        for (User user : searchList) {
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        
        return null;
    }
    
    public ObservableList<Journal> getJournals() {
        return journals;
    } 
    
    public String getUsername(){
        return username.get();
    }
    
    public String getPassword(){
        return password;
    }
    
    public boolean userExists(String username) {
        if(of(username) != null) {
            return true;
        }
        
        return false;
    }
    
    public void addJournal(Journal journal) {
        journals.add(journal);
    }
}
