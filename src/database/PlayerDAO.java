package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;

public class PlayerDAO {
    
    private static Connection con ;
    
    public static String signUp(Player player) throws SQLException{
        
        JSONObject json = new JSONObject();
  
        con = DatabaseConnection.getDBConnection();
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
        json.put("response", "Success");
        json.put("Player_ID", generatedID);
      }else{
         json.put("response", "Failed");
      }
      
      return json.toString();
      
    }
    
    public static String signIn(Player player) throws SQLException{
        
        JSONObject json = new JSONObject();
        
        con = DatabaseConnection.getDBConnection();
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
            json.put("response", "Failed");
        }
        
        PreparedStatement insertStatus = con.prepareStatement(
           "UPDATE PLAYERSTATUS SET ACTIVE=TRUE, BUSY=FALSE WHERE PLAYER_ID=?"
        );
        insertStatus.setInt(1, playerID);
        
        int result = insertStatus.executeUpdate();
        if(result > 0){
             json.put("response", "Success");
             json.put("Player_ID", playerID);
        }
        return json.toString();
    }
    
    
    // Method to get the number of players who are currently active (online)
    public static int getNumberOfOnlinePlayers() throws SQLException {
        int numberOfOnlinePlayers = 0;
        // Use an alias "ACTIVE_COUNT" for the column the query returns
        String countOnlinePlayersQuery = "SELECT COUNT (*) AS ACTIVE_COUNT FROM PLAYERSTATUS WHERE ACTIVE=TRUE";
        
        con = DatabaseConnection.getDBConnection();
        // Use try-with-resources block to prevent resource leaks and avoid calling close() explicitly
        try (PreparedStatement preparedStatement = con.prepareStatement(countOnlinePlayersQuery);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                numberOfOnlinePlayers = resultSet.getInt("ACTIVE_COUNT");
            }
        }
 
        return numberOfOnlinePlayers;
    }
    
    
    // Method to get the number of players who are currently inactive (offline)
    public static int getNumberOfOfflinePlayers() throws SQLException {
        int numberOfOfflinePlayers = 0;
        String countOfflinePlayersQuery = "SELECT COUNT (*) AS ACTIVE_COUNT FROM PLAYERSTATUS WHERE ACTIVE=FALSE";
        
        con = DatabaseConnection.getDBConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement(countOfflinePlayersQuery);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                numberOfOfflinePlayers = resultSet.getInt("ACTIVE_COUNT");
            }
        }
        
        return numberOfOfflinePlayers;
    }
}
      

