package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO {
    
    private static Connection con ;
    
    
    public static boolean signUp(Player player) throws SQLException{
        
        con = DatabaseConnection.getDBConnection();
        boolean finalResult = false;
        int generatedID = 0;
        
        PreparedStatement insertPlayer = con.prepareStatement(
          "INSERT INTO PLAYER(NAME,EMAIL,PASSWORD,SCORE) VALUES(?,?,?,0)",
           PreparedStatement.RETURN_GENERATED_KEYS
        );
        
        insertPlayer.setString(1, player.getName());
        insertPlayer.setString(2, player.getEmail());
        insertPlayer.setString(3, player.getPassword());
    
       int result1 = insertPlayer.executeUpdate();
       
       ResultSet keys = insertPlayer.getGeneratedKeys();
       if (keys.next()) {
         generatedID = keys.getInt(1);
        player.setId(generatedID);    
       }
       
      PreparedStatement insertStatus = con.prepareStatement(
        "INSERT INTO PLAYERSTATUS(PLAYER_ID,ACTIVE,BUSY) VALUES (?,TRUE,FALSE)"
      );
      
      insertStatus.setInt(1, generatedID);
      int result2 = insertStatus.executeUpdate();

      if (result1 > 0 && result2 > 0) {
         finalResult = true;
      }
      return finalResult;
      
    }
    
    public static boolean signIn(Player player) throws SQLException{
        
        con = DatabaseConnection.getDBConnection();
        boolean finalResult=false;
        int playerID=0;
        
        PreparedStatement getPlayer = con.prepareStatement(
            "SELECT * FROM PLAYER WHERE NAME=? AND PASSWORD=?"
        );
        
        getPlayer.setString(1,player.getName());
        getPlayer.setString(2, player.getPassword());
        
        ResultSet set = getPlayer.executeQuery();
        
        if(set.next()){
            playerID = set.getInt("ID");
            player.setId(playerID);
        } else {
            return finalResult;
        }
        
        PreparedStatement insertStatus = con.prepareStatement(
           "UPDATE PLAYERSTATUS SET ACTIVE=TRUE, BUSY=FALSE WHERE PLAYER_ID=?"
        );
        insertStatus.setInt(1, playerID);
        
        int result = insertStatus.executeUpdate();
        if(result > 0){
            finalResult = true;
        }
        return finalResult;
    }
}
      

