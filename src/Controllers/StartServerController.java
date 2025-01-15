package Controllers; 

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class StartServerController {

    @FXML
    private AnchorPane startServerScreen;
    @FXML
    private VBox VBOXForTitileAndButton;
    @FXML
    private Text titleInStartServerScreen;
    @FXML
    private Button startServerButton;
    @FXML
    private ImageView xoImageUpperRightCorner;
    @FXML
    private ImageView xoImageLowerLeftCorner;
    
    
    private Server serverInstance;
    
    
    @FXML
    private void handleStartServerButtonAction(ActionEvent startServerEvent) {
        try {
            serverInstance = new Server(); // Initialize server instance
            
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/StopServerAndStatistics.fxml"));
            Parent stopServerAndStatisticsRoot = loader.load();
            
 
            // Access the controller of StopServerAndStatistics screen to set the serverInstance and the stage
            StopServerAndStatisticsController controller = loader.getController();
            controller.setServerInstance(serverInstance); // Pass the same instance of the server that was started so when closing you close the same instance
            
            
            Scene currentScene = ((Node) startServerEvent.getSource()).getScene();
            controller.setStage((Stage) currentScene.getWindow()); // Pass the stage to the controller of StopServerAndStatistics to shut down the server on window close
            
            currentScene.setRoot(stopServerAndStatisticsRoot);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
       
    
}
