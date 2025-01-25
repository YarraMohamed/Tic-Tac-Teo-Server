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
                System.out.println("Received JSON for SIGN_IN: " + jsonReceived); // log message
                return requestHandler.signInHandle(username, password, gameClient); 
            case "SIGN_UP":
                System.out.println("Received JSON for SIGN_UP: " + jsonReceived); // log message
                return requestHandler.signUpHandle(username, email ,password, gameClient) ;
            case "SIGN_OUT":
                System.out.println("Received JSON for SIGN_OUT: " + jsonReceived); // log message
                return requestHandler.signOutHandle(playerID);
            case "GET_AVAILABLE_PLAYERS":
                return requestHandler.getAvailablePlayersHandle(playerID);    
            case "USER_NAME":
                 return requestHandler.userNameHandle(playerID);  
            case "GAME_REQUEST":
                System.out.println("JSON received in GAME_REQUEST case: " + jsonReceived); // log message
                return requestHandler.handleGameRequest(jsonReceived);
            case "GAME_REJECTED":   
                System.out.println("JSON received in GAME_REJECTED case: " + jsonReceived); // log message
                return requestHandler.handleGameReject(jsonReceived);
                //return requestHandler.handleGameReject(rejectedPlayerId, rejectedPlayerId);
            default:
                return "Error: Invalid request type.";
        }
    }
    
}
