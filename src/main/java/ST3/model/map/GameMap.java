package ST3.model.map;

import org.json.JSONException;
import org.json.JSONObject;

public class GameMap extends EditableMap {

    public GameMap(int width, int height) {
        super(width, height);
    }

    public GameMap(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

}
