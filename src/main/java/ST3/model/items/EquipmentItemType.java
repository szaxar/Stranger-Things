package ST3.model.items;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public enum EquipmentItemType {
    ARMOR,
    BOOTS,
    HELMET,
    LEGS,
    SHIELD,
    SWORD;

    @Override
    public String toString() {
        return WordUtils.capitalize(StringUtils.lowerCase(this.name()));
    }

    public static EquipmentItemType valueByString(String name) {
        for (EquipmentItemType equipmentItemType : EquipmentItemType.values()) {
            if (equipmentItemType.toString().equals(name)) {
                return equipmentItemType;
            }
        }
        return null;
    }
}
