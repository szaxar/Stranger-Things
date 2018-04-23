package ST3.presenter.map;

import ST3.controller.CreatureController;
import ST3.controller.FightController;
import ST3.model.creature.Creature;
import ST3.model.creature.Enemy;
import ST3.model.creature.Hero;
import ST3.model.map.EditableMap;
import ST3.model.map.Tile;
import ST3.utils.Saver;
import ST3.presenter.map.creature.CreatureEditor;
import ST3.presenter.map.tile.BoardRenderer;
import ST3.presenter.map.tile.TileDrawer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class MapEditorPresenter extends BoardRenderer {

    public void initialize(CreatureController creatureController, FightController fightController, EditableMap map) {
        this.creatureController = creatureController;
        this.fightController = fightController;
        this.map = map;
        renderBoard(map);
        initializeSettingsTab();
        initializeTileTab();
        initializeCreatureTab();
    }


    @FXML
    protected TextField mapName;

    private void initializeSettingsTab() { }

    public void setFilename(String filename) {
        mapName.setText(filename);
    }

    @FXML
    public void onSaveMap() {
        if (mapName.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Map name");
            alert.setHeaderText(null);
            alert.setContentText("Enter the map name");
            alert.showAndWait();
            return;
        }
        boolean saveCompleted = Saver.saveMap(mapName.getText(), map);
        if (!saveCompleted) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Map could not be saved. Please try again later.");
            alert.showAndWait();
        }
    }


    private TileDrawer tileDrawer = null;
    @FXML
    private Tab tileTab;
    @FXML
    private ComboBox<Tile> tileComboBox;
    @FXML
    private ImageView tileImageView;

    private void initializeTileTab() {
        tileComboBox.setItems(FXCollections.observableArrayList(Tile.values()));
        tileComboBox.getSelectionModel().selectFirst();

        Tile firstTile = tileComboBox.getItems().get(0);
        tileImageView.setImage(firstTile.getImage());
        tileComboBox.valueProperty().addListener((observableValue, oldTile, newTile) -> tileImageView.setImage(newTile.getImage()));

        tileDrawer = new TileDrawer((EditableMap) map, boardContainer, tileComboBox);
        tileTab.selectedProperty().addListener((observable, oldValue, isActive) -> {
            if (isActive) {
                tileDrawer.enable();
            } else {
                tileDrawer.disable();
            }
        });
    }

    @FXML
    public void onResetTiles() {
        tileDrawer.resetTiles();
    }


    private FightController fightController;
    protected CreatureEditor creatureEditor;
    private CreatureController creatureController;
    @FXML
    private ListView<Creature> creatureList;
    @FXML
    private ListView<CreatureModelItem> creatureModels;
    @FXML
    private Tab creatureTab;

    private void initializeCreatureTab() {
        creatureEditor = new CreatureEditor(map, boardContainer, creatureModels, creatureList, fightController);
        creatureTab.selectedProperty().addListener((observable, oldValue, isActive) -> {
            if (isActive) {
                creatureEditor.enable();
            } else {
                creatureEditor.disable();
            }
        });

        ArrayList<String> savedCreatures = Saver.loadSavedCreatures();
        if (savedCreatures != null) {
            for (String filename : savedCreatures) {
                System.out.println(filename);
                Creature creature = Saver.loadCreature(filename);
                if (creature == null) {
                    continue;
                }
                creatureModels.getItems().add(new CreatureModelItem(creature, filename));
            }
        }
    }

    @FXML
    public void onAddCreatureModel() {
        Creature creature = creatureController.showCreatureEditDialog(null);
        if (creature == null) {
            return;
        }
        String filename = Saver.saveCreature(creature);
        if (filename == null) {
            return;
        }
        creatureModels.getItems().add(new CreatureModelItem(creature, filename));
        sortCreatureModels();
    }

    @FXML
    public void onEditCreatureModel() {
        CreatureModelItem selectedModel = creatureModels.getSelectionModel().getSelectedItem();
        if (selectedModel == null) {
            return;
        }
        Creature editedCreature = creatureController.showCreatureEditDialog(selectedModel.getCreature());
        if (editedCreature == null){
            return;
        }
        Saver.deleteCreature(selectedModel.getFilename());
        String newFilename = Saver.saveCreature(editedCreature);
        if (newFilename == null) {
            return;
        }
        selectedModel.setCreature(editedCreature);
        selectedModel.setFilename(newFilename);
        creatureModels.refresh();
        sortCreatureModels();
    }

    @FXML
    public void onDeleteCreatureModel() {
        CreatureModelItem selectedModel = creatureModels.getSelectionModel().getSelectedItem();
        if (selectedModel == null) {
            return;
        }
        Saver.deleteCreature(selectedModel.getFilename());
        creatureModels.getItems().remove(selectedModel);
    }

    private void sortCreatureModels() {
        creatureModels.getItems().sort((CreatureModelItem a, CreatureModelItem b) -> {
            if (a.equals(b)) {
                return 0;
            }

            if (!a.getCreature().getClass().equals(b.getCreature().getClass())) {
                if (a.getCreature() instanceof Hero) return -1;
                if (b.getCreature() instanceof Hero) return 1;
                if (a.getCreature() instanceof Enemy) return -1;
                if (b.getCreature() instanceof Enemy) return 1;
            }

            return a.getCreature().getName().compareTo(b.getCreature().getName());
        });
    }

    @FXML
    public void onEditCreature() {
        Creature selectedCreature = creatureList.getSelectionModel().getSelectedItem();
        if (selectedCreature == null) {
            return;
        }
        Creature editedCreature = creatureController.showCreatureEditDialog(selectedCreature);
        if (editedCreature == null) {
            return;
        }
        Point position = map.getCreaturePosition(selectedCreature);
        creatureEditor.removeCreatureFromBoard(selectedCreature);

        map.addCreature(editedCreature, position.x, position.y);
        creatureEditor.addCreatureToBoard(editedCreature, position);
        creatureEditor.addMoveCreatureEventHandler(editedCreature, creatureEditor.getCreatureView(editedCreature));
        creatureList.refresh();
    }

    @FXML
    public void onDeleteCreature() {
        Creature selectedCreature = creatureList.getSelectionModel().getSelectedItem();
        if (selectedCreature == null) {
            return;
        }
        creatureEditor.removeCreatureFromBoard(selectedCreature);
    }

}
