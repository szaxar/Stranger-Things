package ST3.controller;

import ST3.Main;
import ST3.model.creature.Creature;
import ST3.model.creature.Enemy;
import ST3.model.creature.Hero;
import ST3.model.items.Item;
import ST3.presenter.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FightController {

    private Stage primaryStage;
    private FightPresenter fightPresenter;


    public FightController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Creature fight(Creature creature, Creature creature1) {
        try {
            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getClassLoader().getResource("view/FightOverview.fxml"));
            Parent page = loader.load();

            fightPresenter = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.setTitle("Edit creature");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);


            fightPresenter.setDialogStage(dialogStage);
            fightPresenter.setFightAppController(this);
            fightPresenter.setHero(creature);
            fightPresenter.setEnemy(creature1);

            fightPresenter.setRound();
            dialogStage.showAndWait();
        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
        if(fightPresenter.getWinner()!=null) {
            Creature winner = fightPresenter.getWinner();
            Creature looser;
            if (winner.equals(creature)) looser = creature1;
            else looser = creature;
            String text = update(winner, looser);
            showWinner(winner, text);
            return winner;
        }else {
            return null;
        }
    }


    public void showAbility(Creature creature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/AbilityView.fxml"));
            Parent page = loader.load();

            AbilityPresenter controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Show Ability");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            controller.setAppController(new FightController(dialogStage));
            controller.setFightPresenter(fightPresenter);
            controller.setDialogStage(dialogStage);

            controller.setData(creature.getAbilityList());

            dialogStage.showAndWait();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    public void showItem(Creature creature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ItemView.fxml"));
            Parent page = loader.load();

            ItemPresenter controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Show Items");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            controller.setAppController(new FightController(dialogStage));
            controller.setFightPresenter(fightPresenter);
            controller.setDialogStage(dialogStage);

            controller.setData(creature.getEquipment().getusableBackpack());

            dialogStage.showAndWait();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();

        }
    }


        public void showWinner(Creature winner,String loot){
            try {
                // load layout from FXML file
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getClassLoader().getResource("view/WinnerPresenter.fxml"));
                Parent page = loader.load();

                WinnerPresenter winnerPresenter = loader.getController();
                winnerPresenter.setCreature(winner);
                winnerPresenter.setText(loot);
                winnerPresenter.show();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Winner");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);


                dialogStage.showAndWait();
            } catch (IOException e) {
                // don't do this in common apps
                e.printStackTrace();
            }
        }


        public String update(Creature winner,Creature looser){
            String text=new String();
            List<Item> lootItem=new ArrayList<>();
            if(winner.getClass().equals(Hero.class)){
                if(looser.getClass().equals(Enemy.class)){
                    Hero tmp=(Hero)winner;
                    Enemy tmp2=(Enemy) looser;
                    tmp.addExp(tmp2.getExpToAdd());
                    text="Exp:"+Integer.toString(tmp2.getExpToAdd());

                    Random generator = new Random();

                    if(tmp2.getDropMap()!=null) tmp2.getDropMap().entrySet().stream().forEach(e->{
                        if(generator.nextDouble()%100<e.getValue()) {
                            lootItem.add(e.getKey());

                        }
                    });

                    text=text+" loot:";
                    for(int i=0;i<lootItem.size();i++){
                        tmp.getEquipment().addToBackpack(lootItem.get(i));
                        text=text+" "+lootItem.get(i).getName();
                    }
                }
                else{
                    Hero tmp=(Hero)winner;
                    Hero tmp2=(Hero) looser;
                    tmp.addExp(100);
                    text="Exp: 100";
                    text=text+" loot:";
                    for(int i=0;i<tmp2.getEquipment().getEquipmentBackpack().size();i++){
                        tmp.getEquipment().addToBackpack(tmp2.getEquipment().getEquipmentBackpack().get(i));
                        text=text+" "+tmp2.getEquipment().getEquipmentBackpack().get(i).getName();
                    }
                    for(int i=0;i<tmp2.getEquipment().getusableBackpack().size();i++){
                        tmp.getEquipment().addToBackpack(tmp2.getEquipment().getusableBackpack().get(i));
                        text=text+" "+tmp2.getEquipment().getusableBackpack().get(i).getName();
                    }
                }
            }

            return text;
        }


}
