package Controllers; 

import database.PlayerDAO;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Stage;


public class StopServerAndStatisticsController {

    @FXML
    private AnchorPane stopServerAndStatisticsScreen;
    
    @FXML
    private Text statisticsTitle;
    
    @FXML
    private BarChart<String, Integer> userStatusBarChart;
    private final double categoryGap = 250;
    private final double barGap = 0.5;
    private final String onlineStatus = "Online";
    private final String offlineStatus = "Offline";
    private int numberOfOnlinePlayers;
    private int numberOfOfflinePlayers;
    
    @FXML
    private NumberAxis numberAxis;
    
    @FXML
    private CategoryAxis categoryAxis;
    
    @FXML
    private ImageView xoImageUpperRightCorner;
    
    @FXML
    private Button stopServerButton;
    
    private Server serverInstance;
    
    private Stage stage;
    
    
    public void setServerInstance(Server serveInstance) {
        this.serverInstance = serveInstance;
    }
    
    // Acessing the stage to shut down the server on window close
    public void setStage(Stage stage) {
        this.stage = stage;
        // EventHandler is a functional interface and can therefore be used as the assignment target for a lambda expression
        stage.setOnCloseRequest(closeStageEvent -> {
              if (serverInstance != null) {
                  serverInstance.stopServer();
              }  
        });
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


    @FXML
    public void initialize() {
        updateBarChart();
    }
    
    

    // Method to create the bar chart to show the number of online/offline players
    // Will be called in updateBarChart()
    private void createBarChart() {
        /* WARNING: NOT ABSOLUTELY SURE WE NEED IT */
        userStatusBarChart.getData().clear(); // Call clear() to dynamically update the bar chart as clients connect and disconnect
        
        XYChart.Series<String, Integer> onlineSeries = new XYChart.Series<>();
        onlineSeries.setName(onlineStatus); 
        onlineSeries.getData().add(new XYChart.Data<>(onlineStatus, numberOfOnlinePlayers)); 
        
        
        XYChart.Series<String, Integer> offlineSeries = new XYChart.Series<>();
        offlineSeries.setName(offlineStatus); 
        offlineSeries.getData().add(new XYChart.Data<>(offlineStatus, numberOfOfflinePlayers));
        
        
        userStatusBarChart.getData().addAll(onlineSeries, offlineSeries);
        userStatusBarChart.setCategoryGap(categoryGap); 
        userStatusBarChart.setBarGap(barGap);
    }
    
    
    // Method to update the bar chart with the actual numbers of online/offline players that we get from the database
    private void updateBarChart() {
        try {
            numberOfOnlinePlayers = PlayerDAO.getNumberOfOnlinePlayers();
            numberOfOfflinePlayers = PlayerDAO.getNumberOfOfflinePlayers();
            createBarChart();
        } catch (SQLException e) {
            System.out.println("Failed to get the number of online/offline players.");
            e.printStackTrace();
        }
    }
    
}
