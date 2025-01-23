package Controllers;

import database.PlayerDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import org.json.JSONObject;


public class GameClientHandler extends Thread {
    
    private Socket gameClientSocket;
    private static Vector<GameClientHandler> gameClientsVector = new Vector<>();
    private BufferedReader bufferedReader;
    private PrintStream printStream;
    
    
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
            start(); 
        } catch (IOException e) {
          e.printStackTrace();
          System.out.println("Error initializing streams for client");
        }
    }
    
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
