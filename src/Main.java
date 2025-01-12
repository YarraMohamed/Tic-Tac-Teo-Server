import Controllers.StopServerAndStatisticsController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("FXML/StartServer.fxml"));
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/StopServerAndStatistics.fxml"));
        // I was trying to handle shutting down the server when window is closed
        /*Parent stopServerAndStatisticsRoot = loader.load();
        StopServerAndStatisticsController controller = loader.getController();
        controller.setStage(stage);*/
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Resources/ServerStyle.css").toExternalForm());
        
        stage.setScene(scene);
        stage.show();
        
        // I was trying to handle shutting down the server when window is closed
        /*stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              System.out.println("Stage is closing");
          }
        });        
            stage.close();*/

        }


    public static void main(String[] args) {
        launch(args);
    }
    
}
