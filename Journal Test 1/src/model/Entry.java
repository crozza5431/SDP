/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.*;
import javafx.beans.property.*;
import journal.test.pkg1.DateFormatter;

/**
 *
 * @author User
 */
public class Entry {
    private final int id;
    private final int journalID;
    private StringProperty name = new SimpleStringProperty();
    private final Date date;
    private boolean hidden;
    private boolean deleted;
    private StringProperty entry = new SimpleStringProperty();
    private String reason;
    private boolean history;
    
    public Entry(int id, int journalID, String name, Date date, boolean hidden, boolean deleted, String data, String reason, boolean history) {
        this.id = id;
        this.journalID = journalID;
        this.name.set(name);
        this.date = date;
        this.hidden = hidden;
        this.deleted = deleted;
        this.entry.set(data);
        this.reason = reason;
        this.history = history;
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
    
    public Date getDate() { return date; }

    public String getDateFormatted() { return DateFormatter.Format(date); }
    
    public String getReason() {
        return reason;
    }
    
    public boolean getHidden() {
        return hidden;
    }
    
    public boolean getHistory() {
        return history;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
}
