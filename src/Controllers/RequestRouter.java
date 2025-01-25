package Controllers;

import org.json.JSONObject;

public class RequestRouter {
   
    public static String routeRequest(String request, GameClientHandler gameClient) {
        
        JSONObject jsonReceived = new JSONObject(request);
        String requestType = jsonReceived.getString("requestType");

        String username = jsonReceived.optString("username");
        String password = jsonReceived.optString("password");
        String email = jsonReceived.optString("email");
        int playerID = jsonReceived.optInt("Player_ID");

         RequestHandler requestHandler = new RequestHandler();
      
        switch (requestType) {
            case "SIGN_IN":
                return requestHandler.signInHandle(username, password, gameClient); 
            case "SIGN_UP":
                return requestHandler.signUpHandle(username, email ,password, gameClient) ;
            case "SIGN_OUT":
                return requestHandler.signOutHandle(playerID);
            case "GET_AVAILABLE_PLAYERS":
                return requestHandler.getAvailablePlayersHandle(playerID);    
            case "USER_NAME":
                 return requestHandler.userNameHandle(playerID);  
            case "GAME_REQUEST":
                return requestHandler.handleGameRequest(jsonReceived);
            default:
                return "Error: Invalid request type.";
        }
    }
    
}
