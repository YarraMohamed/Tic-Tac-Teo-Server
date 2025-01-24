package Controllers;

import database.PlayerDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;


public class GameClientHandler extends Thread {
    
    private Socket gameClientSocket;
    private static Vector<GameClientHandler> gameClientsVector = new Vector<>();
    private BufferedReader bufferedReader;
    private PrintStream printStream;
    private int userID;
    
    
    public GameClientHandler(Socket gameClientSocket) {
        this.gameClientSocket = gameClientSocket;
        initializeStreams();
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
//            sendToAllUsers();
            start(); 
        } catch (IOException e) {
          e.printStackTrace();
          System.out.println("Error initializing streams for client");
        }
    }
    
    private void handleClient() {
        try {
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                System.out.println(message);
                String response = RequestRouter.routeRequest(message, this);
                printStream.println(response);
                printStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while trying to establish a connection with client.");
        } finally {
            closeResources();
            GameClientHandler.gameClientsVector.remove(this);
            //System.out.println("Client is disconnecting."); // Commented it as I think it's unsuitable message for what happens here
        }
    }
    /*
   private void handleClient(){
        try{
            String message;
            while(!gameClientSocket.isClosed()){
                try{
                    message = bufferedReader.readLine();
                    if(message==null){
                        if(gameClientSocket.isClosed()){
                            break;
                        }
                        continue;
                    }
                    String response = RequestRouter.routeRequest(message, this);
                    printStream.println(response);
                    printStream.flush();
                    JSONObject request = new JSONObject(message);
                    if (request.getString("requestType").equals("SIGN_OUT")) {
                        break;
                    }
                }catch(IOException e){
                    System.out.println("Cannot read from this socket");
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Cannot open connection");
        } finally{
            closeResources();
            GameClientHandler.gameClientsVector.remove(this); 
            System.out.println("Client is disconnected.");
        }
    }*/

    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public static PrintStream getClientEar(int id) {
    // Iterate over all connected game clients
    for (GameClientHandler c : gameClientsVector) {
        // Print the userID for debugging purposes
        System.out.println("Checking Player ID: " + c.userID);
        
        // If the given ID matches the userID, return the associated PrintStream
        if (id == c.userID) {   
            return c.printStream;
        }
    }
   
    // If no client with the given ID is found, return null
    System.out.println("No client found with ID: " + id);  // Debugging log
    return null;
}
    public void sendRequest(String requset){
        printStream.println(requset);
        printStream.flush();
    }
    public static GameClientHandler getClientById(int id) {
    // Iterate over all connected game clients
    for (GameClientHandler c : gameClientsVector) {
        // Print the userID for debugging purposes
        System.out.println("Checking Player ID: " + c.userID);
        
        // If the given ID matches the userID, return the associated PrintStream
        if (id == c.userID) {  
            System.out.println("Found client with ID  "+c.userID);
            return c;
        }
    }
   
    // If no client with the given ID is found, return null
    System.out.println("No client found with ID: " + id);  // Debugging log
    return null;
}

    public static void sendToAllUsers(){
        Thread th= new Thread(()->{
            while (true) {                
                for(GameClientHandler c :gameClientsVector){
                c.printStream.println("test");
                c.printStream.flush();
            }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    System.out.println("Errror ::::::::::::::::::::::"+ex.getMessage());
                    Logger.getLogger(GameClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        );
        th.start();
    }

    private void closeResources() {
        try {
            bufferedReader.close();
            printStream.close();
            gameClientSocket.close();
        } catch(IOException e) {
            e.printStackTrace(); 
            System.out.println("Error while closing resources.");
        }
    }
    
    
    public static void closeAllClients() {
        for (GameClientHandler gameClientHandler : gameClientsVector) {
                gameClientHandler.closeResources();    
        }
    }
  
}
