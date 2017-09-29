/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author DeZHONG
 */
public class database {
    public static Connection conn;
    /**
     * @param args the command line arguments
     */
    
    // Establishes connection with the SQL database AZURE
    public static void establishCon() {
        try {            
            String connectionURL = "jdbc:sqlserver://team10sdp.database.windows.net:1433;database=JournalBuddy;user=Team10@team10sdp;password=Passw0rd;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            
            conn = DriverManager.getConnection(connectionURL);
        }
        catch ( SQLException err ) {
            System.out.println( err.getMessage() );
        }
    }
    
    //Checks if there is that user
    public static Boolean checkUser(String uName) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT username FROM users WHERE username='" + uName + "'");
        r.last();
        int count = r.getRow();
        return (count != 0);
    }
    
    //Checks Password with username - may not be needed
    public static Boolean checkPassword(String uName, String uPass) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT username, password FROM users WHERE username='" + uName + "' AND password='" + uPass + "'");
        return (r.next());
    }
    
    //Returns Password and Salt associated with username
    public static String [] getPasswordSalt(String uName) throws SQLException {
        String passSalt[] = null;
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT password, salt FROM users WHERE username='" + uName + "'");
        r.next();
        passSalt[0] = r.getString("password");
        passSalt[1] = r.getString("salt");
        //make sure only one row returned
        return passSalt;
    }
    
    //Gets hint
    public static String getHint(String uName) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT Hint FROM users WHERE username='" + uName + "'");
        r.next();
        return r.getString("Hint");
    }

    //Inserts new user into database
    public static void newUser(String uName, String uPass, String uHint, String uSalt) throws SQLException {
        int uID = nextID() + 1;
        Statement s = conn.createStatement();
        try {
            s.executeUpdate("INSERT INTO Users VALUES ('" + uID + "', '" + uName + "', '" + uPass + "', '" + uHint + "', '" + uSalt + "')");    
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    //Searches for the next available ID
    public static int nextID() throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT ID FROM Users");
        r.last();
        return r.getInt("ID");
    }
    
    public static int getID(String uName) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT ID FROM Users WHERE username='" + uName + "'");
        r.next();
        return r.getInt("ID");
    }
    
    
}
