package ST3.model.creature;

import ST3.model.Statistics;
import org.json.JSONException;
import org.json.JSONObject;

public class NPC extends Creature {

    public NPC() {
        this("NPC");
    }

    public NPC(String name) {
        super.name.setValue(name);
        super.statistics = new Statistics(100, 100);
        super.setHp(100);
        super.setMp(100);
        super.creatureType=CreatureType.NPC;
        setImage("Npcs/npc.png");
    }

    public NPC(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }
    @Override
    public String toString() {
        return name.getValue();
    }
}
