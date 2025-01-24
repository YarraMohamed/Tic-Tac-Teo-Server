package Controllers;

import database.Player;
import database.PlayerDAO;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class RequestHandler {
    
    public String signInHandle(String name, String password){
        try {
            
            Player player = new Player(name,password);
            
            String result = PlayerDAO.signIn(player);
            return result ;
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    }
    
    public String signUpHandle(String name, String email,String password){
        try {
            
            Player player = new Player(name,email,password);
            
            String result = PlayerDAO.signUp(player);
            return result ;
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    }
    
    public String signOutHandle(int playerID){
        try {
           
            String result = PlayerDAO.signOut(playerID);
            return result ;
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    }
    /*
    public String inGameHandle(int playerID, int player2Id, String btnId) {
        JSONObject responseJson = new JSONObject(); // Response JSON to be returned

        try {
            PrintStream p2Ear = GameClientHandler.getClientEar(player2Id); // Get PrintStream for player2Id

            if (p2Ear != null) {
                // Create the JSON object to send to Player 2
                JSONObject moveJson = new JSONObject();
                moveJson.put("requestType", "MOVE");
                moveJson.put("Player_ID", playerID);
                moveJson.put("Player2_ID", player2Id);
                moveJson.put("btn", btnId);

                // Send move JSON to Player 2
                p2Ear.println(moveJson.toString());
                p2Ear.flush();

                // Construct success response
                responseJson.put("response", "Success");
                responseJson.put("message", "Move sent successfully to Player 2.");
            } else {
                // Player 2 is not connected
                responseJson.put("response", "Error");
                responseJson.put("message", "Player 2 is not connected.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Construct error response for exceptions
            responseJson.put("response", "Error");
            responseJson.put("message", "An error occurred while processing the move.");
        }

        return responseJson.toString(); // Return JSON response as a string
    }*/
    public String updateScore(int id, int score){
        try {            
            String result = PlayerDAO.updateScore(id, score);
            return result ;
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    }
    public String inGameHandle(int playerID, int player2Id, String btnId) {
    JSONObject responseJson = new JSONObject(); // Response JSON to be returned

    try {
        // Validate button ID
        if (btnId == null || btnId.isEmpty()) {
            responseJson.put("response", "Error");
            responseJson.put("message", "Invalid button ID.");
            return responseJson.toString();
        }

        // Get PrintStream for Player 2
//        PrintStream p2Ear = GameClientHandler.getClientEar(player2Id);
        GameClientHandler p2=GameClientHandler.getClientById(player2Id);
        
        if (p2 != null) {
            // Create the JSON object to send to Player 2
            JSONObject moveJson = new JSONObject();
            moveJson.put("requestType", "MOVE");
            moveJson.put("Player_ID", playerID);
            moveJson.put("Player2_ID", player2Id);
            moveJson.put("btn", btnId);

            // Send move JSON to Player 2
            System.out.println("Sending move from Player " + playerID + " to Player " + player2Id);
            p2.sendRequest(moveJson.toString());
//            p2.sendRequest(moveJson.toString());

//            p2.flush();
            System.out.println("move sent");

            // Construct success response
            responseJson.put("response", "Success");
            responseJson.put("message", "Move sent successfully to Player 2.");
        } else {
            // Player 2 is not connected
            responseJson.put("response", "Error");
            responseJson.put("message", "Player 2 is not connected.");
            System.err.println("Error: Player " + player2Id + " is not connected.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        // Construct error response for exceptions
        responseJson.put("response", "Error");
        responseJson.put("message", "An error occurred while processing the move: " + e.getMessage());
    }

    return responseJson.toString();  // Return JSON response as a string
//    return "ignore";  // Return JSON response as a string

}




    

    public String getAvailablePlayersHandle(int currentPlayerID) {
        try {
            // Call DAO method to fetch the list of players excluding the current player
            String result = PlayerDAO.getPlayersListExcludingCurrent(currentPlayerID);
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }
    }
   
    public String userNameHandle(int playerID){
        try {
           
            String result = PlayerDAO.userName(playerID);
            return result ;
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    }

//public String getOnlinePlayersHandle(int currentPlayerID) {
//    // Fetch only online players except the current player
//    List<Player> onlinePlayers = PlayerDAO.getOnlinePlayers(currentPlayerID);
//
//    // Prepare the response JSON
//    JSONObject response = new JSONObject();
//    JSONArray playersArray = new JSONArray();
//
//    for (Player player : onlinePlayers) {
//        JSONObject playerObj = new JSONObject();
//        playerObj.put("NAME", player.getName());
//        playersArray.put(playerObj);
//    }
//
//    response.put("onlinePlayers", playersArray);
//    return response.toString();
//}
}
