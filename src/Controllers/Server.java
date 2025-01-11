package Controllers;

/* Note to myself: in catch(IOException iOException), iOException needs to be ioException
   make the O lowercased. This hsould be done in all files! 

   Add comments on the code. 

  WARNING: serevr doesn't shut down on window close  
*/
// package server; // commented it till we figure out where to put the Server.java file

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
            
            new Thread(() -> { 
                try {
                    while (isRunning) { 
                        Socket gameClientSocket = serverSocket.accept();
                        new GameClientHandler(gameClientSocket);
                } 
            } catch(IOException iOException) {
                if (isRunning) {
                    iOException.printStackTrace();
                    System.out.println("Error starting server on port " + port);
                }   
            } finally {
                try {
                    serverSocket.close();  /* Should stopServer() be called instead? + What is the difference between finally and try-catch*/
                    } catch (IOException iOException) {
                        iOException.printStackTrace();
                        /* Should a message be put here? */
                    }
                }
            }).start();
            
        } catch(IOException iOException) {
            iOException.printStackTrace();
            System.out.println("Error starting server on port" + port);
            /* Is this a suitbale message? It is just like the one above. */
        }
    }
    
    
    public void stopServer() {
        isRunning = false;
        GameClientHandler.closeAllClients();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch(IOException iOException) {
            iOException.printStackTrace();
            System.out.println("Error stopping server on port " + port);
        }
    }

}    