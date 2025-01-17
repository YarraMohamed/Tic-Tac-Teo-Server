package Controllers;

public class RequestRouter {
    
    public static String routeRequest(String request, GameClientHandler gameClient) {
        String[] parts = request.split(":");
        String requestType = parts[0];
        switch (requestType) {
            case "signin":
                return new RequestHandler().signInHandle(parts[1], parts[2]); 
            case "signup":
                return new RequestHandler().signUpHandle(parts[1], parts[2], parts[3]) ;
            default:
                return "Error: Invalid request type.";
        }
    }
    
}
