package ST3.presenter;


import ST3.controller.CreatureController;
import ST3.model.Statistics;
import ST3.model.creature.*;
import ST3.model.items.EquipmentItem;
import ST3.model.items.EquipmentItemType;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;

//TODO add creature statistics and creature type edit option

public class CreaturePresenter {


    @FXML
    public TextField playerNameTextField;
    @FXML
    public TextField hpTextField;
    @FXML
    public TextField mpTextField;
    private Creature creature;
    private CreatureController creatureController;
    @FXML
    private TableView<EquipmentItem> itemTable;

    @FXML
    private TableColumn<EquipmentItem, String> itemNameColumn;

    @FXML
    private TableColumn<EquipmentItem, Integer> weightColumn;

    @FXML
    private ComboBox<Proffesion> professionComboBox = new ComboBox<>();

    @FXML
    private ComboBox<CreatureType> creatureTypeComboBox = new ComboBox<>();

    @FXML
    private TableColumn<EquipmentItem, Integer> maxHpColumn;

    @FXML
    private TableColumn<EquipmentItem, Integer> agroColumn;

    @FXML
    private TableColumn<EquipmentItem, Integer> maxMpColumn;

    @FXML
    private TableColumn<EquipmentItem, Integer> moraleColumn;

    @FXML
    private TableColumn<EquipmentItem, Integer> attackColumn;

    @FXML
    private TableColumn<EquipmentItem, Integer> defenceColumn;

    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button editStatisticsButton;
    @FXML
    private Button setOwnImageButton;


    @FXML
    private TextField lvlTextField;

    @FXML
    private TextField expTextField;


    @FXML
    private Group statisticsGroup;

    private boolean approved;
    private Stage dialogStage;
    @FXML
    private Pane heroPane;
    @FXML
    private Pane enemyPane;
    @FXML
    private Pane npcPane;

