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
import journal.test.pkg1.DateFormatter;

import static java.util.stream.Collectors.*;


/**
 *
 * @author Caldiddy's PC
 */
public class Journal {
    private ObservableList<Entry> Entries = FXCollections.observableArrayList();
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
        //Entries.add(new Entries("My first entry", "Welcome!"));
    }
    
    public Date getDate(){
        return date;
    }

    public String getDateFormatted() { return DateFormatter.Format(date); }
    
    public String getName(){
        return name.get();
    }
    
    public int getId(){
        return id;
    }
    
    public ObservableList<Entry> getEntries() { return Entries; }
    
    public void addEntry(Entry entry)
    {
        Entries.add(entry);
    }

    public void addEntries(List<Entry> entries)
    {
    	Entries.addAll(entries);
    }

    public void clearEntries() {
        Entries.clear();
    }
    
    public boolean isDeleted() {
        return deleted;
    }

    public void sortEntriesByDateAndGroupByID()
    {
    	Comparator<Entry> comparator = (o1, o2) -> o1.getDate().compareTo(o2.getDate());

    	List<Entry> sortedEntries = Entries.stream()
		    .sorted(comparator.reversed())
		    .collect(groupingBy(Entry::getId, LinkedHashMap::new, toList()))
		    .values().stream()
		    .flatMap(Collection::stream)
		    .collect(toList());

    	Entries.clear();
    	Entries.addAll(sortedEntries);
    }
}
