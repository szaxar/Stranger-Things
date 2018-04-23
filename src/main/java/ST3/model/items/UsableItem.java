package ST3.model.items;

import ST3.model.creature.Creature;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by szaxar on 10.01.2018.
 */
public abstract class UsableItem extends Item {

    public UsableItem() {}

    public UsableItem(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    public abstract void use(Creature creature);

    @Override
    public void deepCopy(Item item){
        super.deepCopy(item);
    }
}
