/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journal.test.pkg1;

import database.database;
import java.sql.Connection;
import java.util.List;
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
    private ObservableList<User> Users = FXCollections.observableArrayList();
    private static JournalTest1 instance;
    private static database db;
    
    public JournalTest1() {
        //instance = this;
        //Users.add(new User("c", "c"));
    }
    
    public static JournalTest1 getInstance() {
        return instance;
    }
    
    public List<User> getUserList() {
        return Users;
    }
    
    public static void main(String[] args) {
        database.establishCon();
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
    
    public User getLoggedUser() {
        return loggedUser;
    }
    
    public Journal getJournal() {
        return currentJournal;
    }

    public boolean userLogging(String username , String password) {
        if (checkCredentials(username, password)) {
            loggedUser = User.of(username);
            gotoProfile();
            return true;
        }
        
        else return false;
    }
    
    private boolean checkCredentials(String username, String password) {
        for(User user : Users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        
        return false;
    }
    
    public void userLogout(){
        loggedUser = null;
        gotoLogin();
    }
    
    public void gotoRegister() {
        try {
            replaceSceneContext("/view/register.fxml");
        }
        catch (Exception ex){
           System.out.println(ex);
        }
    }
    public void gotoProfile() {
        try {
            replaceSceneContext("/view/profile.fxml");
        }
        catch (Exception ex){
           System.out.println(ex);
        }
    }
    
    public void gotoLogin() {
        try {
            replaceSceneContext("/view/login.fxml");
        }
        catch (Exception ex){
        }
    }
    
    public void gotoCreateEntry() {
        try {
            replaceSceneContext("/view/createEntry.fxml");
        }
        catch (Exception ex) {
            
        }
    }
    
    public void gotoEntry() {
        try {
            replaceSceneContext("/view/journal.fxml");
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
    
    public boolean addUser(String username, String password) {
        User user = new User(username, password);
        
        if (!user.userExists(username)) {
            Users.add(user);
            return true;
        }
        return false;
    }
    
    public void gotoCreateJournal() {
        try {
            replaceSceneContext("/view/createJournal.fxml");
        }
        catch (Exception ex){

        }
    }
}
