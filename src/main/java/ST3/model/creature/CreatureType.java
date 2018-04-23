package ST3.model.creature;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum CreatureType {
    HERO,
    ENEMY,
    NPC;


    public StringProperty getType() {
        StringProperty stringProperty = new SimpleStringProperty();
        switch (this) {
            case HERO:
                stringProperty.setValue("hero");
                return stringProperty;
            case ENEMY:
                stringProperty.setValue("enemy");
                return stringProperty;
            case NPC:
                stringProperty.setValue("npc");
                return stringProperty;
        }
        return null;
    }
}
