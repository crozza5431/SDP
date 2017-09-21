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
    private StringProperty name = new SimpleStringProperty();
    private final Date date;
    
    public Journal(String name) {
        date = new Date();
        this.name.set(name); 
        Entry.add(new Entry("My first entry", "Welcome!"));
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
