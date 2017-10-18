/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journal.test.pkg1;

import database.Database;

import java.io.InvalidObjectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Entry;
import model.Journal;
import model.User;

/**
 *
 * @author Caldiddy's PC
 */
public class JournalTest1 extends Application {
  
    private Stage stage;
    private User loggedUser;
    private Journal currentJournal;
    private Entry currentEntry;
    private static JournalTest1 instance;
    private static Database db;
    
    public JournalTest1() {
        instance = this;
        //Users.add(new User("c", "c"));
    }
    
    public static JournalTest1 getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch(args);
    }
     @Override
    public void start(Stage primarystage){
        try {
            stage = primarystage;
            gotoLogin();
            primarystage.show();
        }
        catch (Exception ex){
            
        }
    }
    
    public void currentEntry(Entry entry) {
        currentEntry = entry;
    }
    
    public Entry getCurrentEntry() {
        return currentEntry;
    }
    
    public User getLoggedUser() {
        return loggedUser;
    }
    
    public Journal getJournal() {
        return currentJournal;
    }
    
    public void setJournal(Journal journal) {
        currentJournal = journal;
    }

    public void userLogging(User user) throws SQLException, InvalidObjectException {
        loggedUser = user;
        loadJournal();
        gotoProfile();
    }
    
    public void userLogout(){
        loggedUser = null;
        gotoLogin();
    }
    
    public void gotoRegister() {
        try {
            replaceSceneContext("/view/register.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){
           System.out.println(ex);
        }
    }
    public void gotoProfile() {
        try {
            replaceSceneContext("/view/profile.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){
           System.out.println(ex);
        }
    }
    
    public void gotoLogin() {
        try {
            replaceSceneContext("/view/login.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){
        }
    }
    
    public void gotoCreateEntry() {
        try {
            replaceSceneContext("/view/createEntry.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex) {
            
        }
    }
    
    public void gotoEntry() {
        try {
            loadEntry();
            replaceSceneContext("/view/journal.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){

        }
    }
    
    public Journal returnSelected(Journal selectedJournal) {
        currentJournal = selectedJournal;
        return currentJournal;
    }
        
    public Parent replaceSceneContext(String fxml) throws Exception {
        Parent page = (Parent) FXMLLoader.load(JournalTest1.class.getResource(fxml), null, new JavaFXBuilderFactory());
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page, 700, 550);
            stage.setScene(scene);
            stage.setTitle("Journal Application");
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        return page;
    }
    
    public void gotoCreateJournal() {
        try {
            replaceSceneContext("/view/createJournal.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){

        }
    }
    
    public void loadJournal() throws SQLException, InvalidObjectException {
        loggedUser.clearJournals();
        LinkedList<Journal> journals = Database.getJournals(loggedUser.getID());
        for (Journal journal : journals)
        {
            loggedUser.addJournal(journal);
        }
    }
    
    public Journal getJournal(int id) {
        for (Journal journal : loggedUser.getJournals()) {
            if (journal.getId() == id) {
                return journal;
            }
        }
        
        return null;
    }
    
    public void loadEntry() throws SQLException, InvalidObjectException {
        currentJournal.clearEntries();
        LinkedList<Entry> entries = Database.getEntry(currentJournal.getId(), false);
        for (Entry entry : entries)
        {
            currentJournal.addEntry(entry);
        }
    }

    public void loadHistory() throws SQLException, InvalidObjectException {
        currentJournal.clearEntries();
        LinkedList<Entry> entries = Database.retrieveEntryHistory(currentEntry.getId());
        for (Entry entry : entries)
        {
            currentJournal.addEntry(entry);
        }
    }
    
    public void gotoEditEntry() {
        try {
            replaceSceneContext("/view/viewEntryEditable.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){

        }
    }
    
    public void gotoViewEntry() {
        try {
            replaceSceneContext("/view/viewEntryUneditable.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){

        }
    }
    
    public void loadHiddenEntry() throws SQLException, InvalidObjectException {
        currentJournal.clearEntries();
        LinkedList<Entry> entries = Database.getEntry(currentJournal.getId(), true);
        for (Entry entry : entries)
        {
            currentJournal.addEntry(entry);
        }
    }

    public void gotoHistory() {
        try {
            replaceSceneContext("/view/entryHistory.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){

        }
    }
    
    public void gotoViewHistory() {
        try {
            replaceSceneContext("/view/viewHistory.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){

        }
    }

    public void loadAllEntries(int showHidden) throws SQLException, InvalidObjectException {
        currentJournal.clearEntries();
        LinkedList<Entry> entries = Database.getAllEntries(currentJournal.getId(), showHidden);
        currentJournal.addEntries(entries);
    }
    
    public void loadAllJournals() throws SQLException, InvalidObjectException {
        loggedUser.clearJournals();
        LinkedList<Journal> journals = Database.getAllJournals(loggedUser.getID());
        for (Journal journal : journals)
        {
            loggedUser.addJournal(journal);
        }
    }
    
    public void loadSearches(int id, String keyword, LocalDate before, LocalDate after, int hid, int delete, int hist) throws SQLException, InvalidObjectException {
        currentJournal.clearEntries();
        LinkedList<Entry> entries = Database.searchEntries(id, keyword, before, after, hid, delete, hist);
        for (Entry entry : entries)
        {
            currentJournal.addEntry(entry);
        }
    }

    public void gotoSearch() {
         try {
            replaceSceneContext("/view/Search.fxml");
            stage.setResizable(false);
            stage.sizeToScene();
        }
        catch (Exception ex){

        }
    }
}
