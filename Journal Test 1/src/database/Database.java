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
import java.sql.*;
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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setString(1, uName);
            ResultSet r = ps.executeQuery();

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
            PreparedStatement ps = conn.prepareStatement("SELECT username FROM users WHERE username = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setString(1, uName);
            ResultSet r = ps.executeQuery();

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
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Users VALUES (?, ?, ?, ?, ?)")
        ) {
            ps.setInt(1, uID);
            ps.setString(2, uName);
            ps.setString(3, uPass);
            ps.setString(4, uHint);
            ps.setString(5, uSalt);
            ps.executeUpdate();
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }

    //Searches for the next available user ID
    private static int nextID() throws SQLException, InvalidObjectException
    {
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT ID FROM Users", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = ps.executeQuery();

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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM journal Where user_id=? ORDER BY Date_created DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();

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
    
    //returns all result set of a users journals
    public static LinkedList<Journal> getAllJournals(int id) throws SQLException, InvalidObjectException
    {
        LinkedList<Journal> journals = new LinkedList<>();
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM journal Where user_id=? ORDER BY Date_created DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            while (r.next())
            {
                int jID = r.getInt("ID");
                int jUserID = r.getInt("User_ID");
                String jName = r.getString("Name");
                String jDateCreated = dateCorrectionFromUTC(r.getTimestamp("Date_created"));
                boolean deleted = r.getBoolean("Deleted");
                journals.add(new Journal(jID, jUserID, jName, jDateCreated, deleted));
            }
        }
        return journals;
    }
    
    //Inserts new journal into Database
    public static int newJournal(int userID, String name) throws SQLException, InvalidObjectException
    {
        int jID = nextJournalID() + 1;
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Journal VALUES (?, ?, ?, GETUTCDATE(), '0')")
        ) {
            ps.setInt(1, jID);
            ps.setInt(2, userID);
            ps.setString(3, name);
            ps.executeUpdate();
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
        return jID;
    }
    
    //Searches for the next available Journal ID
    private static int nextJournalID() throws SQLException, InvalidObjectException
    {
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT ID FROM Journal", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = ps.executeQuery();

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
            PreparedStatement ps = conn.prepareStatement("UPDATE journal SET Deleted=? WHERE ID=?")
        ) {
            ps.setInt(1, delete);
            ps.setInt(2, ID);
            ps.executeUpdate();
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE entry SET Deleted=? WHERE journal_ID=?")
        ) {
            ps.setInt(1, delete);
            ps.setInt(2, ID);
            ps.executeUpdate();
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
     //returns a result set of a users entries
    public static LinkedList<Entry> getEntry(int id, boolean showHidden) throws SQLException, InvalidObjectException
    {
        LinkedList<Entry> entries = new LinkedList<>();
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Entry Where journal_id=? ORDER BY Date_created DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();

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
                if (showHidden) {
                    if (!deleted && !history) entries.add(new Entry(eID, eJournalID, eName, eDateCreated, hidden, false, data, reason, false));
                }
                else {
                    if (!hidden && !deleted && !history) entries.add(new Entry(eID, eJournalID, eName, eDateCreated, false, false, data, reason, false));
                }
            }
        }
        return entries;
    }
    
         //returns a result set of a users entries
    public static LinkedList<Entry> getAllEntries(int id, int showHidden) throws SQLException, InvalidObjectException
    {
        LinkedList<Entry> entries = new LinkedList<>();
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Entry Where journal_id=? ORDER BY Date_created DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setInt(1, id);
            ResultSet r = ps.executeQuery();

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
                if (showHidden != 0) {
                    if (!hidden) entries.add(new Entry(eID, eJournalID, eName, eDateCreated, hidden, deleted, data, reason, history));
                }
                else entries.add(new Entry(eID, eJournalID, eName, eDateCreated, hidden, deleted, data, reason, history));
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
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Entry VALUES (?, ?, ?, GETUTCDATE(), '0', '0', ?, '', '0')")
        ) {
            ps.setInt(1, eID);
            ps.setInt(2, journalID);
            ps.setString(3, name);
            ps.setString(4, data);
            ps.executeUpdate();
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
    //Searches for the next available Journal ID
    private static int nextEntryID() throws SQLException, InvalidObjectException
    {
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT ID FROM Entry", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ResultSet r = ps.executeQuery();

            if (r == null) throw new InvalidObjectException("Hopefully r isn't null");
            r.last();
            return r.getInt("ID");
        }
    }
    
    //sets a entry to be "Hidden"
    public static void changeHiddenStatus(int ID, int hidden) 
    {
        int delete = 0;
        if (hidden == 0) delete = 0;
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE entry SET Hidden=?, Deleted=? WHERE ID=?");
        ) {
            ps.setInt(1, hidden);
            ps.setInt(2, delete);
            ps.setInt(3, ID);
            ps.executeUpdate();
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
    //changes the deleted status of the entry
    public static void setEntryDeletedStatus(int ID, int delete) 
    {
        int hidden = 0;
        if (delete == 0) hidden = 0;
        
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE entry SET Deleted=?, Hidden=? WHERE ID=?")
        ) {
            ps.setInt(1, delete);
            ps.setInt(2, hidden);
            ps.setInt(3, ID);
            ps.executeUpdate();
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }

    //retrieves most recent data ###NOT NEEDED
    public static String retrieveLatestEntryData(int ID) throws SQLException, InvalidObjectException
    {
        String entryData;
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Entry WHERE ID=? ORDER BY Date_created DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setInt(1, ID);
            ResultSet r = ps.executeQuery();

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
            PreparedStatement psHistory = conn.prepareStatement("UPDATE entry SET History=1 WHERE ID=?");
            PreparedStatement psInsert = conn.prepareStatement("INSERT INTO Entry VALUES (?, ?, ?, GETUTCDATE(), '0', '0', ?, ?, '0')")
        ) {
            psHistory.setInt(1, entryID);
            psHistory.executeUpdate();

            psInsert.setInt(1, entryID);
            psInsert.setInt(2, journalID);
            psInsert.setString(3, name);
            psInsert.setString(4, data);
            psInsert.setString(5, reason);
            psInsert.executeUpdate();
        }
        catch ( SQLException err ) {
            System.out.println(err);
        }
    }
    
    //retrieves most recent data ###NOT NEEDED
    public static LinkedList<Entry> retrieveEntryHistory(int ID) throws SQLException, InvalidObjectException
    {
        LinkedList<Entry> entries = new LinkedList<>();
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Entry WHERE ID=? ORDER BY Date_created DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setInt(1, ID);
            ResultSet r = ps.executeQuery();

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
                if (!deleted) {
                    entries.add(new Entry(eID, eJournalID, eName, eDateCreated, hidden, false, data, reason, history));
                }
            }
        }
        return entries;
    }
    
    //searches a users journals
    public static LinkedList<Entry> searchEntries(int id, String keyword, Date before, Date after, int hid, int delete, int hist) throws SQLException, InvalidObjectException {
        LinkedList<Entry> results = new LinkedList<>();
        String bef = null;
        String aft = null;
        if (before != null) {
            bef = dateLocaltoUTC(before);
        }
        if (after != null) {
            aft = dateLocaltoUTC(after);
        }
        
        String query = "";
        
        if (hid == 0) {
            query += " AND hidden=?";
        }
        if (delete == 0) {
            query += " AND deleted=?";
        }
        if (hist == 0) {
            query += " AND History=?";
        }
        
        if (!keyword.isEmpty()) {
            query += " AND DATA LIKE ?";
        }
        if (bef == null) {
            query += " AND date_created> ?";
        } else if (aft == null) {
            query += " AND date_created< ?";
        } else {
            query += " AND date_created BETWEEN ? AND ?";
        }
        
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Entry WHERE journal_ID=?" + query + " ORDER BY Date_created DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setInt(1, id);
            int count = 2;
            if (hid == 0)  {
                ps.setString(count, "0");
                count++;
            }
            if (delete == 0)  {
                ps.setString(count, "0");
                count++;
            }
            if (hist == 0)  {
                ps.setString(count, "0");
                count++;
            }
            if (!keyword.isEmpty()) {
                ps.setString(count, "%" + keyword + "%");
                count++;
            }
            if (bef == null) {
                ps.setString(count, aft);
            } else if (aft == null) {
                ps.setString(count, bef);
            } else {
                ps.setString(count, bef);
                count++;
                ps.setString(count, aft);
            }
            
            ResultSet r = ps.executeQuery();

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
                    results.add(new Entry(eID, eJournalID, eName, eDateCreated, hidden, deleted, data, reason, history));
                }
            }
        }
        return results;
    }

    //DO NOT NEED searches entries to find keywords
    public static LinkedList<Entry> searchEntriesKeyword(int id, String keyword, String hid) throws SQLException, InvalidObjectException 
    {
        LinkedList<Entry> results = new LinkedList<>();
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Entry WHERE User_ID=? Hidden=? AND Data LIKE ? ORDER BY Date_created DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setInt(1, id);
            ps.setString(2, hid);
            ps.setString(3, "%" + keyword + "%");
            ResultSet r = ps.executeQuery();

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
                    results.add(new Entry(eID, eJournalID, eName, eDateCreated, false, false, data, reason, false));
                }
            }
        }
        return results;
    }
    
    //DO NOT NEED searches entries to find entries BF, AF or between dates
    // dates are in YYYY-MM-DD format and in UTC time (matters if an entry was made before 11am as it will show previous day )
    public static LinkedList<Entry> searchEntriesDates(int id, String hid, String date1, String date2) throws SQLException, InvalidObjectException 
    {
        LinkedList<Entry> results = new LinkedList<>();
        String query;
        
        if (date1 == null) {
            query = "> ?";
        } else if (date2 == null) {
            query = "< ?";
        } else {
            query = " BETWEEN ? AND ?";
        }
        try (
            Connection conn = establishConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Entry WHERE User_ID=? Hidden=? AND Date_created" + query + " ORDER BY Date_created DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        ) {
            ps.setInt(1, id);
            ps.setString(2, hid);

            if (date1 == null) {
                ps.setString(3, date2);
            } else if (date2 == null) {
                ps.setString(3, date1);
            } else {
                ps.setString(3, date1);
                ps.setString(4, date2);
            }

            ResultSet r = ps.executeQuery();

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
                    results.add(new Entry(eID, eJournalID, eName, eDateCreated, hidden, deleted, data, reason, false));
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
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-mm-dd");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date UTC = new Date(dateFormatter.format(localTime));
        return dateFormatter.format(UTC);
    }
}
