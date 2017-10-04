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


/**
 *
 * @author Caldiddy's PC
 */
public class Journal {
    private ObservableList<Entry> Entry = FXCollections.observableArrayList();
    private final int id;
    private final int userID;
    private StringProperty name = new SimpleStringProperty();
    private final Date date;
    private boolean deleted;
    
    public Journal(int id, int userID, String name, Date date, boolean deleted) {
        this.id = id;
        this.userID = userID;
        this.name.set(name);
        this.date = date;
        this.deleted = deleted;
        //date = new Date(); 
        //Entry.add(new Entry("My first entry", "Welcome!"));
    }
    
    public int getId(){
        return id;
    }
    
    public String getDate(){
        return date.toString();
    }
    
    public String getName(){
        return name.get();
    }
    
    public ObservableList<Entry> getEntries(){
        return Entry;
    }
    
    public void addEntry(Entry entry) {
        Entry.add(entry);
    }
}
