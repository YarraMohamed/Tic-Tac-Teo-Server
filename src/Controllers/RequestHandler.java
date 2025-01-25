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
        
        JSONObject handlingGameRequestResponse = new JSONObject();
        handlingGameRequestResponse.put("response", "GAME_REQUEST_SUCCESS");

        int requestingPlayerId = jsonReceived.getInt("requestingPlayer_ID");
        int requestedPlayerId = jsonReceived.getInt("requestedPlayer_ID");
        
        String requestingPlayerUsername = PlayerDAO.getPlayerUsernameById(requestingPlayerId);
        GameClientHandler requestedPlayer = GameClientHandler.getGameClient(requestedPlayerId);
        
        if (requestedPlayer != null) {
           return requestedPlayer.sendGameRequest(requestingPlayerId, requestingPlayerUsername);
           
        } else {
            return "GAME_REQUEST_FAILED";
        }
    }
    
}
