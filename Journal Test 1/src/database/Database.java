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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.TimeZone;

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
                String jDateCreated = dateCorrectionFromUTC(r.getTimestamp("Date_created"));
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
            s.executeUpdate("INSERT INTO Journal VALUES ('" + jID + "', '" + userID + "', '" + name + "', GETUTCDATE ( ) , '0')");
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
    public static void changeDeletedStatus(int ID, int delete) 
    {
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
                String eDateCreated = dateCorrectionFromUTC(r.getTimestamp("Date_created"));
                boolean hidden = r.getBoolean("Hidden");
                boolean deleted = r.getBoolean("Deleted");
                String data = r.getString("Data");
                String reason = r.getString("Reason");
                boolean history = r.getBoolean("History");
                if (!hidden && !deleted && !history) {
                    entries.add(new Entry(eID, eJournalID, eName, eDateCreated, false, false, data, reason, history));
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
            s.executeUpdate("INSERT INTO Entry VALUES ('" + eID + "', '" + journalID + "', '" + name + "', GETUTCDATE ( ) , '0', '0', '" + data + "', '', '0')");
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
    public static void changeHiddenStatus(int ID, int hidden) 
    {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement()
        ) {
            s.executeUpdate("UPDATE entry SET Hidden=" + hidden + " WHERE ID=" + ID);
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
    //changes the deleted status of the entry
    public static void setEntryDeletedStatus(int ID, int delete) 
    {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement()
        ) {
            s.executeUpdate("UPDATE entry SET Deleted=" + delete + " WHERE ID=" + ID);
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
    //retrieves most recent data ###NOT NEEDED
    public static String retrieveLatestEntryData(int ID) throws SQLException, InvalidObjectException 
    {
        String entryData = null;
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ) {
            ResultSet r = s.executeQuery("SELECT * FROM Entry WHERE ID=" + ID + " ORDER BY Date_created DESC");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            r.next();
            entryData = r.getString("Data");
        }
        return entryData;
    }
    
    //update entry
    public static void updateEntry(int journalID, int entryID, String name, String data, String reason) throws SQLException, InvalidObjectException
    {
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement()
        ) {
            s.executeUpdate("UPDATE entry SET History=1 WHERE ID=" + entryID + ")");
            s.executeUpdate("INSERT INTO Entry VALUES ('" + entryID + "', '" + journalID + "', '" + name + "', GETUTCDATE ( ) , '0', '0', '" + data + "', '" + reason + "', '0')");
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
    //retrieves most recent data ###NOT NEEDED
    public static LinkedList<Entry> retrieveEntryHistory(int ID) throws SQLException, InvalidObjectException 
    {
        LinkedList<Entry> entries = new LinkedList<>();
        String entryData = null;
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ) {
            ResultSet r = s.executeQuery("SELECT * FROM Entry WHERE ID=" + ID + " ORDER BY Date_created DESC");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            while (r.next())
            {
                int eID = r.getInt("ID");
                int eJournalID = r.getInt("Journal_ID");
                String eName = r.getString("Name");
                String eDateCreated = dateCorrectionFromUTC(r.getTimestamp("Date_created"));
                boolean hidden = r.getBoolean("Hidden");
                boolean deleted = r.getBoolean("Deleted");
                String data = r.getString("Data");
                String reason = r.getString("Reason");
                boolean history = r.getBoolean("History");
                if (!hidden && !deleted && !history) {
                    entries.add(new Entry(eID, eJournalID, eName, eDateCreated, false, false, data, reason, history));
                }
            }
        }
        return entries;
    }
    
    //searches entries to find keywords
    public static LinkedList<Entry> searchEntriesKeyword(int id, String keyword, String hid) throws SQLException, InvalidObjectException 
    {
        LinkedList<Entry> results = new LinkedList<>();
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = s.executeQuery("SELECT * FROM Entry WHERE User_ID=" + id + " Hidden=" + hid + " AND Data LIKE '%" + keyword + "%'");

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            while (r.next())
            {
                int eID = r.getInt("ID");
                int eJournalID = r.getInt("Journal_ID");
                String eName = r.getString("Name");
                String eDateCreated = dateCorrectionFromUTC(r.getTimestamp("Date_created"));
                boolean hidden = r.getBoolean("Hidden");
                boolean deleted = r.getBoolean("Deleted");
                String data = r.getString("Data");
                String reason = r.getString("Reason");
                boolean history = r.getBoolean("History");
                if (!hidden && !deleted && !history) {
                    results.add(new Entry(eID, eJournalID, eName, eDateCreated, false, false, data, reason, history));
                }
            }
        }
        return results;
    }
    
    //searches entries to find entries BF, AF or between dates
    // dates are in YYYY-MM-DD format and in UTC time (matters if an entry was made before 11am as it will show previous day )
    public static LinkedList<Entry> searchEntriesDates(int id, String hid, String date1, String date2) throws SQLException, InvalidObjectException 
    {
        LinkedList<Entry> results = new LinkedList<>();
        String query;
        
        if (date1 == null) {
            query = ">'" + date1 + "'";
        } else if (date2 == null) {
            query = "<'" + date1 + "'";
        } else {
            query = " BETWEEN '" + date1 + "' AND '" + date2 + "'";
        }
        try (
            Connection conn = establishConnection();
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = s.executeQuery("SELECT * FROM Entry WHERE User_ID=" + id + " Hidden=" + hid + " AND Date_created" + query);

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            while (r.next())
            {
                int eID = r.getInt("ID");
                int eJournalID = r.getInt("Journal_ID");
                String eName = r.getString("Name");
                String eDateCreated = dateCorrectionFromUTC(r.getTimestamp("Date_created"));
                boolean hidden = r.getBoolean("Hidden");
                boolean deleted  = r.getBoolean("Deleted");
                String data = r.getString("Data");
                String reason = r.getString("Reason");
                boolean history = r.getBoolean("History");
                if (!hidden && !deleted && !history) {
                    results.add(new Entry(eID, eJournalID, eName, eDateCreated, false, false, data, reason, history));
                }
            }
        }
        return results;
    }
    
    //Converts UTC to local time
    private static String dateCorrectionFromUTC(Date utcTime) 
    {
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(utcTime.getTime() + TimeZone.getTimeZone(timeZone).getOffset(utcTime.getTime()));
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' h:mm a z");
        return dateFormatter.format(local);
    }
    
    //Converts local time to UTC
    private static String dateLocaltoUTC(Date localTime) 
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss:fff");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date UTC = new Date(dateFormatter.format(localTime));
        return dateFormatter.format(UTC);
    }
}
