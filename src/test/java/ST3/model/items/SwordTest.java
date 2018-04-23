package ST3.model.items;

import org.junit.Test;

/**
 * Created by Adm on 2017-12-31.
 */
public class SwordTest {

    @Test
    public void deepCopy() throws Exception {


        EquipmentItem sword = new EquipmentItem();
        sword.setName("S1");
        EquipmentItem sword2 = new EquipmentItem();


        sword2.deepCopy(sword);

        assert (sword.getName().equals(sword2.getName()));//name copied?
        //does deep copy override worked?
        //properties cant be same


    }

}