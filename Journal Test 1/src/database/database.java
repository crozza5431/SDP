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
 * @author tony9
 */
public class database {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Connection conn;
        
        conn = establishCon();
        
        newUser(conn);
        //login(conn);
    }
    
    // Establishes connection with the SQL database AZURE
    public static Connection establishCon() {
        Connection conn;
        try {
            
            String connectionURL = "jdbc:sqlserver://team10sdp.database.windows.net:1433;database=JournalBuddy;user=Team10@team10sdp;password=Passw0rd;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            
            conn = DriverManager.getConnection(connectionURL);
            
            return conn;
        }
        catch ( SQLException err ) {
            System.out.println( err.getMessage() );
            return conn = null;
        }
    }
    
    public static void login(Connection conn) throws SQLException {
        Scanner in = new Scanner(System.in);
        String uName;
        String uPass;
        
        System.out.println("Enter Username: ");
        uName = in.nextLine();
        
        System.out.println("Enter Password: ");
        uPass = in.nextLine();
        
        if (checkUser(conn, uName)) {
            int count = 0;
            while (!checkPassword(conn, uName, uPass)) {
                ++count;
                System.out.println("Login Failed");
                if (count >= 3) {
                    System.out.println("HINT: " + getHint(conn, uName));
                }
                System.out.println("Enter Password: ");
                uPass = in.nextLine();
            }
            System.out.println("Login Success");
        } else { 
            System.out.println("User Does Not Exist");
        }

    }
    
    public static Boolean checkUser(Connection conn, String uName) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT username FROM users WHERE username='" + uName + "'");
        r.last();
        int count = r.getRow();
        return (count != 0);
    }
    
    public static Boolean checkPassword(Connection conn, String uName, String uPass) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT username, password FROM users WHERE username='" + uName + "' AND password='" + uPass + "'");
        return (r.next());
    }
    
    public static String getHint(Connection conn, String uName) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT Hint FROM users WHERE username='" + uName + "'");
        r.next();
        return r.getString("Hint");
    }

    public static void newUser(Connection conn) throws SQLException {
        Scanner in = new Scanner(System.in);
        String uName;
        String uPass;
        
        System.out.println("Enter Username: ");
        uName = in.nextLine();
        
        while (checkUser(conn, uName)) {
            System.out.println("Username Taken, Please Enter Another Username: ");
            uName = in.nextLine();
        }
        System.out.println("Enter Password: ");
        uPass = in.nextLine();
        
        System.out.println("Enter Hint: ");
        String uHint = in.nextLine();
        
        int uID = nextID(conn) + 1;
        
        Statement s = conn.createStatement();
        
        s.executeUpdate("INSERT INTO Users VALUES ('" + uID + "', '" + uName + "', '" + uPass + "', '" + uHint + "')");

    }
    
    public static int nextID(Connection conn) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT ID FROM Users");
        r.last();
        return r.getInt("ID");
    }
}
