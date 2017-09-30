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
    
    private int ID;
    private StringProperty username = new SimpleStringProperty();
    private String password;
    private String salt;
    private String hint;
    private ObservableList<Journal> journals = FXCollections.observableArrayList();
    
    public User(int id, String username, String password, String salt, String hint) {
        this.ID = id;
        this.username.set(username);
        this.password = password;
        this.salt = salt;
        this.hint = hint;
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

    public int getID() { return ID; }

    public String getUsername(){
        return username.get();
    }
    
    public String getPassword(){
        return password;
    }

    public String getSalt() { return salt; }

    public String getHint() { return hint; }
    
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
