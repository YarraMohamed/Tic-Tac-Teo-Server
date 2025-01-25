package Controllers;


import org.json.JSONObject;

public class RequestRouter {
   
    public static String routeRequest(String request, GameClientHandler gameClient) {
        
        System.out.println("request in serverHandler "+request);
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
                String req=requestHandler.signInHandle(username, password,gameClient);
                JSONObject reqJSONObject = new JSONObject(req);
                gameClient.setId(reqJSONObject.getInt("Player_ID"));
                return req; 
            case "SIGN_UP":
                return requestHandler.signUpHandle(username, email ,password,gameClient) ;
            case "SIGN_OUT":
                return requestHandler.signOutHandle(playerID);
            case "GET_AVAILABLE_PLAYERS":
                return requestHandler.getAvailablePlayersHandle(playerID);
               // case "GET_ONLINE_PLAYERS":
                //return requestHandler.getOnlinePlayersHandle(playerID);
            case "USER_NAME":
                return requestHandler.userNameHandle(playerID);
            case "GAME_REQUEST":
//                int requestingPlayer_ID = jsonReceived.optInt("requestingPlayer_ID");
//                int requestedPlayer_ID = jsonReceived.optInt("requestedPlayer_ID");
                System.out.println("gone here");
                return requestHandler.handleGameRequest(jsonReceived);
            case "MOVE":
                return requestHandler.inGameHandle( playerID, player2ID, btnId);
            default:
                return "Error: Invalid request type.";
        }
    }
    
}
