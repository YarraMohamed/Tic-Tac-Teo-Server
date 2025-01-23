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
        int player2ID= jsonReceived.optInt("Player2_ID");
        String btnId=jsonReceived.optString("btn");
        
        
         RequestHandler requestHandler = new RequestHandler();
      
        switch (requestType) {
            case "SIGN_IN":
                String req=requestHandler.signInHandle(username, password);
                JSONObject reqJSONObject = new JSONObject(req);
                gameClient.setUserID(reqJSONObject.getInt("Player_ID"));
                return req; 
            case "SIGN_UP":
                return requestHandler.signUpHandle(username, email ,password) ;
            case "SIGN_OUT":
                return requestHandler.signOutHandle(playerID);
            case "MOVE":
                return requestHandler.inGameHandle( playerID, player2ID, btnId);
            case "USER_NAME":
                return requestHandler.userNameHandle(playerID);
            case "GET_AVAILABLE_PLAYERS":
                return requestHandler.getAvailablePlayersHandle(playerID);
               // case "GET_ONLINE_PLAYERS":
                //return requestHandler.getOnlinePlayersHandle(playerID);
            default:
                return "Error: Invalid request type.";
        }
    }
    
}
