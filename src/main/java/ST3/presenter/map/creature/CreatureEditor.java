package ST3.presenter.map.creature;

import ST3.controller.FightController;
import ST3.model.creature.Creature;
import ST3.model.map.Map;
import ST3.model.map.Tile;
import ST3.utils.ResourcesUtils;
import ST3.presenter.map.CreatureModelItem;
import ST3.presenter.map.Point;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

public class CreatureEditor extends CreatureRenderer {

    private ListView<CreatureModelItem> creatureModels;

    // for adding and moving creature
    private Creature selectedCreature = null;
    private FightController fightController;
    private Group creatureView = null;
    private double oldX, oldY;

    private Rectangle background;
    private static final Color AVAILABLE_COLOR = Color.rgb(127, 255, 0, 0.5);
    private static final Color UNAVAILABLE_COLOR = Color.rgb(204, 0, 0, 0.5);

    public CreatureEditor(Map map, AnchorPane boardContainer, ListView<CreatureModelItem> creatureModels, ListView<Creature> creatureList, FightController fightController) {
        super(map, boardContainer, creatureList);
        this.creatureModels = creatureModels;
        this.fightController = fightController;
        background = new Rectangle(Tile.getWidth(), Tile.getHeight());
        background.setArcWidth(Tile.getWidth() / 10);
        background.setArcHeight(Tile.getHeight() / 10);

        Image crossedSwordsImage = new Image(ResourcesUtils.getResourcePath("Cursor/crossedSwords.png"));
        fightImageCursor = new ImageCursor(crossedSwordsImage);
    }

    private void addMoveCreatureEventHandler(HashMap<? extends Creature, Group> creatureViewHashMap) {
        for (java.util.Map.Entry<? extends Creature, Group> entry : creatureViewHashMap.entrySet()) {
            Creature creature = entry.getKey();
            Group view = entry.getValue();
            addMoveCreatureEventHandler(creature, view);
        }
    }

    private Point oldPosition;
    private ImageCursor fightImageCursor;
    public void addMoveCreatureEventHandler(Creature creature, Group view) {
        view.setOnMousePressed((MouseEvent mouseEvent) -> {
            selectedCreature = creature;
            creatureView = view;

            background.setFill(AVAILABLE_COLOR);
            creatureView.getChildren().add(background);
            oldX = mouseEvent.getSceneX();
            oldY = mouseEvent.getSceneY();
            oldPosition = map.getCreaturePosition(selectedCreature);

            creatureList.getSelectionModel().select(selectedCreature);
        });

        view.setOnMouseDragged((MouseEvent mouseEvent) -> {
            creatureView.setTranslateX(mouseEvent.getSceneX() - oldX);
            creatureView.setTranslateY(mouseEvent.getSceneY() - oldY);
            Point currentTile = getTilePositionByScenePosition(creatureView.localToScene(mouseEvent.getX(), mouseEvent.getY()));
            Creature creatureOnCurrentTile = map.getCreatureByPosition(currentTile);
            if (creatureOnCurrentTile != null && creatureOnCurrentTile != selectedCreature) {
                boardContainer.getScene().setCursor(fightImageCursor);
            } else {
                boardContainer.getScene().setCursor(null);
            }
            setTileBackgroundByPosition(currentTile);
        });

        view.setOnMouseReleased((MouseEvent mouseEvent) -> {
            creatureView.getChildren().remove(background);
            boardContainer.getScene().setCursor(null);

            Point newPosition = getTilePositionByScenePosition(creatureView.localToScene(mouseEvent.getX(), mouseEvent.getY()));
            creatureView.setTranslateX(0);
            creatureView.setTranslateY(0);

            if (newPosition == null || oldPosition.equals(newPosition)) {
                return;
            }

            if(map.isTileTaken(newPosition)) {
                Creature opponent = map.getCreatureByPosition(newPosition);
                if (opponent != null && opponent != selectedCreature) {
                    Creature winner = fightController.fight(selectedCreature, opponent);
                    if(winner!=null) {
                        Creature loser = winner == selectedCreature ? opponent : selectedCreature;
                        removeCreatureFromBoard(loser);
                        if (winner == selectedCreature) {
                            moveCreature(selectedCreature, newPosition.x, newPosition.y);
                        }
                    }
                }
            } else {
                moveCreature(selectedCreature, newPosition.x, newPosition.y);
            }
        });
    }

    private void removeMoveCreatureEventHandler(HashMap<? extends Creature, Group> creatureViewHashMap) {
        for (java.util.Map.Entry<? extends Creature, Group> entry : creatureViewHashMap.entrySet()) {
            Group view = entry.getValue();
            removeMoveCreatureEventHandler(view);
        }
    }

