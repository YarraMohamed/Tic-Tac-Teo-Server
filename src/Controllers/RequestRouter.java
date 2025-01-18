package Controllers;


import org.json.JSONObject;

public class RequestRouter {
    
    public static String routeRequest(String request, GameClientHandler gameClient) {
        
        JSONObject jsonReceived = new JSONObject(request);
        String requestType = jsonReceived.getString("requestType");
        System.out.println(requestType);

        String username = jsonReceived.optString("username");
        String password = jsonReceived.optString("password");
        String email = jsonReceived.optString("email");
        System.out.println(username);
        System.out.println(password);
        System.out.println(email);
      
        switch (requestType) {
            case "SIGN_IN":
                return new RequestHandler().signInHandle(username, password); 
            case "SIGN_UP":
                return new RequestHandler().signUpHandle(username, email ,password) ;     
            default:
                return "Error: Invalid request type.";
        }
    }
    
}
