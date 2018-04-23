package ST3;

import ST3.controller.MapController;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Stranger Things 3");

        MapController mapController = new MapController(primaryStage);
        mapController.showSavedMapsDialog();

    }
}
