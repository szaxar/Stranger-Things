package ST3.model.creature;

import ST3.model.Statistics;
import ST3.model.items.Equipment;
import ST3.model.items.EquipmentItem;
import ST3.model.items.Item;
import ST3.model.items.Potion;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Enemy extends Creature {

    private ObservableMap<Item,Double> dropMap=FXCollections.observableHashMap();
    private IntegerProperty expToAdd=new SimpleIntegerProperty();

    @Override
    public void deepCopy(Creature creature) {
        super.deepCopy(creature);
//        for now copied enemy will have new dropmap
//        Enemy e = (Enemy) creature;
//        for (Map.Entry<Item, Double> pair:e.dropMap.entrySet()) {
//            Item newItem = pair.getKey().getCopy();
//            dropMap.put(newItem, pair.getValue().doubleValue());
//        }
    }


//    public Enemy(JSONObject jsonObject) throws JSONException {
//        super(jsonObject);
//        dropMap = FXCollections.emptyObservableMap();
//        super.creatureType=CreatureType.ENEMY;
////        JSONArray dropMapJSONArray = jsonObject.getJSONArray("dropMap");
////        for (int i = 0; i < dropMapJSONArray.length(); i++) {
////            JSONObject itemDoubleAsJSON = dropMapJSONArray.getJSONObject(i);
////            Item item = new Item(itemDoubleAsJSON.getJSONObject("item"));
////            dropMap.put(item, itemDoubleAsJSON.getDouble("probability"));
////        }
//    }

    public Enemy(String name,Statistics statistics,ObservableMap<Item,Double>dropMap,Equipment equipment,Integer expToAdd) {
        super.name.setValue(name);
        super.statistics = statistics;
        this.dropMap = dropMap;
        super.equipment = equipment;
        super.hp.setValue(statistics.getMaxHp());
        super.mp.setValue(statistics.getMaxMp());
        setImage();
        super.creatureType=CreatureType.ENEMY;
        this.expToAdd.setValue(expToAdd);
        setImage("Enemies/monster.jpg");
    }

    public Enemy(){
        this("Enemy",new Statistics(),FXCollections.observableHashMap(),new Equipment(),100);
    }

    public Enemy(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        expToAdd.setValue(jsonObject.getInt("expToAdd"));
        try {
            JSONArray dropMapJSONArray = jsonObject.getJSONArray("dropMap");
            for (int i = 0; i < dropMapJSONArray.length(); i++) {
                JSONObject itemDoubleAsJSON = dropMapJSONArray.getJSONObject(i);
                Class itemClass = Class.forName(itemDoubleAsJSON.getString("itemClass"));

                Item item;
                if (itemClass == EquipmentItem.class) {
                    item = new EquipmentItem(itemDoubleAsJSON.getJSONObject("item"));
                } else if (itemClass == Potion.class) {
                    item = new Potion(itemDoubleAsJSON.getJSONObject("item"));
                } else {
                    throw new ClassNotFoundException();
                }

                dropMap.put(item, itemDoubleAsJSON.getDouble("probability"));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JSONException("Class not found");
        }
    }

    public ObservableMap<Item, Double> getDropMap() {
        return dropMap;
    }

    public void setDropList(ObservableMap<Item, Double> dropMap) {
        this.dropMap = dropMap;
    }

    @Override
    public String toString() {
        return name.getValue();
    }

    public void setImage() {
        super.image = new javafx.scene.image.Image(getFilePath("monster.jpg"));
    }

    private String getFilePath(String filename) {
        String path = Enemy.class.getClassLoader().getResource("Enemies/" + filename).getPath();
        if (path.charAt(2) == ':') {
            path = path.substring(1);
        }

        return "file:" + path;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject enemyAsJSON = super.toJSONObject();
        enemyAsJSON.put("expToAdd", expToAdd.getValue());
        JSONArray dropMapAsJSON = new JSONArray();
        for (Map.Entry<Item, Double> itemDoubleEntry : dropMap.entrySet()) {
            JSONObject itemDoubleAsJSON = new JSONObject();
            Item item = itemDoubleEntry.getKey();
            itemDoubleAsJSON.put("itemClass", item.getClass().getName());
            itemDoubleAsJSON.put("item", item.toJSONObject());
            itemDoubleAsJSON.put("probability", itemDoubleEntry.getValue());
            dropMapAsJSON.put(itemDoubleAsJSON);
        }
        enemyAsJSON.put("dropMap", dropMapAsJSON);
        return enemyAsJSON;
    }

    public int getExpToAdd() {
        return expToAdd.get();
    }

    public IntegerProperty expToAddProperty() {
        return expToAdd;
    }
}
