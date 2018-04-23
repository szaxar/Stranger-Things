package ST3.model;

import ST3.utils.IJsoner;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.json.JSONException;
import org.json.JSONObject;

public class Statistics implements IJsoner {

    private IntegerProperty maxHp = new SimpleIntegerProperty();
    private IntegerProperty maxMp = new SimpleIntegerProperty();
    private IntegerProperty agro = new SimpleIntegerProperty();
    private IntegerProperty morale = new SimpleIntegerProperty();
    private IntegerProperty attack = new SimpleIntegerProperty();
    private IntegerProperty defence = new SimpleIntegerProperty();

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Statistics))
            return false;
        Statistics s = (Statistics) object;
        if (maxHp.get() != s.maxHp.get())
            return false;
        if (maxMp.get() != s.maxMp.get())
            return false;
        if (agro.get() != s.agro.get())
            return false;
        if (morale.get() != s.morale.get())
            return false;
        if (attack.get() != s.attack.get())
            return false;
        if (defence.get() != s.defence.get())
            return false;
        return true;
    }

    public Statistics getCopy(){
        return this.add(null);
    }

    public Statistics(int maxHp, int maxMp, int agro, int morale,
                      int attack, int defence) {
        this.maxHp.setValue(maxHp);
        this.maxMp.setValue(maxMp);
        this.agro.setValue(agro);
        this.morale.setValue(morale);
        this.attack.setValue(attack);
        this.defence.setValue(defence);
    }

    public Statistics(JSONObject jsonObject) throws JSONException {
        maxHp.setValue(jsonObject.getInt("maxHp"));
        maxMp.setValue(jsonObject.getInt("maxMp"));
        agro.setValue(jsonObject.getInt("agro"));
        morale.setValue(jsonObject.getInt("morale"));
        attack.setValue(jsonObject.getInt("attack"));
        defence.setValue(jsonObject.getInt("defence"));
    }

    public Statistics() {
        this(0, 0, 0, 0, 0, 0);
    }

    public Statistics(int maxHp, int maxMp) {
        this.maxHp.setValue(maxHp);
        this.maxMp.setValue(maxMp);
    }

    public int getAgro() {
        return agro.getValue();
    }

    public int getAttack() {
        return attack.getValue();
    }

    public int getDefence() {
        return defence.getValue();
    }

    public int getMaxHp() {
        return maxHp.getValue();
    }

    public int getMaxMp() {
        return maxMp.getValue();
    }

    public int getMorale() {
        return morale.getValue();
    }


    public IntegerProperty getMaxHpProperty() {
        return maxHp;
    }

    public IntegerProperty getMaxMpProperty() {
        return maxMp;
    }

    public IntegerProperty getAgroProperty() {
        return agro;
    }

    public IntegerProperty getMoraleProperty() {
        return morale;
    }

    public IntegerProperty getAttackProperty() {
        return attack;
    }

    public IntegerProperty getDefenceProperty() {
        return defence;
    }


    public void setMaxHp(int maxHp) {
        this.maxHp.setValue(maxHp);
    }

    public void setMaxMp(int maxMp) {
        this.maxMp.setValue(maxMp);
    }

    public void setAgro(int agro) {
        this.agro.setValue(agro);
    }

    public void setMorale(int morale) {
        this.morale.setValue(morale);
    }

    public void setAttack(int attack) {
        this.attack.setValue(attack);
    }

    public void setDefence(int defence) {
        this.defence.setValue(defence);
    }

    public Statistics add(Statistics statistics) {
        if(statistics==null){
            return new Statistics(maxHp.get(),maxMp.get(),agro.get(),morale.get(),attack.get(),defence.get());
        }
        return new Statistics(maxHp.getValue() + statistics.getMaxHp(),
                maxMp.getValue() + statistics.getMaxMp(),
                agro.getValue() + statistics.getAgro(),
                morale.getValue() + statistics.getMorale(),
                attack.getValue() + statistics.getAttack(),
                defence.getValue() + statistics.getDefence());
    }

    public void setValue(Statistics statistics) {
        this.maxHp.setValue(statistics.getMaxHp());
        this.maxMp.setValue(statistics.getMaxMp());
        this.morale.setValue(statistics.getMorale());
        this.agro.setValue(statistics.getAgro());
        this.attack.setValue(statistics.getAttack());
        this.defence.setValue(statistics.getDefence());
    }

    public String toString() {
        String tmp = "Statistics: maxHp " + maxHp.get() + " maxMp " + maxMp.get() + " agro " + agro.get() +
                " morale " + morale.get() + " attack " + attack.get() + " defence " + defence.get();
        return tmp;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject statisticsAsJSON = new JSONObject();
        statisticsAsJSON.put("maxHp", maxHp.getValue());
        statisticsAsJSON.put("maxMp", maxMp.getValue());
        statisticsAsJSON.put("agro", agro.getValue());
        statisticsAsJSON.put("morale", morale.getValue());
        statisticsAsJSON.put("attack", attack.getValue());
        statisticsAsJSON.put("defence", defence.getValue());
        return statisticsAsJSON;
    }

}

