package Controllers;

import database.PlayerDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;


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
            System.out.println("Client is disconnecting.");
            closeResources();
            GameClientHandler.gameClientsVector.remove(this);
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
