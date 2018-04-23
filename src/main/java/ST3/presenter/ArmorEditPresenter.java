package ST3.presenter;

import ST3.model.Statistics;
import ST3.model.items.EquipmentItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ArmorEditPresenter {

    private EquipmentItem armor;

    @FXML
    private TextField itemNameTextField;


    @FXML
    private TextField typeTextField;

    @FXML
    private TextField maxHpTextField;

    @FXML
    private TextField maxMpTextField;

    @FXML
    private TextField agroTextField;

    @FXML
    private TextField moraleTextField;

    @FXML
    private TextField attackTextField;

    @FXML
    private TextField defenceTextField;

    @FXML
    private TextField weightTextField;

    private Stage dialogStage;

    private boolean approved;

    @FXML
    public void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(EquipmentItem armor) {
        this.armor = armor;
        updateControls();
    }

    public boolean isApproved() {
        return approved;
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        if (isInputValid()) {
            updateModel();
            approved = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    private boolean isInputValid() {
        //maybe do later
        return true;
    }

    private void updateModel() {
        armor.setName(itemNameTextField.getText());
        armor.setWeight(Integer.parseInt(weightTextField.getText()));

        Statistics statistics = new Statistics();

        statistics.setMaxHp(Integer.parseInt(maxHpTextField.getText()));
        statistics.setMaxMp(Integer.parseInt(maxMpTextField.getText()));
        statistics.setAgro(Integer.parseInt(agroTextField.getText()));
        statistics.setMorale(Integer.parseInt(moraleTextField.getText()));
        statistics.setAttack(Integer.parseInt(attackTextField.getText()));
        statistics.setDefence(Integer.parseInt(defenceTextField.getText()));

        armor.setStatistics(statistics);
    }

    private void updateControls() {

        Statistics statistics = armor.getStatistics();
        maxHpTextField.setText(String.valueOf(statistics.getMaxHp()));
        maxMpTextField.setText(String.valueOf(statistics.getMaxMp()));
        agroTextField.setText(String.valueOf(statistics.getAgro()));
        moraleTextField.setText(String.valueOf(statistics.getMorale()));
        attackTextField.setText(String.valueOf(statistics.getAttack()));
        defenceTextField.setText(String.valueOf(statistics.getDefence()));

        itemNameTextField.setText(String.valueOf(armor.getName()));
        weightTextField.setText(String.valueOf(armor.getWeight()));
    }
}
