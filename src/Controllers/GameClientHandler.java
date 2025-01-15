package Controllers;

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
        while(true) {
            try {
                String message = bufferedReader.readLine();
                if (message == null) {
                    System.out.println("Client is disconnecting.");
                    GameClientHandler.gameClientsVector.remove(this);
                    break;
                }
            } catch(IOException e) {
                e.printStackTrace();
                System.out.println("Error while trying to establish a connection with client.");
            } finally {
                closeResources();
            }
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
