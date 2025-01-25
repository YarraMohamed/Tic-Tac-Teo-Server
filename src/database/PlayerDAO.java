package database;

import Controllers.StopServerAndStatisticsController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlayerDAO {
    
    private static Connection con ;
    
    public static String signUp(Player player) throws SQLException {
        
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
        json.put("response", "LOGGED_IN");
        json.put("Player_ID", generatedID);
      }else{
         json.put("response", "Failed");
      }
      
      StopServerAndStatisticsController.notifyBarChart(); // Update bar chart when user signs up
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
             json.put("response", "LOGGED_IN");
             json.put("Player_ID", playerID);
        } else {
            json.put("response", "Failed");
        }
        
        StopServerAndStatisticsController.notifyBarChart(); // Update bar chart when user signs in
        return json.toString();
    }
    
    public static String signOut(int playerID) throws SQLException{
        
        JSONObject json = new JSONObject();
        
        con = DatabaseConnection.getDBConnection();
        
        PreparedStatement insertStatus = con.prepareStatement(
           "UPDATE PLAYERSTATUS SET ACTIVE=FALSE, BUSY=FALSE WHERE PLAYER_ID=?"
        );
        insertStatus.setInt(1, playerID);
        
        int result = insertStatus.executeUpdate();
        if(result > 0){
             json.put("response", "Success");
        } else {
            json.put("response", "Failed");
        }
        StopServerAndStatisticsController.notifyBarChart(); // Update bar chart when user signs out
        return json.toString();
    }
    
    public static String userName(int playerID) throws SQLException{
        
        JSONObject json = new JSONObject();
        
        con = DatabaseConnection.getDBConnection();
        
        PreparedStatement insertStatus = con.prepareStatement(
           "SELECT NAME,SCORE FROM PLAYER WHERE ID=?"
        );
        insertStatus.setInt(1, playerID);
        
        ResultSet result = insertStatus.executeQuery();
        
        if(result.next()){
             json.put("response", "Profile");
             json.put("Name",result.getString("NAME"));
             json.put("Score",result.getInt("SCORE"));
        } else {
            json.put("response", "Failed");
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
    
 public static String getPlayersListExcludingCurrent(int currentPlayerID) throws SQLException {
    JSONObject json = new JSONObject();
    JSONArray playersArray = new JSONArray();

    // Get database connection
    con = DatabaseConnection.getDBConnection();

    // Query: Join PLAYERSTATUS with PLAYER table to get player names and only active players
    String getPlayersQuery = "SELECT p.ID, p.NAME FROM PLAYERSTATUS ps " +
                             "JOIN PLAYER p ON ps.PLAYER_ID = p.ID " +
                             "WHERE ps.ACTIVE = TRUE AND p.ID != ?";  // Exclude current player
    
    // Prepare statement and execute the query
    PreparedStatement preparedStatement = con.prepareStatement(getPlayersQuery);
    preparedStatement.setInt(1, currentPlayerID); // Exclude current player
    ResultSet resultSet = preparedStatement.executeQuery();

    // Iterate over the result set and add players to the JSON array
    while (resultSet.next()) {
        JSONObject playerJson = new JSONObject();
        playerJson.put("ID", resultSet.getInt("ID"));
        playerJson.put("NAME", resultSet.getString("NAME"));
        playersArray.put(playerJson);
    }

    // Put players array into the response JSON
    json.put("response", "List_Of_Players");
    json.put("players", playersArray);

    // Return the response as a string
    return json.toString();
}
 
 public static String getPlayerUsernameById(int playerId) {

        String playerUsername = ""; 
        String query = "SELECT NAME FROM PLAYER WHERE ID = ? ";

        try {
            con = DatabaseConnection.getDBConnection();
            try (PreparedStatement preparedStatement = con.prepareStatement(query)){
                preparedStatement.setInt(1, playerId);   
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            playerUsername = resultSet.getString("NAME");
                    } 
                }
            } 
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Eroor while getting uername by id.");
        }
 
        return playerUsername;
    }
    
//    public static List<Player> getOnlinePlayers(int currentPlayerID) {
//    List<Player> onlinePlayers = new ArrayList<>();
//    
//    try (Connection conn = DatabaseConnection.getDBConnection()) {
//        String query = "SELECT ID, NAME FROM PLAYERSTATUS WHERE ACTIVE = 1 AND ID != ?"
//;
//        PreparedStatement stmt = conn.prepareStatement(query);
//        stmt.setInt(1, currentPlayerID);
//        
//        ResultSet rs = stmt.executeQuery();
//        
//        while (rs.next()) {
//            onlinePlayers.add(new Player(rs.getString("ID"), rs.getString("NAME")));
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//    
//    return onlinePlayers;
//}
    
}
      
