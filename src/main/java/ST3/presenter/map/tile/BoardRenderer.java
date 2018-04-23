package ST3.presenter.map.tile;

import ST3.model.map.EditableMap;
import ST3.model.map.Map;
import ST3.model.map.Tile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class BoardRenderer {

    protected Map map;
    @FXML
    protected ScrollPane mapWrapper;
    @FXML
    protected AnchorPane boardContainer;
    @FXML
    protected AnchorPane mapMiniatureWrapper;

    protected void renderBoard(Map map) {
        this.map = map;

        // add board
        Group board = createBoardGroup();
        boardContainer.getChildren().add(board);
        boardContainer.setPadding(new Insets(Tile.getHeight() / 2, Tile.getWidth() / 2, Tile.getHeight() / 2, Tile.getWidth() / 2));
        board.setTranslateX(Tile.getWidth() / 2);
        board.setTranslateY(Tile.getHeight() / 2);

        // add map miniature
        Group mapMiniature = createMapMiniatureGroup();
        mapMiniatureWrapper.getChildren().add(mapMiniature);

        // bind events
        bindBoardScrollEvents();
    }

    private Group createBoardGroup() {
        Group board = new Group();

        Group tiles = new Group();
        addBoardTilesToGroup(tiles, Tile.getWidth(), Tile.getHeight());

        Group grid = new Group();
        for (int x = 1; x < map.getWidth(); x++) {
            Rectangle verticalLine = createVerticalLine();
            verticalLine.relocate(Tile.getWidth() * x, 0);
            grid.getChildren().add(verticalLine);
        }
        for (int y = 1; y < map.getHeight(); y++) {
            Rectangle horizontalLine = createHorizontalLine();
            horizontalLine.relocate(0, Tile.getHeight() * y);
            grid.getChildren().add(horizontalLine);
        }

        board.getChildren().addAll(tiles, grid);
        return board;
    }

    private Rectangle createVerticalLine() {
        Rectangle rectangle = new Rectangle(2, Tile.getHeight() * map.getHeight());
        rectangle.setFill(Color.rgb(0, 0, 0, 0.3));
        return rectangle;
    }

    private Rectangle createHorizontalLine() {
        Rectangle rectangle = new Rectangle(Tile.getWidth() * map.getWidth(), 2);
        rectangle.setFill(Color.rgb(0, 0, 0, 0.3));
        return rectangle;
    }

    private void addBoardTilesToGroup(Group tiles, double tileWidth, double tileHeight) {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Image image = map.getTile(x, y).getImage();
                ImageView imageView = new ImageView(image);
                if (map instanceof EditableMap) {
                    ((EditableMap) map).registerImageViewForTile(x, y, imageView);
                }
                imageView.setFitWidth(tileWidth);
                imageView.setFitHeight(tileHeight);
                imageView.relocate(tileWidth * x, tileHeight * y);
                tiles.getChildren().add(imageView);
            }
        }
    }

    private double mapMiniatureTileWidth, mapMiniatureTileHeight;

    private Group createMapMiniatureGroup() {
        Group mapMiniature = new Group();
        Group mapViewSelectorWrapper = new Group();

        Group tiles = new Group();
        class SizeInitializer {
            private Pane pane;
            private boolean isMiniatureWrapperWidthInitialized = false;
            private boolean isMiniatureWrapperHeightInitialized = false;

            private SizeInitializer(Pane pane) {
                this.pane = pane;
                bindListeners();
            }

            private void bindListeners() {
                final SizeInitializer that = this;
                pane.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        observable.removeListener(this);
                        isMiniatureWrapperWidthInitialized = true;
                        if (isMiniatureWrapperHeightInitialized) {
                            that.callback();
                        }
                    }
                });
                pane.heightProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        observable.removeListener(this);
                        isMiniatureWrapperHeightInitialized = true;
                        if (isMiniatureWrapperWidthInitialized) {
                            that.callback();
                        }
                    }
                });
            }

            private void callback() {
                computeMapMiniatureTileSize();
                addBoardTilesToGroup(tiles, mapMiniatureTileWidth, mapMiniatureTileHeight);
                if (map.getWidth() >= map.getHeight()) {
                    tiles.relocate(mapMiniatureTileWidth / 2, (map.getWidth() + 1 - map.getHeight()) * mapMiniatureTileHeight / 2);
                } else {
                    tiles.relocate((map.getHeight() + 1 - map.getWidth()) * mapMiniatureTileWidth / 2, mapMiniatureTileHeight / 2);
                }
                createMapViewSelectorGroup(mapViewSelectorWrapper);
            }
        }
        new SizeInitializer(mapMiniatureWrapper);

        mapMiniature.getChildren().addAll(tiles, mapViewSelectorWrapper);
        return mapMiniature;
    }

    private void computeMapMiniatureTileSize() {
        if (map.getWidth() >= map.getHeight()) {
            mapMiniatureTileWidth = mapMiniatureWrapper.getWidth() / (map.getWidth() + 1);
            mapMiniatureTileHeight = Tile.getHeight() * mapMiniatureTileWidth / Tile.getWidth();
        } else {
            mapMiniatureTileHeight = mapMiniatureWrapper.getHeight() / (map.getHeight() + 1);
            mapMiniatureTileWidth = Tile.getWidth() * mapMiniatureTileHeight / Tile.getHeight();
        }
    }

    /* START OF SELECTOR MODULE */

    private Rectangle mapViewSelector;
    private double selectorPositionX, selectorPositionY;
    private double selectorTranslateX, selectorTranslateY;

    private EventHandler<MouseEvent> selectorOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            selectorPositionX = t.getSceneX();
            selectorPositionY = t.getSceneY();
            selectorTranslateX = mapViewSelector.getTranslateX();
            selectorTranslateY = mapViewSelector.getTranslateY();
        }
    };

    private EventHandler<MouseEvent> selectorOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - selectorPositionX;
            double offsetY = t.getSceneY() - selectorPositionY;
            double newTranslateX = selectorTranslateX + offsetX;
            double newTranslateY = selectorTranslateY + offsetY;

            if (newTranslateX >= 0 && newTranslateX + mapViewSelector.getWidth() < mapMiniatureWrapper.getWidth() - 2 * xMiniatureOffset) {
                newTranslateX /= (mapMiniatureWrapper.getWidth() - mapViewSelector.getWidth() - 2 * xMiniatureOffset);
                mapWrapper.setHvalue(newTranslateX);
            }
            if (newTranslateY >= 0 && newTranslateY + mapViewSelector.getHeight() < mapMiniatureWrapper.getHeight() - 2 * yMiniatureOffset) {
                newTranslateY /= (mapMiniatureWrapper.getHeight() - mapViewSelector.getHeight() - 2 * yMiniatureOffset);
                mapWrapper.setVvalue(newTranslateY);
            }
        }
    };

    private double xMiniatureOffset = 0, yMiniatureOffset = 0;

    private void createMapViewSelectorGroup(Group mapViewSelectorWrapper) {
        Rectangle selector = new Rectangle(0, 0);
        mapViewSelector = selector;
        mapViewSelectorWrapper.getChildren().add(selector);
        selector.setFill(Color.TRANSPARENT);
        selector.setStroke(Color.DIMGRAY);
        selector.setStrokeWidth(2);

        if (map.getWidth() >= map.getHeight()) {
            yMiniatureOffset = (mapMiniatureWrapper.getHeight() - mapMiniatureTileHeight * (map.getHeight() + 1)) / 2;
        } else {
            xMiniatureOffset = (mapMiniatureWrapper.getWidth() - mapMiniatureTileWidth * (map.getWidth() + 1)) / 2;
        }
        selector.relocate(xMiniatureOffset, yMiniatureOffset);

        // add click event
        selector.setOnMousePressed(selectorOnMousePressedEventHandler);
        selector.setOnMouseDragged(selectorOnMouseDraggedEventHandler);

        // resize selector on map wrapper resize
        mapWrapper.widthProperty().addListener((observable, oldValue, newValue) -> {
            double newSelectorWidth = newValue.doubleValue() * mapMiniatureTileWidth / Tile.getWidth();
            mapViewSelector.setWidth(newSelectorWidth < mapMiniatureWrapper.getWidth() ? newSelectorWidth : mapMiniatureWrapper.getWidth());
            // refresh selector - fixing position after window resize
            double offset = mapWrapper.getHvalue() * (mapMiniatureWrapper.getWidth() - mapViewSelector.getWidth());
            mapViewSelector.setTranslateX(offset);
        });

        mapWrapper.heightProperty().addListener((observable, oldValue, newValue) -> {
            double newSelectorHeight = newValue.doubleValue() * mapMiniatureTileHeight / Tile.getHeight();
            mapViewSelector.setHeight(newSelectorHeight < mapMiniatureWrapper.getHeight() ? newSelectorHeight : mapMiniatureWrapper.getHeight());
            // refresh selector - fixing position after window resize
            double offset = mapWrapper.getVvalue() * (mapMiniatureWrapper.getHeight() - mapViewSelector.getHeight());
            mapViewSelector.setTranslateY(offset);
        });
    }

    /* END OF SELECTOR MODULE */

    private void bindBoardScrollEvents() {
        // move selector on scroll view
        mapWrapper.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            double offset = newValue.doubleValue() * (mapMiniatureWrapper.getWidth() - mapViewSelector.getWidth() - 2 * xMiniatureOffset);
            mapViewSelector.setTranslateX(offset);
        });

        mapWrapper.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            double offset = newValue.doubleValue() * (mapMiniatureWrapper.getHeight() - mapViewSelector.getHeight() - 2 * yMiniatureOffset);
            mapViewSelector.setTranslateY(offset);
        });
    }

}