    private StatisticsPresenter statisticsPresenter;
    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        itemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        itemNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getNameProperty());
        weightColumn.setCellValueFactory(dataValue -> dataValue.getValue().getWeightProperty().asObject());

        maxHpColumn.setCellValueFactory(dataValue -> dataValue.getValue().getStatistics().getMaxHpProperty().asObject());
        maxMpColumn.setCellValueFactory(dataValue -> dataValue.getValue().getStatistics().getMaxMpProperty().asObject());
        agroColumn.setCellValueFactory(dataValue -> dataValue.getValue().getStatistics().getAgroProperty().asObject());
        moraleColumn.setCellValueFactory(dataValue -> dataValue.getValue().getStatistics().getMoraleProperty().asObject());
        attackColumn.setCellValueFactory(dataValue -> dataValue.getValue().getStatistics().getAttackProperty().asObject());
        defenceColumn.setCellValueFactory(dataValue -> dataValue.getValue().getStatistics().getDefenceProperty().asObject());

        deleteButton.disableProperty().bind(Bindings.isEmpty(itemTable.getSelectionModel().getSelectedItems()));
        editButton.disableProperty().bind(Bindings.isEmpty(itemTable.getSelectionModel().getSelectedItems()));

        professionComboBox.setItems(FXCollections.observableArrayList(Proffesion.values()));

        creatureTypeComboBox.setItems(FXCollections.observableArrayList(CreatureType.values()));

        keepImageCheckBox.setOnAction((event) -> {
            boolean keepImage = keepImageCheckBox.isSelected();
            this.creature.setKeepImage(keepImage);
        });
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        creature.getEquipment().getEquipmentBackpack().remove(itemTable.getSelectionModel().getSelectedItem());
    }


    @FXML
    private void handleEditAction(ActionEvent event) {
        EquipmentItem armor = itemTable.getSelectionModel().getSelectedItem();
        if (armor != null) {
            creatureController.showItemEditDialog(armor);
        }
        itemTable.refresh();
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        EquipmentItem armor = new EquipmentItem(EquipmentItemType.ARMOR,"New Armor",0,new Statistics());
        if (creatureController.showItemEditDialog(armor)) {
            itemTable.getItems().add(armor);
        }
    }

    @FXML
    private CheckBox keepImageCheckBox;

    @FXML
    private void handleSetImage(ActionEvent event) {
        FileChooser fc = new FileChooser();
//        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files"));
        File f = fc.showOpenDialog(null);

        if (f!=null){
            //System.out.println(f.getAbsolutePath());
            try {
                creature.setImage(f.toURI().toURL().toExternalForm());
                imageView.setImage(creature.getImage());
            }
            catch (MalformedURLException e){
                System.out.println("Malformed exception!");
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void handleEquipAction(ActionEvent event) {
        //TODO in future new window showing full eq with item types for equip and unequip
        EquipmentItem armor = itemTable.getSelectionModel().getSelectedItem();
        if (armor != null) {
            creature.getEquipment().setArmor(armor);
            creature.refreshFullStatistics();
        }
        itemTable.refresh();
    }


    @FXML
    private void handleUnEquipAction(ActionEvent event) {
        EquipmentItem armor = creature.getEquipment().getArmor();
        if(armor == null){
            return;
        }
        creature.getEquipment().unsetArmor();
        itemTable.refresh();
    }

    @FXML
    private void handleEditStatistics(ActionEvent event) {
        Statistics statistics = this.creature.getStatistics().getCopy();
        if(creatureController.showStatisticsEditDialog(statistics)){
            this.creature.getStatistics().setValue(statistics);
            this.creature.refreshFullStatistics();
        }
    }


    private HashMap<TextField, ChangeListener<? super Boolean>> fieldFocusedListenerMap = new HashMap<>();

    private void setFieldAction(TextField field, EventHandler handler){
        if(fieldFocusedListenerMap.containsKey(field)){//if given textfield had previous handler:
            field.focusedProperty().removeListener(fieldFocusedListenerMap.get(field));
        }

        field.setOnAction(handler);

        ChangeListener<? super Boolean> listener=((event, oldVal, newVal) -> {
            if(field.isFocused()){
                //System.out.println("is focused");
            }
            else{
//                System.out.println("lost focus");
                handler.handle(new Event(null,null,null));
            }
        });

        field.focusedProperty().addListener(listener);
        fieldFocusedListenerMap.put(field,listener);
    }



    private ChangeListener<? super CreatureType> creatureTypeListener = null;

    public void setCreature(Creature creature) {
        this.creature=creature;
        keepImageCheckBox.setSelected(this.creature.keepImage());
        itemTable.setItems(this.creature.getEquipment().getEquipmentBackpack());

        playerNameTextField.textProperty().bindBidirectional(this.creature.getNameProperty());

        hpTextField.setText(String.valueOf(this.creature.getHp()));
        setFieldAction(hpTextField,(event ->{
            this.creature.setHp(Integer.parseInt(hpTextField.getText()));
//            System.out.println("hom many times this is handled?");
        }
        ));
        mpTextField.setText(String.valueOf(this.creature.getMp()));
        setFieldAction(mpTextField,(event -> this.creature.setMp(Integer.parseInt(mpTextField.getText()))));

        if (creatureTypeListener != null){
            creatureTypeComboBox.valueProperty().removeListener(creatureTypeListener);
        }
        creatureTypeListener = ((observableValue, oldType, newType) -> {
            if(oldType!=null && oldType!=newType) {
                Creature tmp = null;
                switch (newType) {
                    case HERO:
                        tmp = new Hero();
                        break;
                    case NPC:
                        tmp = new NPC();
                        break;
                    case ENEMY:
                        tmp = new Enemy();
                        break;
                }
                tmp.setHp(this.creature.getHp());
                tmp.setMp(this.creature.getMp());
                tmp.setStatistics(this.creature.getStatistics());
                tmp.setName(this.creature.getName());
                tmp.setEquipment(this.creature.getEquipment());
                //tmp.setImage(this.creature.getImagePath());
                if(this.creature.keepImage()) {
                    tmp.setKeepImage(false);
                    tmp.setImage(this.creature.getImagePath());
                    tmp.setKeepImage(this.creature.keepImage());
                }
                setCreature(tmp);
            }
        });
        creatureTypeComboBox.valueProperty().addListener(creatureTypeListener);

        setCreatureType(this.creature.getType());
        creatureTypeComboBox.getSelectionModel().select(this.creature.getType());


        statisticsPresenter.setData(this.creature.getFullStatistics());



        imageView.setImage(creature.getImage());
    }

    private void setCreatureType(CreatureType type){
        switch (type) {
            case HERO:
                setHero();
                break;
            case NPC:
                break;
            case ENEMY:
                break;
        }
        heroPane.setVisible(type==CreatureType.HERO);
        enemyPane.setVisible(type==CreatureType.ENEMY);
        npcPane.setVisible(type==CreatureType.NPC);
    }

    private ChangeListener<? super Proffesion> professionListener = null;

    private void setHero() {
        Hero hero = ((Hero)(this.creature));
        professionComboBox.getSelectionModel().select(hero.getProffesion());

        if(professionListener!=null){
            professionComboBox.valueProperty().removeListener(professionListener);
        }

        professionListener=((observableValue, oldProf, newProf) -> {
            if(oldProf!=null && newProf!=oldProf) {
                hero.setProffesion(newProf);
                if(!creature.keepImage()) {
                    switch (newProf) {
                        case MAGE:
                            hero.setImage("Heroes/mage.jpg");
                            break;
                        case PRIEST:
                            hero.setImage("Heroes/priest.jpg");
                            break;
                        case SZAMAN:
                            hero.setImage("Heroes/szaman.jpg");
                            break;
                        case KNIGHT:
                            hero.setImage("Heroes/knight.jpg");
                            break;
                    }
                    imageView.setImage(creature.getImage());
                }
            }
        });

        professionComboBox.valueProperty().addListener(professionListener);

        lvlTextField.setText(String.valueOf(hero.getLvl()));
        setFieldAction(lvlTextField,(event -> {
            hero.setLvl(Integer.parseInt(lvlTextField.getText()));
//            System.out.println(hero.getLvl());
        }));
        expTextField.setText(String.valueOf(hero.getExp()));
        setFieldAction(expTextField,(event -> hero.setExp(Integer.parseInt(expTextField.getText()))));
    }


    public void setStatisticsGroup(Node statisticsNode) {
        this.statisticsGroup.getChildren().add(statisticsNode);
    }

    public void setCreatureController(CreatureController creatureController) {
        this.creatureController = creatureController;
    }

    public boolean isApproved() {
        return approved;
    }

    @FXML
    public void handleCancelAction(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleAcceptAction(ActionEvent event) {
        approved = true;
        dialogStage.close();
    }


    public Creature getCreature(){
        return this.creature;
    }

    public void setStatisticsPresenter(StatisticsPresenter statisticsPresenter) {
        this.statisticsPresenter = statisticsPresenter;
    }
}
