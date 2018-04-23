package ST3.presenter;

import ST3.controller.FightController;
import ST3.model.creature.Ability;
import ST3.model.creature.Creature;
import ST3.model.items.UsableItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.stage.Stage;

import java.util.Random;

/**
 * Created by szaxar on 22.12.2017.
 */
public class FightPresenter {

    private Stage dialogStage;
    private Creature hero;
    private FightController fightAppController;
    private Creature enemy;
    private Boolean cancel=false;
    private Creature attacker;
    private Creature opponent;


    private final Integer missValue=3;
    private final Integer hitValue=7;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEnemy(Creature enemy) {
        this.enemy = enemy;
        enemyName.setText(enemy.getName());
        enemyHp.setText((Integer.toString(enemy.getHp())));
        enemyMp.setText(Integer.toString(enemy.getMp()));
        enemyView.setImage(enemy.getImage());
    }

    public void setHero(Creature hero) {

        this.hero = hero;
        heroName.setText(hero.getName());
        heroHp.setText((Integer.toString(hero.getHp())));
        heroMp.setText(Integer.toString(hero.getMp()));
        heroView.setImage(hero.getImage());
    }


    public void updateStatistics() {
        heroHp.setText((Integer.toString(hero.getHp())));
        heroMp.setText(Integer.toString(hero.getMp()));

        enemyHp.setText((Integer.toString(enemy.getHp())));
        enemyMp.setText(Integer.toString(enemy.getMp()));
    }


    public void setFightAppController(FightController fightAppController) {
        this.fightAppController = fightAppController;
    }

    public void setRound() {
        if (hero.getFullStatistics().getMoraleProperty().getValue() > enemy.getFullStatistics().getMoraleProperty().getValue()) {
            attacker = hero;
            opponent = enemy;
        } else {
            attacker = enemy;
            opponent = hero;
        }
        playerName.setText(attacker.getName());
    }

    public void nextRound() {


        Creature tmp = attacker;
        attacker = opponent;
        opponent = tmp;


        playerName.setText(attacker.getName());
    }


    public Creature getCreature() {
        return attacker;
    }

    public void setCreature(Creature attacker) {
        this.attacker = attacker;
    }


    @FXML
    private Label playerName;

    @FXML
    private Label enemyHp;

    @FXML
    private Label enemyMp;

    @FXML
    private Label heroHp;

    @FXML
    private Label heroMp;

    @FXML
    private Label heroName;

    @FXML
    private Label enemyName;

    @FXML
    private ImageView heroView;

    @FXML
    private ImageView enemyView;

    @FXML
    private Label logID;

    @FXML
    private void initialize() {

    }


    @FXML
    public void handleAbilityAction(ActionEvent event) {
        fightAppController.showAbility(attacker);
    }


    @FXML
    public void handleItemAction(ActionEvent event) {
        fightAppController.showItem(attacker);
    }

    @FXML
    public void handleCancelAction(ActionEvent event) {
        dialogStage.close();
        cancel=true;
    }


    @FXML
    public void handleAttackAction(ActionEvent event) {
        Random generator = new Random();
        int damage;
        int random = generator.nextInt(14);
        int defence;

        defence = opponent.getFullStatistics().getDefence();


        if(random<missValue) damage=0;
        else if(random<hitValue) {
            if(attacker.getFullStatistics().getAttack()/2-defence>0){
                damage=attacker.getFullStatistics().getAttack()/2-defence;
            }
            else damage=0;
        }
            else {
            if(attacker.getFullStatistics().getAttack()-defence>0){
                damage=attacker.getFullStatistics().getAttack()-defence;
            }
            else damage=0;
        }

        opponent.setHp(opponent.getHp() - damage);

        logID.setText(attacker.getName()+" wylosowal "+random+" i " +
                "zadal "+damage+"\n"+logID.getText());

        updateStatistics();

        if (opponent.getHp() < 0) {
            logID.setText("Wygral " + hero.getName() + "\n" + logID.getText());
        }
        if (!check()) nextRound();

    }


    public void useAbility(Ability ability) {
        if (ability.use(attacker, opponent)) {
            logID.setText(attacker.getName() + " uzyl " + ability.getAbilityName() + "\n" + logID.getText());
            updateStatistics();
            if (!check()) nextRound();
        } else {
            logID.setText(attacker.getName() + " nie ma many na " + ability.getAbilityName() + "\n" + logID.getText());
        }

    }

    public void useItem(UsableItem item) {


        attacker.useItem(item);
        logID.setText(attacker.getName() + " uzyl " + item.getName() + "\n" + logID.getText());
        updateStatistics();
        if (!check()) nextRound();


    }

    public boolean check(){
        if(opponent.getHp()<=0) {
            dialogStage.close();
            return true;
        } else return false;
    }

    public Creature getWinner() {

        if(cancel==false)return attacker;
        else return null;
    }

}