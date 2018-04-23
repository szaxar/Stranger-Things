package ST3.model.creature;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum AbilityType {
    HEAL,
    BUFF,
    MAGIC_ATTACK,
    PHYSICAL_ATTACK;

   private StringProperty stringProperty = new SimpleStringProperty();

    public StringProperty getName() {

        switch (this) {
            case HEAL:
                stringProperty.setValue("HEAL");
                break;
            case BUFF:
                stringProperty.setValue("BUFF");
                break;
            case MAGIC_ATTACK:
                stringProperty.setValue("MAGIC ATTACK");
                break;
            case PHYSICAL_ATTACK:
                stringProperty.setValue("PHYSICAL ATTACK");
                break;
        }
        return stringProperty;
    }
}
