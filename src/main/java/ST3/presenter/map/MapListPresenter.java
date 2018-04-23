package ST3.presenter.map;

import ST3.controller.MapController;
import ST3.model.map.EditableMap;
import ST3.model.map.GameMap;
import ST3.model.map.Map;
import ST3.utils.Saver;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class MapListPresenter {

    private class IntegerField extends TextField {
        @Override
        public void replaceText(int start, int end, String text) {
            if (validate(text)) {
                super.replaceText(start, end, text);
            }
        }

        @Override
        public void replaceSelection(String text) {
            if (validate(text)) {
                super.replaceSelection(text);
            }
        }

        public Integer getValue() {
            if (this.getText() == null || this.getText().length() == 0) {
                return null;
            }
            return Integer.valueOf(this.getText());
        }

        private boolean validate(String text) {
            return text.matches("[0-9]*");
        }
    }

    MapController mapController;
	@FXML
	private ListView<String> savedMapList;
	@FXML
    private ListView<String> savedGameList;

	@FXML
    public void initialize() {
        initializeMapModelTab();
        initializeGameTab();
    }

    public void setMapController(MapController mapController) {
	    this.mapController = mapController;
    }

    private void initializeMapModelTab() {
        ArrayList<String> savedMaps = Saver.loadSavedMaps();
        if (savedMaps != null) {
            for (String filename : savedMaps) {
                savedMapList.getItems().add(filename);
            }
            Collections.sort(savedMapList.getItems());
        }
    }

    @FXML
    public void onCreate() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Creating map");
        dialog.setHeaderText(null);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        IntegerField widthField = new IntegerField();
        widthField.setPromptText("Width");
        IntegerField heightField = new IntegerField();
        heightField.setPromptText("Height");

        grid.add(new Label("Width:"), 0, 0);
        grid.add(widthField, 1, 0);
        grid.add(new Label("Height:"), 0, 1);
        grid.add(heightField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);

        widthField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(widthField.getValue() == null || heightField.getValue() == null);
        });

        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(widthField.getValue() == null || heightField.getValue() == null);
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
            return;
        }

        mapController.setEditorScene(new EditableMap(widthField.getValue(), heightField.getValue()));
        closeWindow();
    }

	@FXML
	public void onEdit() {
        String filename = savedMapList.getSelectionModel().getSelectedItem();
        if (filename == null) {
            return;
        }
        EditableMap editableMap = Saver.loadEditableMap(filename);
        if (editableMap == null) {
            return;
        }
        mapController.setEditorScene(editableMap, filename);
        closeWindow();
	}

	@FXML
	public void onDelete() {
        String filename = savedMapList.getSelectionModel().getSelectedItem();
        if (filename == null) {
            return;
        }
        Saver.deleteMap(filename);
        savedMapList.getItems().remove(filename);
	}

	@FXML
    public void onGameStart() {
        String filename = savedMapList.getSelectionModel().getSelectedItem();
        if (filename == null) {
            return;
        }
        GameMap gameMap = Saver.loadNewGameMap(filename);
        if (gameMap == null) {
            return;
        }
        mapController.setGameScene(gameMap, filename);
        closeWindow();
    }

    private void initializeGameTab() {
        ArrayList<String> savedGames = Saver.loadSavedGames();
        if (savedGames != null) {
            for (String filename : savedGames) {
                savedGameList.getItems().add(filename);
            }
            Collections.sort(savedGameList.getItems());
        }
    }

    @FXML
    public void onGameResume() {
        String filename = savedGameList.getSelectionModel().getSelectedItem();
        if (filename == null) {
            return;
        }
        GameMap gameMap = Saver.loadGameMap(filename);
        if (gameMap == null) {
            return;
        }
        mapController.setGameScene(gameMap, filename);
        closeWindow();
    }

    @FXML
    public void onGameDelete() {
        String filename = savedGameList.getSelectionModel().getSelectedItem();
        if (filename == null) {
            return;
        }
        Saver.deleteGame(filename);
        savedGameList.getItems().remove(filename);
    }

    private void closeWindow() {
        Stage stage = (Stage) savedMapList.getScene().getWindow();
        stage.close();
    }

}
