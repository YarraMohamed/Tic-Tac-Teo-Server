package Controllers;

import database.Player;
import database.PlayerDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;


public class GameClientHandler extends Thread {
    
    private Socket gameClientSocket;
    public static Vector<GameClientHandler> gameClientsVector = new Vector<>();
    private BufferedReader bufferedReader;
    private PrintStream printStream;
    private int playerId;
    private int playerId2;
    static Map<Integer, GameClientHandler> clientMap = new ConcurrentHashMap<>(); // concurrent map is better in multi-threading applications
    
    
    public GameClientHandler(Socket gameClientSocket) {
        this.gameClientSocket = gameClientSocket;
        initializeStreams();
        System.out.println("constructoor => "+this.getName());
    }
    

    @Override
    public void run() {
        handleClient();
    }
    
    
    private void initializeStreams() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(gameClientSocket.getInputStream()));
            printStream = new PrintStream(gameClientSocket.getOutputStream());
            GameClientHandler.gameClientsVector.add(this); 
            //System.out.println("Added to gameClientsVector: " + this); // log message
            start(); 
        } catch (IOException e) {
          e.printStackTrace();
          System.out.println("Error initializing streams for client");
        }
    }
    

    
    private void handleClient() {
    try {
        String message;
        while (!gameClientSocket.isClosed()) {
            try {
                message = bufferedReader.readLine();
                if (message == null) {
                    if (gameClientSocket.isClosed()) break;
                    continue;
                }

                System.out.println("Received message: " + message);

                // Specific handling for MOVE and GAME_REQUEST
                JSONObject jsonMessage = new JSONObject(message);
                String requestType = jsonMessage.getString("requestType");

                if ("MOVE".equals(requestType) || "GAME_REQUEST".equals(requestType)) {
                    System.out.println("Broadcasting " + requestType + " message");
                    sendToAllPlayers(message);
                }

                // Process other request types
                String response = RequestRouter.routeRequest(message, this);
                System.out.println("Response: " + response);
                printStream.println(response);
                printStream.flush();

                // Check for sign out
                if ("SIGN_OUT".equals(requestType)) {
                    printStream.println("Loged OUT");
                    printStream.flush();
                    break;
                }
            } catch (IOException e) {
                System.out.println("Socket read error: " + e.getMessage());
                break;
            }
        }
    } catch (Exception e) {
        System.out.println("Unexpected error in handleClient: " + e.getMessage());
    } finally {
        closeResources();
        GameClientHandler.gameClientsVector.remove(this);
        if (playerId > 0) {
            GameClientHandler.clientMap.remove(playerId);
        }
        System.out.println("Client disconnected.");
    }
}
    public void sendToAllPlayers(String message) {
    try {
        System.out.println("Sending to all players. Total clients: " + clientMap.size());
//        for (Map.Entry<Integer, GameClientHandler> entry : clientMap.entrySet()) {
//                    System.out.println("map => "+ entry.getValue().getName());
//
//            try {
//                GameClientHandler client = entry.getValue();
//                if (client != null && client.printStream != null) {
//                    System.out.println("Sending to Player ID: " + entry.getKey());
//                    client.printStream.println(message);
//                    client.printStream.flush();
//                }
//            } catch (Exception clientEx) {
//                System.err.println("Error sending to individual client: " + clientEx.getMessage());
//            }
//        }
        for (GameClientHandler clinet:gameClientsVector) {
            System.out.println("Vectoooor clinet => "+clinet.getName());
            clinet.printStream.println(message);
            clinet.printStream.flush();
        }
    } catch (Exception e) {
        System.err.println("Global error in sendToAllPlayers: " + e.getMessage());
    }
}
    public void setId(int id){
        playerId2=id;
    }
    public void mapPlayerIdToClient(int playerId){
        this.playerId = playerId;
        clientMap.put(playerId, this);
//        gameClientsVector.add(this);
        System.out.println("Registered client with player ID: " + playerId); // log message
    }
    
    
    public static GameClientHandler getGameClient(int playerId) {
        for (GameClientHandler client: gameClientsVector) {
            if (client.playerId == playerId) {
                System.out.println("found client "+client.playerId+"    "+client.playerId2);
                return client;
            }
        }
        return null; // should I really return null? Doesn't the concurrent map not accept nulls?
    }
    public static GameClientHandler getClientHandler(int playerId){
//        clientMap.forEach((key,value)->{
//            if (key==playerId) {
//                System.out.println("Found ID");
//                client=value;
//                return;
//            }
//        });
        
        return clientMap.get(playerId);
    }
    public static GameClientHandler getGameClient2(int playerId) {
        for (GameClientHandler client: gameClientsVector) {
            System.out.println("check with  "+client.playerId2);
            if (client.playerId2 == playerId) {
                System.out.println("found client "+client.playerId+"    "+client.playerId2);
                return client;
            }
        }
        return null; // should I really return null? Doesn't the concurrent map not accept nulls?
    }
    

    public String sendGameRequest(int requestingPlayerId, String requestingPlayerUsername) {
        
        JSONObject json = new JSONObject();
        json.put("requestType", "GAME_REQUEST");
        json.put("requestingPlayer_ID", requestingPlayerId);
        json.put("requestingPlayerUsername", requestingPlayerUsername);
        
        System.out.println("Sending game request JSON: " + json.toString()); 
        
        return json.toString();
        
        /*printStream.println(json.toString());
        printStream.flush();*/
    }
    

    private void closeResources() {
        try {
            if(bufferedReader!=null)bufferedReader.close();
            if(printStream!=null) printStream.close();
            if(gameClientSocket!=null)gameClientSocket.close();
        } catch(IOException e) {
            e.printStackTrace(); 
            System.out.println("Error while closing resources.");
        }
    }
    
    
    public static synchronized void closeAllClients() {
        new Thread(() -> {
         try{
             Iterator<GameClientHandler> iterator = gameClientsVector.iterator();
            while (iterator.hasNext()) {
                GameClientHandler client = iterator.next();
                client.closeResources();
                iterator.remove(); 
                System.out.println("Client removed.");
            }
          clientMap.clear();
         }catch(Exception e){
             System.out.print("");
         }    
        }).start();
   }

    /*public static void printVector() {
        for (GameClientHandler handler: GameClientHandler.gameClientsVector) {
            System.out.println(handler.playerId);
        }
    }*/

    public void sendRequest(String request) {
        printStream.println(request);
        printStream.flush();
    }
  
}
