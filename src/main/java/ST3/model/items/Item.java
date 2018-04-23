package ST3.model.items;

import ST3.utils.IJsoner;
import ST3.model.Statistics;
import javafx.beans.property.*;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Item implements IJsoner {

    protected Statistics statistics = new Statistics();//TODO change to separate interface or abstract class maybe
    protected IntegerProperty weight = new SimpleIntegerProperty();
    protected StringProperty name = new SimpleStringProperty();

    public Item() {}

    public Item(JSONObject jsonObject) throws JSONException {
        statistics = new Statistics(jsonObject.getJSONObject("statistics"));
        weight.setValue(jsonObject.getInt("weight"));
        name.setValue(jsonObject.getString("name"));
    }

    public Item getCopy() {
        Item item = null;
        try {
            item = this.getClass().newInstance();
            item.deepCopy(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Item)) {
            return false;
        }
        Item itemObj = (Item) object;
        if (this.weight.get() != itemObj.weight.get()) return false;
        if (!this.name.get().equals(itemObj.name.get())) return false;
        if (!this.statistics.equals(itemObj.statistics)) return false;
        return true;
    }

    public void deepCopy(Item item) {
        if (item == null) {
            return;
        }
        this.statistics.setValue(item.statistics);
        this.name.setValue(item.name.getValue());
        this.weight.setValue(item.weight.getValue());
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public int getWeight() {
        return weight.getValue();
    }

    public IntegerProperty getWeightProperty() {
        return this.weight;
    }

    public StringProperty getNameProperty() {
        return this.name;
    }

    public void setWeight(int weight) {
        this.weight.setValue(weight);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    @Override
    public String toString() {
        return "ST3.model.items.Item{" +
                "weight=" + weight.getValue() +
                ", name='" + name.getValue() + '\'' +
                '}';
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject itemAsJSON = new JSONObject();
        itemAsJSON.put("statistics", statistics.toJSONObject());
        itemAsJSON.put("weight", weight.getValue());
        itemAsJSON.put("name", name.getValue());
        return itemAsJSON;
    }

}
