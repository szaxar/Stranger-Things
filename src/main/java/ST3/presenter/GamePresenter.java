package ST3.presenter;

import ST3.model.creature.Hero;
import ST3.model.map.GameMap;
import ST3.model.map.Tile;
import ST3.presenter.map.MapEditorPresenter;
import ST3.utils.Saver;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class GamePresenter extends MapEditorPresenter {

    private Hero currentHero = null;
    private ArrayList<Hero> heroArrayList = null;
    @FXML
    private TabPane tabs;
    @FXML
    private BorderPane roundPane;
    @FXML
    private Label roundInfo;
    @FXML
    private VBox rightPane;

    Rectangle heroMarker;

    @FXML
    public void initialize() {
        rightPane.getChildren().remove(roundPane);

        heroMarker = new Rectangle(Tile.getWidth(), Tile.getHeight());
        heroMarker.setFill(Color.TRANSPARENT);
        heroMarker.setArcWidth(Tile.getWidth() / 10);
        heroMarker.setArcHeight(Tile.getHeight() / 10);
        heroMarker.setStroke(Color.rgb(208, 20, 20));
        heroMarker.setStrokeWidth(2);
    }

    @FXML
    public void onNextRound() {
        heroArrayList = creatureEditor.getHeroArrayList();
        rightPane.getChildren().remove(tabs);
        rightPane.getChildren().add(roundPane);
        nextRound();
    }

    @FXML
    public void onEndOfRound() {
        removeEventListeners(currentHero);
        if (heroArrayList.isEmpty()) {
            rightPane.getChildren().remove(roundPane);
            rightPane.getChildren().add(tabs);
            currentHero = null;
            return;
        }
        nextRound();
    }

    private void nextRound() {
        Hero hero = heroArrayList.get(0);
        heroArrayList.remove(hero);
        currentHero = hero;
        roundInfo.setText(hero.getName() + "'s round");
        bindEventListeners(hero);
    }

    private void bindEventListeners(Hero hero) {
        Group view = creatureEditor.getCreatureView(hero);
        view.getChildren().add(heroMarker);
        creatureEditor.addMoveCreatureEventHandler(hero, view);
    }

    private void removeEventListeners(Hero hero) {
        Group view = creatureEditor.getCreatureView(hero);
        if (view == null) {
            return;
        }
        view.getChildren().remove(heroMarker);
        creatureEditor.removeMoveCreatureEventHandler(view);
    }

    @FXML
    public void onSaveGame() {
        if (mapName.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game name");
            alert.setHeaderText(null);
            alert.setContentText("Enter the game name");
            alert.showAndWait();
            return;
        }
        boolean saveCompleted = Saver.saveGame(mapName.getText(), (GameMap) map);
        if (!saveCompleted) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Game could not be saved. Please try again later.");
            alert.showAndWait();
        }
    }

}
