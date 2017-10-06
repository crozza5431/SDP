/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import model.Journal;
import model.User;
import model.Entry;

import java.io.InvalidObjectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author DeZHONG
 */
public class Database
{
    private static Connection establishConnection() throws SQLException
    {
        // Establishes connection with the SQL Database AZURE
        try {
            String connectionURL = "jdbc:sqlserver://team10sdpaus.database.windows.net:1433;database=JournalBuddy;user=Team10SDP@team10sdpaus;password=Passw0rd;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

            return DriverManager.getConnection(connectionURL);
        }
        catch ( SQLException err ) {
            System.out.println( err.getMessage() );
            throw err;
        }
    }

    //Get user info or null
    public static User tryGetUser(String uName) throws SQLException, InvalidObjectException
    {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = s.executeQuery("SELECT * FROM users WHERE username ='" + uName + "'");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
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
    }
    
    //Checks for Duplicate ID
    public static boolean checkDupUser(String uName) throws SQLException, InvalidObjectException
    {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = s.executeQuery("SELECT username FROM users WHERE username ='" + uName + "'");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            r.last();

            return (!(r.getRow() == 0));
        }
    }

    //Inserts new user into Database
    public static void newUser(String uName, String uPass, String uHint, String uSalt) throws SQLException, InvalidObjectException
    {
        int uID = nextID() + 1;
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement()
        ) {
            s.executeUpdate("INSERT INTO Users VALUES ('" + uID + "', '" + uName + "', '" + uPass + "', '" + uHint + "', '" + uSalt + "')");
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }

    //Searches for the next available user ID
    static int nextID() throws SQLException, InvalidObjectException
    {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = s.executeQuery("SELECT ID FROM Users");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            r.last();
            return r.getInt("ID");
        }
    }
    
    //returns a result set of a users journals
    public static LinkedList<Journal> getJournals(int id) throws SQLException, InvalidObjectException
    {
        LinkedList<Journal> journals = new LinkedList<>();
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = s.executeQuery("SELECT * FROM journal Where user_id='" + id + "'");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            while (r.next())
            {
                int jID = r.getInt("ID");
                int jUserID = r.getInt("User_ID");
                String jName = r.getString("Name");
                Date jDateCreated = r.getDate("Date_created");
                boolean deleted = r.getBoolean("Deleted");
                if (!deleted) journals.add(new Journal(jID, jUserID, jName, jDateCreated, false));
            }
        }
        return journals;
    }
    
    //Inserts new journal into Database
    public static void newJournal(int userID, String name) throws SQLException, InvalidObjectException
    {
        int jID = nextJournalID() + 1;
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement()
        ) {
            s.executeUpdate("INSERT INTO Journal VALUES ('" + jID + "', '" + userID + "', '" + name + "', GETDATE ( ) , '0')");
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
    //Searches for the next available Journal ID
    static int nextJournalID() throws SQLException, InvalidObjectException
    {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = s.executeQuery("SELECT ID FROM Journal");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            r.last();
            return r.getInt("ID");
        }
    }
    
    //sets a journal to be "deleted"
    public static void changeDeletedStatus(int ID, int delete) {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement()
        ) {
            s.executeUpdate("UPDATE journal SET Deleted=" + delete + " WHERE ID=" + ID);
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
     //returns a result set of a users journals
    public static LinkedList<Entry> getEntry(int id) throws SQLException, InvalidObjectException
    {
        LinkedList<Entry> entries = new LinkedList<>();
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = s.executeQuery("SELECT * FROM Entry Where journal_id='" + id + "'");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            while (r.next())
            {
                int eID = r.getInt("ID");
                int eJournalID = r.getInt("Journal_ID");
                String eName = r.getString("Name");
                Date eDateCreated = r.getDate("Date_created");
                boolean hidden = r.getBoolean("Hidden");
                String data = r.getString("Data");
                String reason = r.getString("Reason");
                if (!hidden) {
                    entries.add(new Entry(eID, eJournalID, eName, eDateCreated, false, data, reason));
                }
            }
        }
        return entries;
    }
    
    //Inserts new entry into Database
    public static void newEntry(int journalID, String name, String data) throws SQLException, InvalidObjectException
    {
        int eID = nextEntryID() + 1;
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement()
        ) {
            s.executeUpdate("INSERT INTO Entry VALUES ('" + eID + "', '" + journalID + "', '" + name + "', GETUTCDATE ( ) , '0', '" + data + "', '')");
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
    //Searches for the next available Journal ID
    public static int nextEntryID() throws SQLException, InvalidObjectException
    {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = s.executeQuery("SELECT ID FROM Entry");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            r.last();
            return r.getInt("ID");
        }
    }
    
    //sets a entry to be "Hidden"
    public static void changeHiddenStatus(int ID, int hidden) {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement()
        ) {
            s.executeUpdate("UPDATE entry SET Deleted=" + hidden + " WHERE ID=" + ID);
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
        //retrieves most recent data
    public static String retrieveLatestEntry(int ID) throws SQLException, InvalidObjectException {
        String entryData = null;
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ) {
            ResultSet r = s.executeQuery("SELECT * FROM Entry WHERE ID=" + ID + " ORDER BY Date_created ASC");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            r.next();
            entryData = r.getString("Data");
        }
        return entryData;
    }
}
