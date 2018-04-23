package ST3.controller;

import ST3.Main;
import ST3.model.Statistics;
import ST3.model.creature.Creature;
import ST3.model.creature.Hero;
import ST3.model.items.EquipmentItem;
import ST3.presenter.ArmorEditPresenter;
import ST3.presenter.CreaturePresenter;
import ST3.presenter.StatisticsEditPresenter;
import ST3.presenter.StatisticsPresenter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreatureController {

    private Stage primaryStage;

    public CreatureController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public boolean showItemEditDialog(EquipmentItem equipmentItem) {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getClassLoader().getResource("view/ItemEditor.fxml"));
            BorderPane page = (BorderPane) loader.load();


            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit armor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the transaction into the presenter.
            ArmorEditPresenter armorEditDialogPresenter = loader.getController();
            armorEditDialogPresenter.setDialogStage(dialogStage);
            armorEditDialogPresenter.setData(equipmentItem);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return armorEditDialogPresenter.isApproved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean showStatisticsEditDialog(Statistics statistics) {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getClassLoader().getResource("view/StatisticsEditor.fxml"));
            BorderPane page = (BorderPane) loader.load();


            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit base statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the transaction into the presenter.
            StatisticsEditPresenter statisticsEditPresenter = loader.getController();
            statisticsEditPresenter.setDialogStage(dialogStage);
            statisticsEditPresenter.setData(statistics);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return statisticsEditPresenter.isApproved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Creature showCreatureEditDialog(Creature creature) {
        try {
            if(creature==null){
                creature = new Hero();
            }
            else{
                creature = creature.getCopy();
            }
            FXMLLoader loader = new FXMLLoader();
            FXMLLoader statisticsLoader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/CreatureEditor.fxml"));
            Parent page = loader.load();

            // set initial data into ST3.controller
            CreaturePresenter creaturePresenter = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit creature");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            creaturePresenter.setCreatureController(new CreatureController(dialogStage));
            creaturePresenter.setDialogStage(dialogStage);

            // set statistics presenter
            // loader
            statisticsLoader.setLocation(getClass().getClassLoader().getResource("view/StatisticsView.fxml"));
            Pane statisticsGroup = statisticsLoader.load();
            StatisticsPresenter statisticsPresenter = statisticsLoader.getController();

            creaturePresenter.setStatisticsPresenter(statisticsPresenter);
            creaturePresenter.setStatisticsGroup(statisticsGroup);


            creaturePresenter.setCreature(creature);

            dialogStage.showAndWait();

            return creaturePresenter.isApproved()?creaturePresenter.getCreature():null;
        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
            return null;
        }
    }


}
