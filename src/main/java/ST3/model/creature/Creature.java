package ST3.model.creature;

import ST3.utils.IJsoner;
import ST3.model.Statistics;
import ST3.model.items.Equipment;
import ST3.model.items.NotInBackpackException;
import ST3.model.items.UsableItem;
import ST3.utils.ResourcesUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Creature implements IJsoner {

    protected IntegerProperty hp = new SimpleIntegerProperty();
    protected IntegerProperty mp = new SimpleIntegerProperty();
    protected Statistics statistics = new Statistics();
    protected Statistics fullStatistics = new Statistics();
    protected StringProperty name=new SimpleStringProperty();
    protected ObservableList<Ability> abilityList= FXCollections.observableArrayList();
    protected Image image = null;
    protected boolean doKeepImage = false;

    public boolean keepImage(){
        return doKeepImage;
    }

    public void setKeepImage(boolean bol){
        doKeepImage =bol;
    }

    public String getImagePath() {
        return imagePath;
    }

    private String imagePath = null;
    protected Equipment equipment = new Equipment();
    protected CreatureType creatureType = CreatureType.NPC;

    public Creature() {
    }

    public CreatureType getType(){
        return this.creatureType;
    }

    public Creature(JSONObject jsonObject) throws JSONException {
        hp.setValue(jsonObject.getInt("hp"));
        mp.setValue(jsonObject.getInt("mp"));
        name.setValue(jsonObject.getString("name"));
        statistics = new Statistics(jsonObject.getJSONObject("statistics"));
        fullStatistics = new Statistics(jsonObject.getJSONObject("fullStatistics"));
        equipment = new Equipment(jsonObject.getJSONObject("equipment"));
        JSONArray abilityListAsJSONArray = jsonObject.getJSONArray("abilityList");
        for (int i = 0; i < abilityListAsJSONArray.length(); i++) {
            abilityList.add(new Ability(new JSONObject(abilityListAsJSONArray.get(i))));
        }
        creatureType = CreatureType.valueOf(jsonObject.getString("creatureType"));
        setImage(jsonObject.getString("imagePath"));
        try {
            doKeepImage = jsonObject.getBoolean("keepImage");
        }
        catch (JSONException e){
            doKeepImage = false;
        }
    }

    public Creature getCopy() {
        Creature creature = null;
        switch (creatureType){
            case ENEMY:
                creature = new Enemy();
                break;
            case HERO:
                creature = new Hero();
                break;
            case NPC:
                creature = new NPC();
                break;
        }
        creature.deepCopy(this);
        return creature;
    }

    public void deepCopy(Creature creature) {
        if (creature == null) {
            return;
        }
        this.hp.setValue(creature.getHp());
        this.mp.setValue(creature.getMp());
        this.statistics.setValue(creature.getStatistics());
        this.name.setValue(creature.getName());
        this.equipment.deepCopy(creature.equipment);
        this.doKeepImage =creature.doKeepImage;
        this.imagePath=creature.getImagePath();
        this.image = creature.getImage();
    }

    public void refreshFullStatistics() {
        this.fullStatistics.setValue(this.statistics.add(equipment.getStatistics()));
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String imagePath) {
        if(doKeepImage){
            return;
        }
        this.imagePath = imagePath;
        boolean isGood = true;
        try {
            this.image = new Image(ResourcesUtils.getResourcePath(imagePath));
        }
        catch (NullPointerException e){
            isGood= false;
        }
        if(!isGood){
            this.image = new Image(imagePath);
        }
    }

    public int getHp() {
        return hp.getValue();
    }

    public void setHp(int hp) {
        this.hp.setValue(hp);
    }

    public int getMp() {
        return mp.getValue();
    }

    public void setMp(int mp) {
        this.mp.setValue(mp);
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Statistics getFullStatistics() {
        refreshFullStatistics();
        return this.fullStatistics;
    }

    public void heal(Integer value) {
        if (hp.getValue() + value > statistics.getMaxHp()) {
            hp.setValue(statistics.getMaxHp());
        } else {
            hp.setValue(hp.getValue() + value);
        }

    }

    public void regenerateMana(Integer value) {
        if (mp.getValue() + value > statistics.getMaxMp()) {
            mp.setValue(statistics.getMaxMp());
        } else {
            mp.setValue(mp.getValue() + value);
        }
    }

    public ObservableList<Ability> getAbilityList() {
        return abilityList;
    }

    public void setAbilityList(ObservableList<Ability> abilityList) {
        this.abilityList = abilityList;
    }

    public void addAbility(Ability ability) {
        abilityList.add(ability);
    }

    public void useItem(UsableItem item) {
        if (!getEquipment().getusableBackpack().contains(item)) {
            throw (new NotInBackpackException());
        }
        getEquipment().getusableBackpack().remove(item);
        item.use(this);

    }

    public StringProperty getNameProperty() {
        return name;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject creatureAsJSON = new JSONObject();
        creatureAsJSON.put("hp", hp.getValue());
        creatureAsJSON.put("mp", mp.getValue());
        creatureAsJSON.put("name", name.getValue());
        creatureAsJSON.put("statistics", statistics.toJSONObject());
        creatureAsJSON.put("fullStatistics", fullStatistics.toJSONObject());
        creatureAsJSON.put("equipment", equipment.toJSONObject());
        JSONArray abilityListAsJSONArray = new JSONArray();
        for (Ability ability : abilityList) {
            abilityListAsJSONArray.put(ability.toJSONObject());
        }
        creatureAsJSON.put("abilityList", abilityListAsJSONArray);
        creatureAsJSON.put("imagePath", imagePath);
        creatureAsJSON.put("creatureType", creatureType);
        creatureAsJSON.put("keepImage",doKeepImage);
        return creatureAsJSON;
    }

}
