package ST3.model;

import ST3.model.creature.Hero;
import ST3.model.creature.Proffesion;
import ST3.model.items.*;
import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;
public class HeroTest {

    @Before
    public void GraphicsTest(){
        JFXPanel jfxPanel = new JFXPanel();
    }

    @Test
    public void EquipTest(){

        Hero hero = new Hero(Proffesion.KNIGHT, "PlayerName");
        // System.out.println(hero);

        /*
        int mHp=10,mMp=20,agro=30,mor=40,att=50,def=60;//armor statistics parameter

        Armor armor = new Armor("Magic armor", 50,
                new Statistics(mHp,mMp,agro,mor,att,def));
*/
        Statistics stat =new Statistics(10,15,16,17,18,19);
        EquipmentItem armor = new EquipmentItem(EquipmentItemType.ARMOR,"Armor", 50, stat);
        EquipmentItem boots =new EquipmentItem(EquipmentItemType.BOOTS,"boots",15,stat);
        EquipmentItem helmet=new EquipmentItem(EquipmentItemType.HELMET,"helmet",70,stat);
        EquipmentItem legs=new EquipmentItem(EquipmentItemType.LEGS,"legs",120,stat);
        EquipmentItem shield=new EquipmentItem(EquipmentItemType.SHIELD,"shield",150,stat);
        EquipmentItem sword=new EquipmentItem(EquipmentItemType.SWORD,"sword",100,stat);

        // System.out.print(armor);
        hero.getEquipment().addToBackpack(armor);
        hero.getEquipment().addToBackpack(boots);
        hero.getEquipment().addToBackpack(helmet);
        hero.getEquipment().addToBackpack(legs);
        hero.getEquipment().addToBackpack(shield);
        hero.getEquipment().addToBackpack(sword);

        //System.out.println("Before armor:");
        //System.out.println(hero.getFullStatistics());
        //hero.getEquipment().showBackpack();

        Statistics preArmorStats = hero.getFullStatistics();

        //setting armor
        hero.getEquipment().setArmor(armor);
        hero.getEquipment().setBoots(boots);
        hero.getEquipment().setHelmet(helmet);
        hero.getEquipment().setLegs(legs);
        hero.getEquipment().setShield(shield);
        hero.getEquipment().setSword(sword);
        //hero.getEquipment().showBackpack();

        Statistics afterArmorStats;
        afterArmorStats = hero.getFullStatistics();

        //assert that after armor has stats more than pre armor
        assert(afterArmorStats.getAgro()==preArmorStats.getAgro()+stat.getAgro()*6);
        assert(afterArmorStats.getMaxHp()==preArmorStats.getMaxHp()+stat.getMaxHp()*6);
        assert(afterArmorStats.getMaxMp()==preArmorStats.getMaxMp()+stat.getMaxMp()*6);
        assert(afterArmorStats.getAttack()==preArmorStats.getAttack()+stat.getAttack()*6);
        assert(afterArmorStats.getDefence()==preArmorStats.getDefence()+stat.getDefence()*6);
        assert(afterArmorStats.getMorale()==preArmorStats.getMorale()+stat.getMorale()*6);

        //unset armor
        hero.getEquipment().unsetArmor();
        hero.getEquipment().unsetHelmet();
        hero.getEquipment().unsetBoots();
        hero.getEquipment().unsetLegs();
        hero.getEquipment().unsetSword();
        hero.getEquipment().unsetShield();

        afterArmorStats = hero.getFullStatistics();

        //assert that after armor has same stats now
        assert(afterArmorStats.getAgro()==preArmorStats.getAgro());
        assert(afterArmorStats.getMaxHp()==preArmorStats.getMaxHp());
        assert(afterArmorStats.getMaxMp()==preArmorStats.getMaxMp());
        assert(afterArmorStats.getAttack()==preArmorStats.getAttack());
        assert(afterArmorStats.getDefence()==preArmorStats.getDefence());
        assert(afterArmorStats.getMorale()==preArmorStats.getMorale());



        //System.out.println(hero);
    }
    @Test
    public void PotionTest(){

        Hero hero = new Hero(Proffesion.KNIGHT, "PlayerName");
        hero.setHp(50);
        hero.setMp(5);
        int hpBefore=hero.getHp();
        int mpBefore=hero.getMp();
        Potion potion=new Potion(10,10);
        hero.getEquipment().addToBackpack(potion);
        hero.useItem(potion);
        assert(hpBefore+potion.getHealth()==hero.getHp());
        assert(mpBefore+potion.getMana()==hero.getMp());
    }

}
