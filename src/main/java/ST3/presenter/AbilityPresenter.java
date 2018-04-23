package ST3.presenter;

import ST3.controller.FightController;
import ST3.model.creature.Ability;
import ST3.model.creature.AbilityType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class AbilityPresenter {

    private Stage dialogStage;
    private FightController appController;
    private FightPresenter fightPresenter;

    @FXML
    private TableView<Ability> abilityTable;


    @FXML
    private TableColumn<Ability, String> abilityNameColumn;

    @FXML
    private TableColumn<Ability, Integer> manaCostColumn;

    @FXML
    private TableColumn<Ability, String> abilityTypeColumn;

    @FXML
    private TableColumn<Ability, Integer> valueColumn;

    @FXML
    private TableColumn<Ability, String> descriptionColumn;



    @FXML
    public void initialize() {
        abilityTable.setEditable(true);

        abilityTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        abilityNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().abilityNameProperty());
        manaCostColumn.setCellValueFactory(dataValue -> dataValue.getValue().manaCostProperty());

        abilityTypeColumn.setCellValueFactory(dataValue -> dataValue.getValue().getAbilityType().getName());
        valueColumn.setCellValueFactory(dataValue -> dataValue.getValue().valueProperty());
        descriptionColumn.setCellValueFactory(dataValue -> dataValue.getValue().descriptionProperty());

    }

    @FXML
    private void handleUseAction(ActionEvent event) {
        if (abilityTable.getSelectionModel().getSelectedItem() != null)
            fightPresenter.useAbility(abilityTable.getSelectionModel().getSelectedItem());
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAppController(FightController appController) {
        this.appController = appController;
    }

    public void setFightPresenter(FightPresenter fightPresenter) {
        this.fightPresenter = fightPresenter;
    }

    public void setData(ObservableList<Ability> abilitiesList) {
        abilityTable.setItems(abilitiesList);
    }
}

