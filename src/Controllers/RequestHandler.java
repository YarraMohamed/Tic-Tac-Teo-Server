package Controllers;

import database.Player;
import database.PlayerDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler {
    
    public String signInHandle(String name, String password){
        try {
            
            Player player = new Player(name,password);
            
            boolean result = PlayerDAO.signIn(player);
            System.out.println("PlayerDOA result"+result);
            return result ? "Success" : "Failed";
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    }
    
    public String signUpHandle(String name, String email,String password){
        try {
            
            Player player = new Player(name,email,password);
            
            boolean result = PlayerDAO.signUp(player);
            System.out.println("PlayerDOA result"+result);
            return result ? "Success" : "Failed";
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "Database Error";
        }  
    }
    
}
