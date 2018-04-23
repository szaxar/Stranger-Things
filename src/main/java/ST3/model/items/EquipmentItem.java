package ST3.model.items;

import ST3.model.Statistics;
import org.json.JSONException;
import org.json.JSONObject;

public class EquipmentItem extends Item {

    private EquipmentItemType equipmentItemType;

    public EquipmentItemType getType() {
        return equipmentItemType;
    }

    public void setType(EquipmentItemType type) {
        equipmentItemType = equipmentItemType;
    }

    public EquipmentItem(EquipmentItemType itemType, String name, int weight, Statistics statistics) {
        this.equipmentItemType = itemType;
        super.name.setValue(name);
        super.weight.setValue(weight);
        super.statistics = statistics;
    }

    public EquipmentItem(EquipmentItemType type){
        this(type,"Some Item",0,new Statistics());
    }

    public EquipmentItem() {
        this(EquipmentItemType.ARMOR,"Some Armor",0,new Statistics());
    }

    public EquipmentItem(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        equipmentItemType = EquipmentItemType.valueByString(jsonObject.getString("equipmentItemType"));
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = super.toJSONObject();
        jsonObject.put("equipmentItemType", equipmentItemType);
        return jsonObject;
    }

}
