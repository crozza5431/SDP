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
 * @author DeSHUN CHECK
 */
public class database {
    public static Connection conn;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        

        login();
    }
    
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
    
    public static void login() throws SQLException {
        Scanner in = new Scanner(System.in);
        String uName;
        String uPass;
        
        System.out.println("Enter Username: ");
        uName = in.nextLine();
        
        System.out.println("Enter Password: ");
        uPass = in.nextLine();
        
        if (checkUser(uName)) {
            int count = 0;
            while (!checkPassword(uName, uPass)) {
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
    
    public static Boolean checkUser(String uName) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT username FROM users WHERE username='" + uName + "'");
        r.last();
        int count = r.getRow();
        return (count != 0);
    }
    
    public static Boolean checkPassword(String uName, String uPass) throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT username, password FROM users WHERE username='" + uName + "' AND password='" + uPass + "'");
        return (r.next());
    }
    
    public static String [] getPasswordSalt(String uName) throws SQLException {
        String arrPasSalt[] = null;
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT password, salt FROM users WHERE username='" + uName + "'");
        r.next();
        arrPasSalt[0] = r.getString("password");
        arrPasSalt[1] = r.getString("salt");
        //make sure only one row returned
        return arrPasSalt;
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
        
        while (checkUser(uName)) {
            System.out.println("Username Taken, Please Enter Another Username: ");
            uName = in.nextLine();
        }
        System.out.println("Enter Password: ");
        uPass = in.nextLine();
        
        System.out.println("Enter Hint: ");
        String uHint = in.nextLine();
        
        int uID = nextID() + 1;
        
        Statement s = conn.createStatement();
        
        s.executeUpdate("INSERT INTO Users VALUES ('" + uID + "', '" + uName + "', '" + uPass + "', '" + uHint + "')");

    }
    
    public static int nextID() throws SQLException {
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet r = s.executeQuery("SELECT ID FROM Users");
        r.last();
        return r.getInt("ID");
    }
    
    
}
