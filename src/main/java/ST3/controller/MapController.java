package ST3.controller;

import ST3.model.map.EditableMap;
import ST3.model.map.GameMap;
import ST3.presenter.map.MapEditorPresenter;
import ST3.presenter.map.MapListPresenter;
import ST3.presenter.GamePresenter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MapController {

    private Stage primaryStage;

    public MapController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setEditorScene(EditableMap map) {
        setEditorScene(map, null);
    }

    public void setEditorScene(EditableMap map, String filename) {
        try {
            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/MapEditor.fxml"));
            Parent rootLayout = loader.load();

            // set map and render it
            MapEditorPresenter mapEditorPresenter = loader.getController();
            mapEditorPresenter.initialize(new CreatureController(primaryStage), new FightController(primaryStage), map);
            if (filename != null && filename.length() > 0) {
                mapEditorPresenter.setFilename(filename);
            }

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    public void setGameScene(GameMap gameMap, String filename) {
        try {
            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/Game.fxml"));
            Parent rootLayout = loader.load();

            // set map and render it
            GamePresenter gamePresenter = loader.getController();
            gamePresenter.initialize(new CreatureController(primaryStage), new FightController(primaryStage), gameMap);
            if (filename != null && filename.length() > 0) {
                gamePresenter.setFilename(filename);
            }

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    public void showSavedMapsDialog() {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/MapList.fxml"));
            Parent page = loader.load();

            // set map controller
            MapListPresenter mapListPresenter = loader.getController();
            mapListPresenter.setMapController(this);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Saved maps");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(page));
            dialogStage.showAndWait();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

}
