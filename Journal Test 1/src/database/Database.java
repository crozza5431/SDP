/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author DeZHONG
 */
// Database will use the SINGLETON pattern
public class Database
{
    // Singleton instance
    public static final Database INSTANCE = new Database();

    // Constructor
    public Database()
    {
        // Establishes connection with the SQL Database AZURE
        try {
            String connectionURL = "jdbc:sqlserver://team10sdp.Database.windows.net:1433;Database=JournalBuddy;user=Team10@team10sdp;password=Passw0rd;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.Database.windows.net;loginTimeout=30;";

            conn = DriverManager.getConnection(connectionURL);
        }
        catch ( SQLException err ) {
            System.out.println( err.getMessage() );
        }
    }

    Connection conn;

    //Get user info or null
    public User tryGetUser(String uName) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT * FROM users WHERE username ='" + uName + "'");
        r.last();

        if (r.getRow() > 1) throw new SQLException("Can't be more than one user with the same username");
        if (r.getRow() == 0) return null;

        return new User(
            r.getInt("ID"),
            uName,
            r.getString("password"),
            r.getString("salt"),
            r.getString("hint"));
    }
    
    //Checks for Duplicate ID
    public boolean checkDupUser(String uName) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT username FROM users WHERE username ='" + uName + "'");
        r.last();
        
        return (!(r.getRow() == 0));
    }

    //Inserts new user into Database
    public void newUser(String uName, String uPass, String uHint, String uSalt) throws SQLException {
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
    public int nextID() throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT ID FROM Users");
        r.last();
        return r.getInt("ID");
    }
    
    //returns a result set of a users journals
    public ResultSet getJournals(int id) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT * FROM journal Where user_id='" + id + "'");
        return r;
    }
}
