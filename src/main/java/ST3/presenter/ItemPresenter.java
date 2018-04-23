package ST3.presenter;

import ST3.controller.FightController;
import ST3.model.items.UsableItem;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ItemPresenter {

    private Stage dialogStage;
    private FightController appController;
    private FightPresenter fightPresenter;

    @FXML
    private TableView<UsableItem> itemTable;
    @FXML
    private TableColumn<UsableItem, String> itemNameColumn;

    @FXML
    public void initialize() {
        itemTable.setEditable(true);
        itemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        itemNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getNameProperty());
    }

    @FXML
    private void handleUseAction(ActionEvent event) {
        if (itemTable.getSelectionModel().getSelectedItem() != null)
            fightPresenter.useItem(itemTable.getSelectionModel().getSelectedItem());
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

    public void setData(ObservableList<UsableItem> itemsList) {
        itemTable.setItems(itemsList);
    }
}