package database;

import org.apache.derby.jdbc.ClientDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    
    private static Connection con = null;
    
    private DatabaseConnection(){  
    }
    
    public static Connection getDBConnection(){
        if(con==null){
             try {
            DriverManager.registerDriver(new ClientDriver());
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacTeo","root","root");
            System.out.println("DB Started");
        } catch (SQLException ex) {
            System.out.println("Error connecting to database");
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        return con;
    }
    
    public static void closeDBConnection(){
        if(con!=null){
            try {
                con.close();
                con=null;
                System.out.println("DB Closed");
            } catch (SQLException ex) {
                System.out.println("Error Closing DB");
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
