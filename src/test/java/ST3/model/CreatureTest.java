package ST3.model;

import ST3.model.creature.Creature;
import ST3.model.creature.Hero;
import ST3.model.creature.Proffesion;
import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Adm on 2017-12-31.
 */
public class CreatureTest {
    @Before
    public void initGraphics() throws Exception{
        JFXPanel jfxPanel = new JFXPanel();
    }

    @Test
    public void getCopy() throws Exception {
        Creature creature1 = new Hero(Proffesion.KNIGHT,"c1");
        creature1.setHp(1337);
        creature1.setMp(1773);

        Creature creature2 = creature1.getCopy();

        assert(creature1!=creature2);
        assert(creature1.getNameProperty()!=creature2.getNameProperty());
        assert(creature1.getName().equals(creature2.getName()));//diff properties but same value
        assert(creature2.getHp()==1337);
        assert(creature2.getMp()==1773);
    }

}