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
    private StringProperty name = new SimpleStringProperty();
    private final Date date;
    private StringProperty entry = new SimpleStringProperty();
    
    public Entry(String name, String entry) {
        date = new Date();
        this.name.set(name);
        this.entry.set(entry);
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
