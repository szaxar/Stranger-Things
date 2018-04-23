package ST3.model.items;

import ST3.model.Statistics;
import org.junit.Test;

/**
 * Created by Adm on 2017-12-31.
 */
public class ArmorTest {
    @Test
    public void deepCopy() throws Exception {


        EquipmentItem armor = new EquipmentItem(EquipmentItemType.ARMOR,"Armor 1",1,new Statistics());
        Item armor2 = new EquipmentItem();

        armor2.deepCopy(armor);

        assert(armor2.getName().equals(armor.getName()));



    }

}