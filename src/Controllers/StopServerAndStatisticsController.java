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
    private BarChart<?, ?> userStatusBarChart;
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
        
        XYChart.Series series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Online", 2));
        series.getData().add(new XYChart.Data<>("Offline", 10));
        series.getData().add(new XYChart.Data<>(" ", 0));
        series.getData().add(new XYChart.Data<>(" ", 0));
        series.getData().add(new XYChart.Data<>(" ", 0));
        series.getData().add(new XYChart.Data<>(" ", 0));
        series.getData().add(new XYChart.Data<>(" ", 0));
        series.getData().add(new XYChart.Data<>(" ", 0));
        userStatusBarChart.getData().add(series);
    }    
    
}
