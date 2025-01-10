package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO {
    
    private static Connection con =DatabaseConnection.getDBConnection() ;
    
    public static boolean signUp(Player player) throws SQLException{
        
        boolean finalResult=false;
        
        PreparedStatement insertPlayer = con.prepareStatement("INSERT INTO PLAYER(NAME,EMAIL,PASSWORD,SCORE) VALUES(?,?,?,0)");
        insertPlayer.setString(1, player.getName());
        insertPlayer.setString(2, player.getEmail());
        insertPlayer.setString(3, player.getPassword());
        
        PreparedStatement insertStatus = con.prepareStatement("INSERT INTO PLAYERSTATUS(NAME,ACTIVE,BUSY) VALUES (?,TRUE,FALSE)");
        insertStatus.setString(1, player.getName());
        
        int result1 = insertPlayer.executeUpdate();
        int result2 = insertStatus.executeUpdate();
        
        if(result1 > 0 && result2 > 0){
            finalResult=true;
        }
        return finalResult;
    }
    
    public static boolean signIn(Player player) throws SQLException{
        
        boolean finalResult=false;
        
        PreparedStatement getPlayer = con.prepareStatement("SELECT * FROM PLAYER WHERE Name=? AND PASSWORD=?");
        getPlayer.setString(1, player.getName());
        getPlayer.setString(2, player.getPassword());
        
        ResultSet set = getPlayer.executeQuery();
        
        if(set.next()){
            PreparedStatement insertStatus = con.prepareStatement("UPDATE INTO PLAYERSTATUS(NAME,ACTIVE,BUSY) VALUES (?,TRUE,FALSE)");
            insertStatus.setString(1, player.getName());
            int result = insertStatus.executeUpdate();
            if(result > 0){
                finalResult=true;
            }
        }
        return finalResult; 
   }
}
