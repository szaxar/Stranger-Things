package ST3.model.items;

import ST3.model.creature.Creature;
import ST3.model.Statistics;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.json.JSONException;
import org.json.JSONObject;


public class Potion extends UsableItem {
    private IntegerProperty health = new SimpleIntegerProperty();
    private IntegerProperty mp = new SimpleIntegerProperty();

    public Potion(Statistics statistics, int health, int mp, int weight, String name) {
        super.statistics = statistics;
        this.health.setValue(health);
        this.mp.setValue(mp);
        super.name.setValue(name);
        super.weight.setValue(weight);
    }

    public Potion() {
        this(new Statistics(), 10, 10, 1, "Potion");
    }

    public Potion(int health, int mp) {
        this(new Statistics(), health, mp, 1, "Potion");
    }

    public Potion(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        health.setValue(jsonObject.getInt("health"));
        mp.setValue(jsonObject.getInt("mp"));
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public void use(Creature creature) {
        creature.heal(health.getValue());
        creature.regenerateMana(mp.getValue());
    }

    public int getHealth() {
        return health.getValue();
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    public void setHealth(int health) {
        this.health.set(health);
    }

    public int getMana() {
        return mp.getValue();
    }

    public IntegerProperty manaProperty() {
        return mp;
    }

    public void setMana(int mana) {
        this.mp.set(mana);
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = super.toJSONObject();
        jsonObject.put("health", health.getValue());
        jsonObject.put("mp", mp.getValue());
        return jsonObject;
    }

    @Override
    public Item getCopy() {
        Item item = new Potion();
        item.deepCopy(this);
        return item;
    }

    @Override
    public void deepCopy(Item item) {
        super.deepCopy(item);
        this.health.setValue(((Potion)item).getHealth());
        this.mp.setValue(((Potion)item).getMana());
    }


}
