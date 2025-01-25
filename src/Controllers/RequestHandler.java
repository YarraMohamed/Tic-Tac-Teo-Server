package Controllers;

import database.Player;
import database.PlayerDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class RequestHandler {
    
    public String signInHandle(String name, String password, GameClientHandler gameClientHandler){
        try {
            
            Player player = new Player(name,password);
            
            String result = PlayerDAO.signIn(player);
            
            JSONObject signInJsonResponse = new JSONObject(result);
            String signInResponse = signInJsonResponse.optString("response"); 
            
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
    
    public String signUpHandle(String name, String email,String password, GameClientHandler gameClientHandler){
        try {
            
            Player player = new Player(name,email,password);
            
            String result = PlayerDAO.signUp(player);
            
            JSONObject signInJsonResponse = new JSONObject(result);
            String signInResponse = signInJsonResponse.optString("response"); 
            
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
    
    
    
    
    public String userNameHandle(int playerID){
        try {
           
            String result = PlayerDAO.userName(playerID);
            return result ;
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    }
            
    public String handleGameRequest(JSONObject jsonReceived) {
        
        //GameClientHandler.printVector();
        
        JSONObject handlingGameRequestResponse = new JSONObject();
        handlingGameRequestResponse.put("response", "GAME_REQUEST_SUCCESS");

        int requestingPlayerId = jsonReceived.getInt("requestingPlayer_ID");
        int requestedPlayerId = jsonReceived.getInt("requestedPlayer_ID");
        
        System.out.println("Requesting Player ID: " + requestingPlayerId); // log message
        System.out.println("Requested Player ID: " + requestedPlayerId); // log message

        
        String requestingPlayerUsername = PlayerDAO.getPlayerUsernameById(requestingPlayerId);
        GameClientHandler requestedPlayer = GameClientHandler.getGameClient(requestedPlayerId);
        
        System.out.println("Received JSON in GAME_REQUEST: " + jsonReceived.toString()); // log message

        if (requestedPlayer != null) {
           return requestedPlayer.sendGameRequest(requestingPlayerId, requestingPlayerUsername);
           //return handlingGameRequestResponse.toString();
           
        } else {
            return "GAME_REQUEST_FAILED";
        }
    }
    
    
    /*public String handleGameReject (JSONObject jsonReceived) {
        
        System.out.println("Received JSON in GAME_REJECTED: " + jsonReceived); // log messages
        
        JSONObject handlingGameRejectResponse = new JSONObject();
        handlingGameRejectResponse.put("response", "GAME_REJECT_SUCCESS");
        
        
        int rejectingPlayerId = jsonReceived.getInt("rejectingPlayer_ID");
        int rejectedPlayerId = jsonReceived.getInt("rejectedPlayer_ID");
        
        
        System.out.println("Rejecting Player ID: " + rejectingPlayerId); // log message
        System.out.println("Rejected Player ID: " + rejectedPlayerId); // log message
        
        String rejectingPlayerUsername = PlayerDAO.getPlayerUsernameById(rejectingPlayerId);

        GameClientHandler rejectedPlayer = GameClientHandler.getGameClient(rejectedPlayerId);
    
        System.out.println("Received JSON in GAME_REJECTED: " + jsonReceived.toString()); // log message

        if (rejectedPlayer != null) {
           return rejectedPlayer.sendGameReject(rejectingPlayerId, rejectingPlayerUsername);
        } else {
            return "GAME_REJECTED_FAILED";
        }
    }*/
    
    
    public String handleGameReject (JSONObject jsonReceived) {
        
        System.out.println("Received JSON in GAME_REJECTED: " + jsonReceived); // log messages
        
        JSONObject handlingGameRejectResponse = new JSONObject();
        handlingGameRejectResponse.put("response", "GAME_REJECT_SUCCESS");
        
        
        //int rejectingPlayerId = jsonReceived.getInt("rejectingPlayer_ID");
        int rejectedPlayerId = jsonReceived.getInt("rejectedPlayer_ID");
        
        
        //System.out.println("Rejecting Player ID: " + rejectingPlayerId); // log message
        System.out.println("Rejected Player ID: " + rejectedPlayerId); // log message
        
        //String rejectingPlayerUsername = PlayerDAO.getPlayerUsernameById(rejectingPlayerId);

        GameClientHandler rejectedPlayer = GameClientHandler.getGameClient(rejectedPlayerId);
    
        System.out.println("Received JSON in GAME_REJECTED: " + jsonReceived.toString()); // log message

        if (rejectedPlayer != null) {
           return rejectedPlayer.sendGameReject();
        } else {
            return "GAME_REJECTED_FAILED";
        }
    }
    
}
