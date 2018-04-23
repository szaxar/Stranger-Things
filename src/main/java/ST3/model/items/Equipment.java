package ST3.model.items;


import ST3.utils.IJsoner;
import ST3.model.Statistics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Equipment implements IJsoner {

    private EquipmentItem armor = null;
    private EquipmentItem boots = null;
    private EquipmentItem helmet = null;
    private EquipmentItem legs = null;
    private EquipmentItem shield = null;
    private EquipmentItem sword = null;

    private int maxWeight = 0;//if 0 then infinity
    private int currentWeight = 0;
    private ObservableList<EquipmentItem> equipmentBackpack = FXCollections.observableArrayList();
    private ObservableList<UsableItem> usableBackpack = FXCollections.observableArrayList();
    private Statistics statistics = new Statistics();

    public Equipment(EquipmentItem armor, EquipmentItem boots, EquipmentItem helmet, EquipmentItem legs, EquipmentItem shield, EquipmentItem sword) {
        if (armor.getType().equals(EquipmentItemType.ARMOR)) this.armor = armor;
        if (boots.getType().equals(EquipmentItemType.BOOTS)) this.boots = boots;
        if (helmet.getType().equals(EquipmentItemType.HELMET)) this.helmet = helmet;
        if (legs.getType().equals(EquipmentItemType.LEGS)) this.legs = legs;
        if (shield.getType().equals(EquipmentItemType.SHIELD)) this.shield = shield;
        if (sword.getType().equals(EquipmentItemType.SWORD)) this.sword = sword;
    }

    public Equipment() {
    }

    public Equipment(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("armor")) armor = new EquipmentItem(jsonObject.getJSONObject("armor"));
        if (jsonObject.has("boots")) boots = new EquipmentItem(jsonObject.getJSONObject("boots"));
        if (jsonObject.has("helmet")) helmet = new EquipmentItem(jsonObject.getJSONObject("helmet"));
        if (jsonObject.has("legs")) legs = new EquipmentItem(jsonObject.getJSONObject("legs"));
        if (jsonObject.has("shield")) shield = new EquipmentItem(jsonObject.getJSONObject("shield"));
        if (jsonObject.has("sword")) sword = new EquipmentItem(jsonObject.getJSONObject("sword"));
        maxWeight = jsonObject.getInt("maxWeight");
        currentWeight = jsonObject.getInt("currentWeight");
        statistics = new Statistics(jsonObject.getJSONObject("statistics"));
        JSONArray equipmentBackpackJSON = jsonObject.getJSONArray("equipmentBackpack");
        for (int i = 0; i < equipmentBackpackJSON.length(); i++) {
            EquipmentItem equipmentItem = new EquipmentItem(equipmentBackpackJSON.getJSONObject(i));
            equipmentBackpack.add(equipmentItem);
        }
        JSONArray usableBackpackJSON = jsonObject.getJSONArray("usableBackpack");
        for (int i = 0; i < usableBackpackJSON.length(); i++) {
            UsableItem usableItem = new Potion(usableBackpackJSON.getJSONObject(i));
            usableBackpack.add(usableItem);
        }
    }

    public void deepCopy(Equipment equipment) {
        if (equipment == null) {
            return;
        }
        if (equipment.armor != null) {
            this.armor = new EquipmentItem();
            this.armor.deepCopy(equipment.
                    armor);
        }
        if (equipment.boots != null) {
            this.boots = new EquipmentItem();
            this.boots.deepCopy(equipment.
                    boots);
        }
        if (equipment.legs != null) {
            this.legs = new EquipmentItem();
            this.legs.deepCopy(equipment.
                    legs);
        }
        if (equipment.shield != null) {
            this.shield = new EquipmentItem();
            this.shield.deepCopy(equipment.
                    shield);
        }
        if (equipment.helmet != null) {
            this.helmet = new EquipmentItem();
            this.helmet.deepCopy(equipment.
                    helmet);
        }
        if (equipment.sword != null) {
            this.sword = new EquipmentItem();
            this.sword.deepCopy(equipment.
                    sword);
        }
        equipmentBackpack.clear();
        for (EquipmentItem item : equipment.equipmentBackpack) {
            this.equipmentBackpack.add((EquipmentItem) item.getCopy());
        }
        usableBackpack.clear();
        for (UsableItem item : equipment.usableBackpack) {
            this.usableBackpack.add((UsableItem) item.getCopy());
        }

    }

    public EquipmentItem getBoots() {
        return boots;
    }

    public void setBoots(EquipmentItem boots) throws NotInBackpackException {
        if (!equipmentBackpack.contains(boots)) {
            throw (new NotInBackpackException());
        }
        equipmentBackpack.remove(boots);//r
        unsetBoots();
        this.boots = boots;
    }

    public void unsetBoots() {
        if (boots != null)
            equipmentBackpack.add(boots);
        boots = null;

    }

    public EquipmentItem getHelmet() {
        return helmet;
    }

    public void setHelmet(EquipmentItem helmet) {
        if (!equipmentBackpack.contains(helmet)) {
            throw (new NotInBackpackException());
        }
        equipmentBackpack.remove(helmet);//r
        unsetHelmet();
        this.helmet = helmet;
    }

    public void unsetHelmet() {
        if (helmet != null)
            equipmentBackpack.add(helmet);
        helmet = null;
    }

    public EquipmentItem getLegs() {
        return legs;
    }

    public void setLegs(EquipmentItem legs) {
        if (!equipmentBackpack.contains(legs)) {
            throw (new NotInBackpackException());
        }
        equipmentBackpack.remove(legs);//r
        unsetLegs();
        this.legs = legs;
    }

    public void unsetLegs() {
        if (legs != null)
            equipmentBackpack.add(legs);
        legs = null;
    }

    public EquipmentItem getShield() {
        return shield;
    }

    public void setShield(EquipmentItem shield) {
        if (!equipmentBackpack.contains(shield)) {
            throw (new NotInBackpackException());
        }
        equipmentBackpack.remove(shield);//r
        unsetShield();
        this.shield = shield;
    }

    public void unsetShield() {
        if (shield != null)
            equipmentBackpack.add(shield);
        shield = null;
    }

    public EquipmentItem getSword() {
        return sword;
    }

    public void setSword(EquipmentItem sword) {
        if (!equipmentBackpack.contains(sword)) {
            throw (new NotInBackpackException());
        }
        equipmentBackpack.remove(sword);//r
        unsetSword();
        this.sword = sword;
    }

    public void unsetSword() {
        if (sword != null)
            equipmentBackpack.add(sword);
        sword = null;
    }

    public ObservableList<EquipmentItem> getEquipmentBackpack() {
        return equipmentBackpack;
    }

    public ObservableList<UsableItem> getusableBackpack() {
        return usableBackpack;
    }

    public EquipmentItem getArmor() {
        return armor;
    }

    public void setArmor(EquipmentItem armor) throws NotInBackpackException {
        if (!equipmentBackpack.contains(armor)) {
            throw (new NotInBackpackException());
        }
        equipmentBackpack.remove(armor);//r
        unsetArmor();
        this.armor = armor;
    }

    public Statistics getStatistics() {
        statistics = new Statistics();
        if (armor != null)
            statistics = statistics.add(armor.getStatistics());
        if (boots != null)
            statistics = statistics.add(boots.getStatistics());
        if (helmet != null)
            statistics = statistics.add(helmet.getStatistics());
        if (legs != null)
            statistics = statistics.add(legs.getStatistics());
        if (sword != null)
            statistics = statistics.add(sword.getStatistics());
        if (shield != null)
            statistics = statistics.add(shield.getStatistics());

        return statistics;
    }


    public void unsetArmor() {
        if (armor != null)
            equipmentBackpack.add(armor);
        armor = null;

    }

    public void addToBackpack(Item item) throws FullBackpackException {

        if (maxWeight == 0) {
            //it means maxWeight = infinity
        } else {
            if (currentWeight + item.getWeight() > maxWeight) {
                throw (new FullBackpackException());
            }
        }
        if (item.getClass().equals(EquipmentItem.class)) equipmentBackpack.add((EquipmentItem) item);
        else usableBackpack.add((UsableItem) item);

        currentWeight += item.getWeight();
    }

    public void removeFromBackpack(Item item) {
        if (!equipmentBackpack.contains(item) && !usableBackpack.contains(item)) {
            throw (new NotInBackpackException());
        }
       if(equipmentBackpack.contains(item)) equipmentBackpack.remove(item);
       if(usableBackpack.contains(item))  usableBackpack.remove(item);
        currentWeight -= item.getWeight();
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "armor=" + armor +
                ", boots=" + boots +
                ", helmet=" + helmet +
                ", legs=" + legs +
                ", shield=" + shield +
                ", sword=" + sword +
                ", maxWeight=" + maxWeight +
                ", currentWeight=" + currentWeight +
                ", backpack=" + equipmentBackpack +
                ", statistics=" + statistics +
                '}';
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject equipmentAsJSON = new JSONObject();
        if (armor != null) equipmentAsJSON.put("armor", armor.toJSONObject());
        if (boots != null) equipmentAsJSON.put("boots", boots.toJSONObject());
        if (helmet != null) equipmentAsJSON.put("helmet", helmet.toJSONObject());
        if (legs != null) equipmentAsJSON.put("legs", legs.toJSONObject());
        if (shield != null) equipmentAsJSON.put("shield", shield.toJSONObject());
        if (sword != null) equipmentAsJSON.put("sword", sword.toJSONObject());
        equipmentAsJSON.put("maxWeight", maxWeight);
        equipmentAsJSON.put("currentWeight", currentWeight);
        equipmentAsJSON.put("statistics", statistics.toJSONObject());
        JSONArray equipmentBackpackJSON = new JSONArray();
        for (EquipmentItem equipmentItem : equipmentBackpack) {
            equipmentBackpackJSON.put(equipmentItem.toJSONObject());
        }
        equipmentAsJSON.put("equipmentBackpack", equipmentBackpackJSON);
        JSONArray usableBackpackJSON = new JSONArray();
        for (UsableItem usableItem : usableBackpack) {
            usableBackpackJSON.put(usableItem.toJSONObject());
        }
        equipmentAsJSON.put("usableBackpack", usableBackpackJSON);
        return equipmentAsJSON;
    }

}
