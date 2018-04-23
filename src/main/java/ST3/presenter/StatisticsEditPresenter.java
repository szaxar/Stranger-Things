package ST3.presenter;

import ST3.model.Statistics;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StatisticsEditPresenter {

    private Statistics statistics;


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


    private Stage dialogStage;

    private boolean approved;

    @FXML
    public void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Statistics statistics) {
        this.statistics = statistics;
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
        statistics.setMaxHp(Integer.parseInt(maxHpTextField.getText()));
        statistics.setMaxMp(Integer.parseInt(maxMpTextField.getText()));
        statistics.setAgro(Integer.parseInt(agroTextField.getText()));
        statistics.setMorale(Integer.parseInt(moraleTextField.getText()));
        statistics.setAttack(Integer.parseInt(attackTextField.getText()));
        statistics.setDefence(Integer.parseInt(defenceTextField.getText()));
    }

    private void updateControls() {
        maxHpTextField.setText(String.valueOf(statistics.getMaxHp()));
        maxMpTextField.setText(String.valueOf(statistics.getMaxMp()));
        agroTextField.setText(String.valueOf(statistics.getAgro()));
        moraleTextField.setText(String.valueOf(statistics.getMorale()));
        attackTextField.setText(String.valueOf(statistics.getAttack()));
        defenceTextField.setText(String.valueOf(statistics.getDefence()));
    }
}
