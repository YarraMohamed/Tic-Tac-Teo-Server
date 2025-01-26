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
    
     public String signInHandle(String name, String password, GameClientHandler gameClientHandler){
        try {
            
            Player player = new Player(name,password);
            
            String result = PlayerDAO.signIn(player);
            
            JSONObject signInJsonResponse = new JSONObject(result);
            String signInResponse = signInJsonResponse.optString("response"); // opt or get?
            
            if (signInResponse.equals("LOGGED_IN")) {
                int playerId = signInJsonResponse.optInt("Player_ID");
                gameClientHandler.mapPlayerIdToClient(playerId);
                //System.out.println(GameClientHandler.clientMap);
            }
            
            return result ;
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    }
    
    public String signUpHandle(String name, String email,String password,GameClientHandler gameClientHandler){
        try {
            
            Player player = new Player(name,email,password);
            
            String result = PlayerDAO.signUp(player);
            
            JSONObject signInJsonResponse = new JSONObject(result);
            String signInResponse = signInJsonResponse.optString("response"); // opt or get?
            
            if (signInResponse.equals("LOGGED_IN")) {
                int playerId = signInJsonResponse.optInt("Player_ID");
                gameClientHandler.mapPlayerIdToClient(playerId);
            }
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

    public String userNameHandle(int playerID){
        try {
           
            String result = PlayerDAO.userName(playerID);
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
//        GameClientHandler p2=GameClientHandler.getGameClient(player2Id);
        GameClientHandler p2=GameClientHandler.getClientHandler(player2Id);

//        GameClientHandler p2=GameClientHandler.getGameClient2(11);
        
        
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
            responseJson.put("response", "MOVED");
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
    public String handleGameRequest(JSONObject jsonReceived) {

        JSONObject handlingGameRequestResponse = new JSONObject();
        handlingGameRequestResponse.put("response", "GAME_REQUEST_SUCCESS");

        int requestingPlayerId = jsonReceived.getInt("requestingPlayer_ID");
        int requestedPlayerId = jsonReceived.getInt("requestedPlayer_ID");
        System.out.println("Requested Player ID: " + requestedPlayerId); // log message

        String requestingPlayerUsername = PlayerDAO.getPlayerUsernameById(requestingPlayerId);
        GameClientHandler requestedPlayer = GameClientHandler.getClientHandler(requestedPlayerId);

        if (requestedPlayer != null) {
           PrintStream stream = requestedPlayer.getStream(requestedPlayer);
           String message = requestedPlayer.sendGameRequest(requestingPlayerId, requestingPlayerUsername,requestedPlayerId);
           stream.println(message);
           return handlingGameRequestResponse.toString();
        } else {
            handlingGameRequestResponse.put("response", "GAME_REQUEST_FAILED");
            return handlingGameRequestResponse.toString();
        }
       
    }
    public String updateScore(int playerID, int score) {
        try {            
            String result = PlayerDAO.updateScore(playerID, score);
            return result ;
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    
    }
    public String handleRejection(JSONObject jsonReceived) {

        JSONObject handlingGameRequestResponse = new JSONObject();
        handlingGameRequestResponse.put("requestType", "rejectedNotification");
        int requestedPlayerId = jsonReceived.getInt("requestedPlayer_ID");
        System.out.println("in handleReject playerID is "+requestedPlayerId);
        GameClientHandler requestedPlayer = GameClientHandler.getClientHandler(requestedPlayerId);
        System.out.println("in handleReject requestedPlayer "+requestedPlayer);

        if (requestedPlayer != null) {
           PrintStream stream = requestedPlayer.getStream(requestedPlayer);
           System.out.println("in handleReject stream "+stream);
           stream.println(handlingGameRequestResponse.toString());
           return handlingGameRequestResponse.toString();
        } else {
            handlingGameRequestResponse.put("response", "GAME_REQUEST_FAILED");
            return handlingGameRequestResponse.toString();
        }
       
    }
    
    
    public String handleAcceptiance(JSONObject jsonReceived) {

        JSONObject handlingGameRequestResponse = new JSONObject();
        handlingGameRequestResponse.put("requestType", "ACCEPTED");
        int senderId = jsonReceived.getInt("senderID");
        int reciverId=jsonReceived.getInt("reciverID");
        handlingGameRequestResponse.put("p2ID", senderId);

//        System.out.println("in handleReject playerID is "+requestedPlayerId);
        GameClientHandler requestedPlayer = GameClientHandler.getClientHandler(reciverId);
        System.out.println("in handleReject requestedPlayer "+requestedPlayer);

        if (requestedPlayer != null) {
            requestedPlayer.sendRequest(handlingGameRequestResponse.toString());
           return "DONE";
        } else {
            handlingGameRequestResponse.put("response", "GAME_REQUEST_FAILED");
            return "PLAYER_OFF";
        }
       
    }

}
