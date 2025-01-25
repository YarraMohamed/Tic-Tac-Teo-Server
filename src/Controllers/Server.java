package Controllers;

import database.DatabaseConnection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    private ServerSocket serverSocket;
    private final int port = 5005;
    
    private boolean isRunning = true;
    
    
    public Server() {
        startServer(); 
    }
    
    
    private void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            DatabaseConnection.getDBConnection();
            
            // Use a thread to prevent UI from freezing while server is waiting for a client to connect
            new Thread(() -> { 
                try {
                    while (isRunning) { 
                        Socket gameClientSocket = serverSocket.accept();
                        new GameClientHandler(gameClientSocket);
                } 
            } catch(IOException e) {
                if (isRunning) {
                    e.printStackTrace();
                    System.out.println("Error starting server on port " + port);
                }   
            } finally {
                try {
                    serverSocket.close(); 
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Failed to close the server socket.");
                    }
                }
            }).start();
            
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error starting server on port" + port);
        }
    }
    
    
    public void stopServer() {
        isRunning = false;
        GameClientHandler.closeAllClients();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            DatabaseConnection.closeDBConnection();
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error stopping server on port " + port);
        }
    }

}    