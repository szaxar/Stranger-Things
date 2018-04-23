package ST3.model;

import ST3.model.items.Equipment;
import ST3.model.items.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Adm on 2017-12-10.
 */
public class EquipmentTest {

    private Equipment equipment = new Equipment();
    private EquipmentItem armor;
    private int mHp=10,mMp=20,agro=30,mor=40,att=50,def=60;//armor statistics parameter
    @Before
    public void setUp() throws Exception {
        armor = new EquipmentItem(EquipmentItemType.ARMOR,"Magic armor", 50,
                new Statistics(mHp,mMp,agro,mor,att,def));
        boolean isExcThrown = false;
        try {
            equipment.setArmor(armor);
        }
        catch (NotInBackpackException e){
            isExcThrown=true;
        }
        assert(isExcThrown);
        equipment.addToBackpack(armor);
        equipment.setArmor(armor);
    }

    @Test
    public void getBackpack() throws Exception {
        EquipmentItem armor2 = new EquipmentItem(EquipmentItemType.ARMOR,"Armor 2",10,new Statistics());
        EquipmentItem armor3 = new EquipmentItem(EquipmentItemType.ARMOR,"Armor 3",20,new Statistics());
        equipment.addToBackpack(armor2);
        equipment.addToBackpack(armor3);
        assert(equipment.getEquipmentBackpack().contains(armor2));
        assert(equipment.getEquipmentBackpack().contains(armor3));
        equipment.removeFromBackpack(armor2);
        equipment.removeFromBackpack(armor3);
        assert(!equipment.getEquipmentBackpack().contains(armor2));
        assert(!equipment.getEquipmentBackpack().contains(armor3));
    }

    @Test
    public void getArmor() throws Exception {
        assert(equipment.getArmor()==armor);
        equipment.unsetArmor();
        assert(equipment.getArmor()==null);
    }

    @Test
    public void deepCopy() throws Exception {
        Equipment eq1 = new Equipment();
        EquipmentItem s1 = new EquipmentItem(EquipmentItemType.SWORD,"s1",1,new Statistics());
        EquipmentItem a1 = new EquipmentItem(EquipmentItemType.ARMOR,"a1",1,new Statistics());
        EquipmentItem s2 = new EquipmentItem(EquipmentItemType.SWORD,"s2",1,new Statistics());
        EquipmentItem a2 = new EquipmentItem(EquipmentItemType.ARMOR,"a2",1,new Statistics());
        eq1.addToBackpack(s1);
        eq1.addToBackpack(a1);
        eq1.addToBackpack(s2);
        eq1.addToBackpack(a2);
        eq1.setArmor(a1);
        eq1.setSword(s1);

        Equipment eq2 = new Equipment();
        eq2.deepCopy(eq1);

        assert(eq2!=eq1);

        EquipmentItem newA2 = null;
        EquipmentItem newS2 = null;
        for (Item item:eq2.getEquipmentBackpack()) {
            if(item.getName().equals("a2")){
                newA2=(EquipmentItem)item;
            }
            if(item.getName().equals("s2")){
                newS2=(EquipmentItem)item;
            }
        }
        assert(newA2!=null);
        assert(newS2!=null);//these objs need to be in our new equipment obj

        assert(newA2!=a2);
        assert(newA2.equals(a2));//cant be same object but need to have same value

        assert(newS2!=s2);
        assert(newS2.equals(s2));//cant be same object but need to have same value


        assert(eq1.getArmor().equals(eq2.getArmor()));
        assert(eq1.getArmor()!=eq2.getArmor());

        assert(eq1.getSword().equals(eq2.getSword()));
        assert(eq1.getSword()!=eq2.getSword());//same with equipped objs

    }

}