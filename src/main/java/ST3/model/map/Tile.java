package ST3.model.map;

import ST3.utils.ResourcesUtils;
import javafx.scene.image.Image;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;

public enum Tile {

    GRASS,
    GROUND,
    WATER;

    private static final double width = 100;
    private static final double height = 100;
    private HashMap<Tile, Image> images = new HashMap<>();

    public Image getImage() {
        if (images.containsKey(this)) {
            return images.get(this);
        }

        Image image;
        switch (this) {
            case GRASS:
                image = new Image(ResourcesUtils.getTilePath("grass.png"));
                images.put(this, image);
                return image;
            case GROUND:
                image = new Image(ResourcesUtils.getTilePath("ground.jpg"));
                images.put(this, image);
                return image;
            case WATER:
                image = new Image(ResourcesUtils.getTilePath("water.jpg"));
                images.put(this, image);
                return image;
        }
        return null;
    }

    public static double getWidth() {
        return width;
    }

    public static double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return WordUtils.capitalize(StringUtils.lowerCase(this.name()));
    }

    public static Tile valueByName(String name) {
        for (Tile tile : Tile.values()) {
            if (tile.toString().equals(name)) {
                return tile;
            }
        }
        return null;
    }

}
