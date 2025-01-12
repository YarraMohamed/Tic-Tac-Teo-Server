/* To change: 
    - in catch(IOException iOException), iOException needs to be ioException
    make the O lowercased. OR make it 'e'. This hsould be done in all files! 
    
   To add: 
    - Comments on the code. 
    - Print statements in try blocks
*/

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
            DatabaseConnection.closeDBConnection();
        } catch(IOException iOException) {
            iOException.printStackTrace();
            System.out.println("Error stopping server on port " + port);
        }
    }

}    
