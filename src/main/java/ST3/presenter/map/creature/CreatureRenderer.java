package ST3.presenter.map.creature;

import ST3.model.creature.Creature;
import ST3.model.creature.Enemy;
import ST3.model.creature.Hero;
import ST3.model.creature.NPC;
import ST3.model.map.Map;
import ST3.model.map.Tile;
import ST3.presenter.map.CreatureModelItem;
import ST3.presenter.map.Point;
import com.sun.istack.internal.NotNull;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class CreatureRenderer {

    private static final double imageScale = 0.8;

    protected Map map;
    protected AnchorPane boardContainer;
    protected ListView<Creature> creatureList;
    protected HashMap<Hero, Group> heroViewHashMap = new HashMap<>();
    protected HashMap<Enemy, Group> enemyViewHashMap = new HashMap<>();
    protected HashMap<NPC, Group> npcViewHashMap = new HashMap<>();

    public CreatureRenderer(Map map, AnchorPane boardContainer, ListView<Creature> creatureList) {
        this.map = map;
        this.boardContainer = boardContainer;
        this.creatureList = creatureList;

        for (java.util.Map.Entry<Hero, Point> entry : map.getHeroPointEntrySet()) {
            addCreatureToBoard(entry.getKey(), entry.getValue());
        }
        for (java.util.Map.Entry<Enemy, Point> entry : map.getEnemyPointEntrySet()) {
            addCreatureToBoard(entry.getKey(), entry.getValue());
        }
        for (java.util.Map.Entry<NPC, Point> entry : map.getNpcPointEntrySet()) {
            addCreatureToBoard(entry.getKey(), entry.getValue());
        }
    }

    public void addCreatureToBoard(Creature creature, Point point) {
        renderCreature(creature, point);
        creatureList.getItems().add(creature);
        sortCreatureList();
    }

    protected void renderCreature(Creature creature, Point point) {
        if (creature instanceof Hero) {
            renderCreature(heroViewHashMap, (Hero) creature, point);
        } else if (creature instanceof Enemy) {
            renderCreature(enemyViewHashMap, (Enemy) creature, point);
        } else if (creature instanceof NPC) {
            renderCreature(npcViewHashMap, (NPC) creature, point);
        }
    }

    private <CreatureType extends Creature> void renderCreature(HashMap<CreatureType, Group> creatureViewHashMap, CreatureType creature, Point point) {
        Group view = createView(creature);
        view.relocate(Tile.getWidth() / 2 + point.x * Tile.getWidth(), Tile.getHeight() / 2 + point.y * Tile.getHeight());
        creatureViewHashMap.put(creature, view);
        boardContainer.getChildren().add(view);
    }

    protected Group createView(@NotNull Creature creature) {
        Group view = new Group();

        Label name = new Label(creature.getName());
        name.setFont(new Font(Tile.getHeight() * (1.0 - imageScale) * 0.75));
        name.setMinWidth(Tile.getWidth());
        name.setMaxWidth(Tile.getWidth());
        name.setAlignment(Pos.CENTER);
        view.getChildren().add(name);

        Rectangle background = new Rectangle(Tile.getWidth(), Tile.getHeight());
        background.setFill(Color.TRANSPARENT);
        view.getChildren().add(background);

        ImageView imageView = new ImageView(creature.getImage());
        imageView.setFitWidth(Tile.getWidth() * imageScale);
        imageView.setFitHeight(Tile.getHeight() * imageScale);
        imageView.setTranslateX(Tile.getWidth() * (1.0 - imageScale) / 2);
        imageView.setTranslateY(Tile.getHeight() * (1.0 - imageScale));
        view.getChildren().add(imageView);

        double radiusX = Tile.getWidth() * imageScale / 2;
        double radiusY = Tile.getHeight() * imageScale / 2;
        Ellipse ellipse = new Ellipse(radiusX, radiusY);
        ellipse.setCenterX(radiusX);
        ellipse.setCenterY(radiusY);
        imageView.setClip(ellipse);

        return view;
    }

    protected void moveCreature(Creature creature, int x, int y) {
        Group view = getCreatureView(creature);
        view.relocate(Tile.getWidth() / 2 + (double) x * Tile.getWidth(), Tile.getHeight() / 2 + (double) y * Tile.getHeight());
        map.moveCreature(creature, x, y);
    }

    public Group getCreatureView(Creature creature) {
        Group view = null;
        if (creature instanceof Hero) {
            view = heroViewHashMap.get(creature);
        } else if (creature instanceof Enemy) {
            view = enemyViewHashMap.get(creature);
        } else if (creature instanceof NPC) {
            view = npcViewHashMap.get(creature);
        }
        return view;
    }

    public void removeCreatureFromBoard(Creature creature) {
        Group oldView = getCreatureView(creature);
        boardContainer.getChildren().remove(oldView);
        if (creature instanceof Hero) {
            heroViewHashMap.remove(creature);
        } else if (creature instanceof Enemy) {
            enemyViewHashMap.remove(creature);
        } else if (creature instanceof NPC) {
            npcViewHashMap.remove(creature);
        }
        creatureList.getItems().remove(creature);
        map.removeCreature(creature);
    }

    private void sortCreatureList() {
        creatureList.getItems().sort((Creature a, Creature b) -> {
            if (!a.getClass().equals(b.getClass())) {
                if (a instanceof Hero) return -1;
                if (b instanceof Hero) return 1;
                if (a instanceof Enemy) return -1;
                if (b instanceof Enemy) return 1;
            }

            return a.getName().compareTo(b.getName());
        });
    }

    public ArrayList<Hero> getHeroArrayList() {
        return new ArrayList<>(heroViewHashMap.keySet());
    }

}
