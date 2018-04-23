package ST3.presenter.map;

import ST3.model.creature.Creature;

public class CreatureModelItem {

    private String filename;
    private Creature creature;

    public CreatureModelItem(Creature creature, String filename) {
        this.creature = creature;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Creature getCreature() {
        return creature;
    }

    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CreatureModelItem
                && filename.equals(((CreatureModelItem) object).filename)
                && creature.getClass().equals(((CreatureModelItem) object).creature.getClass());
    }

    @Override
    public String toString() {
        return creature.toString();
    }

}
