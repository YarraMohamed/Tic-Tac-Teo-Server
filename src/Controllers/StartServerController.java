/* To add: 
    - Drop shadows to the button 
    - Same gradient in client-side
*/
package Controllers; 

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class StartServerController implements Initializable {

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
            StopServerAndStatisticsController controller = loader.getController();
            controller.setServerInstance(serverInstance);
            Scene currentScene = ((Node) startServerEvent.getSource()).getScene();
            currentScene.setRoot(stopServerAndStatisticsRoot);
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
