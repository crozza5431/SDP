/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.*;
import javafx.beans.property.*;

/**
 *
 * @author User
 */
public class Entry {
    //Github test!
    private final int id;
    private final int journalID;
    private StringProperty name = new SimpleStringProperty();
    private final String date;
    private boolean hidden;
    private boolean deleted;
    private StringProperty entry = new SimpleStringProperty();
    private String reason;
    
    public Entry(int id, int journalID, String name, String date, boolean hidden, boolean deleted, String data, String reason) {
        this.id = id;
        this.journalID = journalID;
        this.name.set(name);
        this.date = date;
        this.hidden = hidden;
        this.deleted = deleted;
        this.entry.set(data);
        this.reason = reason;
    }
    
    public int getId() {
        return id;
    }
    public String getName() {
        return name.get();
    }
    
    public String getEntry() {
        return entry.get();
    }
    
    public String getDate() {
        return date.toString();
    }
}
