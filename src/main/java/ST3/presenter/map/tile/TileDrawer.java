package ST3.presenter.map.tile;

import ST3.model.map.EditableMap;
import ST3.model.map.Tile;
import ST3.presenter.map.Point;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class TileDrawer {

    private EditableMap editableMap;
    private AnchorPane boardContainer;
    private ComboBox<Tile> tileComboBox;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private ArrayList<Point> points = new ArrayList<>();
    private int minX, maxX, minY, maxY;

    public TileDrawer(EditableMap editableMap, AnchorPane boardContainer, ComboBox<Tile> tileComboBox) {
        this.editableMap = editableMap;
        this.boardContainer = boardContainer;
        this.tileComboBox = tileComboBox;
    }

    private final EventHandler<MouseEvent> onMousePressedEventHandler = (MouseEvent event) -> {
        canvas = new Canvas(boardContainer.getWidth(), boardContainer.getHeight());
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setLineWidth(1.0);
        graphicsContext.setFill(Color.BLACK);
        boardContainer.getChildren().add(canvas);
        maxX = maxY = 0;
        minX = (int) boardContainer.getWidth();
        minY = (int) boardContainer.getHeight();
    };

    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = (MouseEvent event) -> {
        int newX = (int) event.getX();
        int newY = (int) event.getY();

        if (newX > maxX) maxX = newX;
        if (newX < minX) minX = newX;
        if (newY > maxY) maxY = newY;
        if (newY < minY) minY = newY;

        if (points.isEmpty()) {
            points.add(new Point(newX, newY));
            return;
        }

        Point lastPoint = points.get(points.size() - 1);
        int lastX = lastPoint.x;
        int lastY = lastPoint.y;

        int parts = (Math.abs(newX - lastX) < Math.abs(newY - lastY) ? Math.abs(newX - lastX) : Math.abs(newY - lastY)) + 1;
        int xStep = (newX - lastX + (newX < lastX ? -1 : 1)) / parts;
        int yStep = (newY - lastY + (newY < lastY ? -1 : 1)) / parts;

        int x = lastX;
        int y = lastY;
        for (int i = 0; i < parts; i++) {
            if (Math.abs(xStep) == 1) {
                for (int yOffset = 0; yOffset < Math.abs(yStep); yOffset++) {
                    if (i != 0 || yOffset != 0) {
                        points.add(new Point(x, y));
                        graphicsContext.fillRect(x, y, 1, 1);
                    }
                    if (yStep < 0) {
                        y--;
                    } else {
                        y++;
                    }
                }
                x += xStep;
            } else { // abs yStep == 1
                for (int xOffset = 0; xOffset < Math.abs(xStep); xOffset++) {
                    if (i != 0 || xOffset != 0) {
                        points.add(new Point(x, y));
                        graphicsContext.fillRect(x, y, 1, 1);
                    }
                    if (xStep < 0) {
                        x--;
                    } else {
                        x++;
                    }
                }
                y += yStep;
            }
        }
    };

    private final EventHandler<MouseEvent> onMouseReleasedEventHandler = (MouseEvent event) -> {
        boardContainer.getChildren().remove(canvas);
        graphicsContext = null;
        canvas = null;

        if (points.size() == 0) { // single click
            int tileOffsetX = (int) ((event.getX() - Tile.getWidth() / 2) / Tile.getWidth());
            int tileOffsetY = (int) ((event.getY() - Tile.getHeight() / 2) / Tile.getHeight());
            Tile newTile = tileComboBox.getValue();
            editableMap.setTile(tileOffsetX, tileOffsetY, newTile);
            return;
        }

        removeLineFromStart();
        removeLineFromEnd();

        if (points.size() == 0) {
            return;
        }

        Polygon polygon = new Polygon();
        for (Point point : points) {
            polygon.getPoints().addAll((double) point.x, (double) point.y);
        }
        points.clear();

        int minTileX = (minX - (int) Tile.getWidth() / 2) / 100;
        int maxTileX = (maxX - (int) Tile.getWidth() / 2) / 100;
        int minTileY = (minY - (int) Tile.getHeight() / 2) / 100;
        int maxTileY = (maxY - (int) Tile.getHeight() / 2) / 100;
        if (minTileX < 0) minTileX = 0;
        if (maxTileX > editableMap.getWidth() - 1) maxTileX = editableMap.getWidth() - 1;
        if (minTileY < 0) minTileY = 0;
        if (maxTileY > editableMap.getHeight() - 1) maxTileY = editableMap.getHeight() - 1;

        Tile newTile = tileComboBox.getValue();
        Rectangle tileToCheck = new Rectangle(Tile.getWidth(), Tile.getHeight());
        double tileSurfaceArea = Tile.getWidth() * Tile.getHeight() / 2;
        for (int tileOffsetX = minTileX; tileOffsetX <= maxTileX; tileOffsetX++) {
            for (int tileOffsetY = minTileY; tileOffsetY <= maxTileY; tileOffsetY++) {
                tileToCheck.relocate(Tile.getWidth() / 2 + tileOffsetX * Tile.getWidth(), Tile.getHeight() / 2 + tileOffsetY * Tile.getHeight());
                Shape intersection = Shape.intersect(polygon, tileToCheck);
                intersection.setFill(Color.TRANSPARENT);
                boardContainer.getChildren().add(intersection);
                int intersectionSurfaceArea = (int) intersection.computeAreaInScreen();
                boardContainer.getChildren().remove(intersection);
                if (intersectionSurfaceArea > tileSurfaceArea) {
                    editableMap.setTile(tileOffsetX, tileOffsetY, newTile);
                }
            }
        }
    };

    private void removeLineFromStart() {
        while (true) {
            if (points.isEmpty()) {
                break;
            }
            Point point = points.get(0);
            if (isPointDoubled(point)) {
                break;
            }
            points.remove(0);
        }
    }

    private void removeLineFromEnd() {
        while (true) {
            if (points.isEmpty()) {
                break;
            }
            Point point = points.get(points.size() - 1);
            if (isPointDoubled(point)) {
                break;
            }
            points.remove(points.size() - 1);
        }
    }

    private boolean isPointDoubled(Point pointToFind) {
        for (Point point : points) {
            if (point == pointToFind) {
                continue;
            }
            if (point.equals(pointToFind)) {
                return true;
            }
        }
        return false;
    }

    public void enable() {
        boardContainer.setOnMousePressed(onMousePressedEventHandler);
        boardContainer.setOnMouseDragged(onMouseDraggedEventHandler);
        boardContainer.setOnMouseReleased(onMouseReleasedEventHandler);
    }

    public void disable() {
        boardContainer.setOnMousePressed(null);
        boardContainer.setOnMouseDragged(null);
        boardContainer.setOnMouseReleased(null);
    }

    public void resetTiles() {
        for (int x = 0; x < editableMap.getWidth(); x++) {
            for (int y = 0; y < editableMap.getHeight(); y++) {
                editableMap.setTile(x, y, Tile.GRASS);
            }
        }
    }

}