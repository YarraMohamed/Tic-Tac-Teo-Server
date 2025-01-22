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
                gameClient.setUserID(playerID);
                return requestHandler.signInHandle(username, password); 
            case "SIGN_UP":
                return requestHandler.signUpHandle(username, email ,password) ;
            case "SIGN_OUT":
                return requestHandler.signOutHandle(playerID);
            case "MOVE":
                return requestHandler.inGameHandle( playerID, player2ID, btnId);
            default:
                return "Error: Invalid request type.";
        }
    }
    
}
