package Controllers;

public class RequestRouter {
    
    public static String routeRequest(String request) {
        String[] parts = request.split(":");
        String requestType = parts[0];

        switch (requestType) {
            case "signin":
                return new RequestHandler().signInHandle(parts[1], parts[2]); 
            default:
                return "Error: Invalid request type.";
        }
    }
    
}
