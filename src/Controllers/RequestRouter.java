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
      
        switch (requestType) {
            case "SIGN_IN":
                return new RequestHandler().signInHandle(username, password); 
            case "SIGN_UP":
                return new RequestHandler().signUpHandle(username, email ,password) ;
            case "SIGN_OUT":
                return new RequestHandler().signOutHandle(playerID);
            default:
                return "Error: Invalid request type.";
        }
    }
    
}
