package ST3.presenter;

import ST3.model.Statistics;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by Adm on 2017-12-15.
 */
public class StatisticsPresenter {

    private Statistics statistics;

    @FXML
    private Label maxHpLabel;

    @FXML
    private Label maxMpLabel;

    @FXML
    private Label agroLabel;

    @FXML
    private Label moraleLabel;

    @FXML
    private Label attackLabel;

    @FXML
    private Label defenceLabel;

    @FXML
    private void initialize() {

    }

    public void setData(Statistics statistics) {
        this.statistics = statistics;
        maxHpLabel.textProperty().bind(statistics.getMaxHpProperty().asString());
        maxMpLabel.textProperty().bind(statistics.getMaxMpProperty().asString());
        moraleLabel.textProperty().bind(statistics.getMoraleProperty().asString());
        attackLabel.textProperty().bind(statistics.getAttackProperty().asString());
        agroLabel.textProperty().bind(statistics.getAgroProperty().asString());
        defenceLabel.textProperty().bind(statistics.getDefenceProperty().asString());
    }


}

