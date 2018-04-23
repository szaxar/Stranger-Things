package ST3.model.creature;

import ST3.model.Statistics;
import ST3.model.items.Equipment;
import ST3.model.items.Potion;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.json.JSONException;
import org.json.JSONObject;

public class Hero extends Creature {

    private Proffesion proffesion;
    private IntegerProperty exp = new SimpleIntegerProperty();

    public IntegerProperty expProperty() {
        return exp;
    }

    public int getLvl() {
        return lvl.get();
    }

    public IntegerProperty lvlProperty() {
        return lvl;
    }

    private IntegerProperty lvl = new SimpleIntegerProperty();

    @Override
    public void deepCopy(Creature creature) {
        super.deepCopy(creature);
        Hero heroCreature = (Hero) creature;
        this.setProffesion(heroCreature.proffesion);
        this.exp.setValue(heroCreature.exp.getValue());
        this.lvl.setValue(heroCreature.lvl.getValue());
        this.setImage(heroCreature.getImagePath());
    }

    public Hero() {
        this(Proffesion.KNIGHT, "Test");
    }

    public Hero(Proffesion proffesion, String name) {
        super.name.setValue(name);

        this.setProffesion(proffesion);
        super.statistics = proffesion.getStatistics();
        super.hp.setValue(statistics.getMaxHp());
        super.mp.setValue(statistics.getMaxMp());
        this.lvl.setValue(1);
        this.exp.setValue(0);
        super.equipment = new Equipment();
        super.refreshFullStatistics();

        addInitialItems();
        super.creatureType = CreatureType.HERO;
    }

    public Hero(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        proffesion = Proffesion.valueOf(jsonObject.getString("proffesion"));
        exp.setValue(jsonObject.getInt("exp"));
        lvl.setValue(jsonObject.getInt("lvl"));
    }

    @Override
    public String toString() {
        return name.getValue();
    }

    public void setProffesion(Proffesion proffesion) {
        this.proffesion = proffesion;
        switch (proffesion) {
            case MAGE:
                setImage("Heroes/mage.jpg");
                break;
            case PRIEST:
                setImage("Heroes/priest.jpg");
                break;
            case SZAMAN:
                setImage("Heroes/szaman.jpg");
                break;
            case KNIGHT:
                setImage("Heroes/knight.jpg");
                break;
        }

        super.abilityList.clear();
        setAbility();
    }

    public Proffesion getProffesion() {
        return proffesion;
    }

    public int getExp() {
        return exp.getValue();
    }

    public void setExp(int exp) {
        this.exp.setValue(exp);
    }

    public void setLvl(int lvl) {
        this.lvl.set(lvl);
    }

    public void addExp(int exp) {
        this.exp.setValue(this.exp.getValue() + exp);
        lvlUp();
    }

    public void lvlUp() {
        while (this.exp.getValue() > 100) {
            lvl.setValue(lvl.getValue() + 1);
            exp.setValue(exp.getValue() - 100);
        }
    }

    private String getFilePath(String filename) {
        String path = Hero.class.getClassLoader().getResource("Heroes/" + filename).getPath();
        if (path.charAt(2) == ':') {
            path = path.substring(1);
        }

        return "file:" + path;
    }

    public void setAbility() {
        switch (this.proffesion) {
            case MAGE:
                super.abilityList.add(new Ability(new SimpleStringProperty("ice ball"),AbilityType.MAGIC_ATTACK,
                        new SimpleIntegerProperty(20),new SimpleStringProperty("lod"),
                        new SimpleIntegerProperty(50)));
                break;
            case PRIEST:
                super.abilityList.add(new Ability(new SimpleStringProperty("heal"),AbilityType.HEAL,
                        new SimpleIntegerProperty(30),new SimpleStringProperty("heal"),
                        new SimpleIntegerProperty(70)));
                break;
            case SZAMAN:
                super.abilityList.add(new Ability(new SimpleStringProperty("agility"),AbilityType.BUFF,
                        new SimpleIntegerProperty(20),new SimpleStringProperty("agility"),
                        new SimpleIntegerProperty(1)));
                break;
            case KNIGHT:
                super.abilityList.add(new Ability(new SimpleStringProperty("defence "),AbilityType.BUFF,
                        new SimpleIntegerProperty(20),new SimpleStringProperty("defence"),
                        new SimpleIntegerProperty(1)));
                break;
        }
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject heroAsJSON = super.toJSONObject();
        heroAsJSON.put("proffesion", proffesion.toString());
        heroAsJSON.put("exp", exp.getValue());
        heroAsJSON.put("lvl", lvl.getValue());
        return heroAsJSON;
    }

    public void addInitialItems(){

        Statistics statistics=new Statistics(100,01,1,1,1,1);
        Potion potion=new Potion(statistics,100,10,1,"potion");
        for(int i=0;i<5;i++) {
            super.equipment.addToBackpack(potion);
        }
    }
}