    public void removeMoveCreatureEventHandler(Group view) {
        view.setOnMousePressed(null);
        view.setOnMouseDragged(null);
        view.setOnMouseReleased(null);
    }

    private void addAddCreatureEventHandler() {
        creatureModels.setOnMousePressed((MouseEvent event) -> {
            CreatureModelItem selectedModel = creatureModels.getSelectionModel().getSelectedItem();
            if (selectedModel == null) {
                return;
            }
            selectedCreature = selectedModel.getCreature();
            oldX = event.getSceneX();
            oldY = event.getSceneY();
            creatureView = createView(selectedCreature);
            boardContainer.getChildren().add(creatureView);
            background.setFill(AVAILABLE_COLOR);
            creatureView.getChildren().add(background);
            Point2D boardPosition = boardContainer.sceneToLocal(event.getSceneX(), event.getSceneY());
            creatureView.relocate(boardPosition.getX() - Tile.getWidth() / 2, boardPosition.getY() - Tile.getHeight() / 2);
        });

        creatureModels.setOnMouseDragged((MouseEvent mouseEvent) -> {
            if (selectedCreature == null) {
                return;
            }
            creatureView.setTranslateX(mouseEvent.getSceneX() - oldX);
            creatureView.setTranslateY(mouseEvent.getSceneY() - oldY);
            Point2D scenePosition = creatureModels.localToScene(mouseEvent.getX(), mouseEvent.getY());
            Point currentTile = getTilePositionByScenePosition(scenePosition);
            setTileBackgroundByPosition(currentTile);
        });

        creatureModels.setOnMouseReleased((MouseEvent event) -> {
            if (selectedCreature == null) {
                return;
            }
            creatureView.getChildren().remove(background);
            boardContainer.getChildren().remove(creatureView);

            if (oldX == event.getSceneX() && oldY == event.getSceneY()) {
                return;
            }

            Point2D scenePosition = creatureModels.localToScene(event.getX(), event.getY());
            Point position = getTilePositionByScenePosition(scenePosition);
            if (position == null || map.isTileTaken(position)) {
                return;
            }
            Creature creatureToAdd = selectedCreature.getCopy();
            map.addCreature(creatureToAdd, position.x, position.y);
            addCreatureToBoard(creatureToAdd, position);
            addMoveCreatureEventHandler(creatureToAdd, getCreatureView(creatureToAdd));
            selectedCreature = null;
            creatureView = null;
        });
    }

    private void removeAddCreatureEventHandler() {
        creatureModels.setOnMousePressed(null);
        creatureModels.setOnMouseDragged(null);
        creatureModels.setOnMouseReleased(null);
    }

    public void enable() {
        addAddCreatureEventHandler();
        addMoveCreatureEventHandler(heroViewHashMap);
        addMoveCreatureEventHandler(enemyViewHashMap);
        addMoveCreatureEventHandler(npcViewHashMap);
    }

    public void disable() {
        removeAddCreatureEventHandler();
        removeMoveCreatureEventHandler(heroViewHashMap);
        removeMoveCreatureEventHandler(enemyViewHashMap);
        removeMoveCreatureEventHandler(npcViewHashMap);
    }

    public void refreshCreatureView(Creature creature) {
        Group oldView = getCreatureView(creature);
        boardContainer.getChildren().remove(oldView);
        Point position = map.getCreaturePosition(creature);
        renderCreature(creature, position);
        Group newView = getCreatureView(creature);
        addMoveCreatureEventHandler(creature, newView);
    }

    private void setTileBackgroundByPosition(Point point) {
        if (point == null || (!point.equals(oldPosition) && map.isTileTaken(point))) {
            if (!background.getFill().equals(UNAVAILABLE_COLOR)) {
                background.setFill(UNAVAILABLE_COLOR);
            }
        } else if (!background.getFill().equals(AVAILABLE_COLOR)) {
            background.setFill(AVAILABLE_COLOR);
        }
    }

    private Point getTilePositionByScenePosition(Point2D scenePosition) {
        Point2D boardPosition = boardContainer.sceneToLocal(scenePosition);
        double boardX = boardPosition.getX() - Tile.getWidth() / 2;
        double boardY = boardPosition.getY() - Tile.getHeight() / 2;
        if (boardX < 0 || boardY < 0) {
            return null;
        }
        int tileX = (int)(boardX / Tile.getWidth());
        int tileY = (int)(boardY / Tile.getHeight());
        if (tileX >= map.getWidth() || tileY >= map.getHeight()) {
            return null;
        }
        return new Point(tileX, tileY);
    }

}
