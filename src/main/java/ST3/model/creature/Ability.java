package ST3.model.creature;


import ST3.utils.IJsoner;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import org.json.JSONException;
import org.json.JSONObject;

public class Ability implements IJsoner {
    private StringProperty abilityName=new SimpleStringProperty();
    private AbilityType abilityType;
    private ObservableValue<Integer> manaCost=new SimpleObjectProperty<>();
    private StringProperty description=new SimpleStringProperty();
    private ObservableValue<Integer> value=new SimpleObjectProperty();

    public Ability(StringProperty abilityName, AbilityType abilityType, ObservableValue manaCost, StringProperty description, ObservableValue value) {
        this.abilityName = abilityName;
        this.abilityType = abilityType;
        this.manaCost = manaCost;
        this.description = description;
        this.value = value;
    }

    public Ability(JSONObject jsonObject) {
        //
    }

    public boolean use(Creature creature1, Creature creature2) {
        switch (abilityType) {
            case MAGIC_ATTACK:
                if (creature1.getMp() > manaCost.getValue()) {
                    creature2.setHp(creature2.getHp() - value.getValue());
                    creature1.setMp(creature1.getMp() - manaCost.getValue());
                    return true;
                } else return false;
            case HEAL:
                if (creature1.getMp() > manaCost.getValue()) {
                    creature1.heal(value.getValue());
                    creature1.setMp(creature1.getMp() - manaCost.getValue());
                    return true;
                } else return false;

            case PHYSICAL_ATTACK:
                if (creature1.getMp() > manaCost.getValue()) {
                    creature2.setHp(creature2.getHp() - value.getValue());
                    creature1.setMp(creature1.getMp() - manaCost.getValue());
                    return true;
                } else return false;

            case BUFF:
                return true;
        }
        return true;
    }

    public String getAbilityName() {
        return abilityName.get();
    }

    public StringProperty abilityNameProperty() {
        return abilityName;
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public ObservableValue manaCostProperty() {
        return manaCost;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public ObservableValue valueProperty() {
        return value;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject abilityAsJSON = new JSONObject();
        abilityAsJSON.put("abilityName",abilityName.getValue());
        abilityAsJSON.put("abilityType",abilityType.getName());
        abilityAsJSON.put("manaCost",manaCostProperty().getValue());
        abilityAsJSON.put("description",description.getValue());
        abilityAsJSON.put("value",value.getValue());
        return abilityAsJSON;
    }
}
