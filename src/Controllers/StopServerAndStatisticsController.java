// package server;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class StopServerAndStatisticsController implements Initializable {

    @FXML
    private AnchorPane stopServerAndStatisticsScreen;
    @FXML
    private Text statisticsTitle;
    @FXML
    private BarChart<String, Integer> userStatusBarChart;
    @FXML
    private NumberAxis numberAxis;
    @FXML
    private CategoryAxis categoryAxis;
    @FXML
    private ImageView xoImageUpperRightCorner;
    @FXML
    private Button stopServerButton;
    
    
    private Server serverInstance;
    public void setServerInstance(Server serveInstance) {
        this.serverInstance = serveInstance;
    }
    
    
    @FXML
    private void handleStopServerButtonAction(ActionEvent stopServerEvent) {
        if (serverInstance != null) {
            serverInstance.stopServer();
        }
        try {
            Parent startServerRoot = FXMLLoader.load(getClass().getResource("/FXML/StartServer.fxml"));
            Scene currentScene = ((Node) stopServerEvent.getSource()).getScene();
            currentScene.setRoot(startServerRoot);
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        XYChart.Series<String, Integer> onlineSeries = new XYChart.Series<>();
        onlineSeries.setName("Online"); // make a variable to store the string
        onlineSeries.getData().add(new XYChart.Data<>("Online", 10)); // make a variable to store the number
        
        
        XYChart.Series<String, Integer> offlineSeries = new XYChart.Series<>();
        offlineSeries.setName("Offline"); // same here
        offlineSeries.getData().add(new XYChart.Data<>("Offline", 5));
        
        
        userStatusBarChart.getData().addAll(onlineSeries, offlineSeries);
        userStatusBarChart.setCategoryGap(250); // variables to store the number
        userStatusBarChart.setBarGap(0.5); // same here
    
    }    
    
}
