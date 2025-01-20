package Controllers;

import database.Player;
import database.PlayerDAO;
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
}