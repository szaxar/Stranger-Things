package ST3.model.map;

import javafx.scene.image.ImageView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditableMap extends Map {

    private java.util.Map<String, ArrayList<ImageView>> tileImageViewMap = new HashMap<>();

    public EditableMap(int width, int height) {
        super(width, height);
    }

    public EditableMap(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    public void setTile(int x, int y, Tile tile) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        tiles[x][y] = tile;

        String key = Integer.toString(x) + "," + Integer.toString(y);
        if (tileImageViewMap.containsKey(key)) {
            for (ImageView imageView : tileImageViewMap.get(key)) {
                imageView.setImage(tile.getImage());
            }
        }
    }

    public void registerImageViewForTile(int x, int y, ImageView imageView) {
        String key = Integer.toString(x) + "," + Integer.toString(y);
        if (tileImageViewMap.containsKey(key)) {
            tileImageViewMap.get(key).add(imageView);
            return;
        }
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(imageView);
        tileImageViewMap.put(key, imageViews);
    }

}
