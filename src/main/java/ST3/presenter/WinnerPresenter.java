package ST3.presenter;

import ST3.model.creature.Creature;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WinnerPresenter {




    private Creature creature;
    private String text;
    @FXML
    private Label winnerId;

    @FXML
    private TextField textField;

    public void show(){
        winnerId.setText(creature.getName());
        textField.setText(text);
    }


    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    public void setText(String text){
        this.text=text;
    }
}
