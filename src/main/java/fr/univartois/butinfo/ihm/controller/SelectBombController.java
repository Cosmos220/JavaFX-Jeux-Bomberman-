package fr.univartois.butinfo.ihm.controller;

import fr.univartois.butinfo.ihm.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SelectBombController {
    
     ;
    @FXML
    private ListView<AbstractBomb> listViewBombs;
    
    @FXML
    private ImageView selectedBombImage;

    @FXML
    private Label selectedBombName;

    @FXML
    private TextArea selectedBombDescription;

    private Stage dialogStage;
    private MaFacadeBomberman facade;
    private AbstractBomb selectedBomb;
    private boolean validerClicked = false;
    
    @FXML
    public void initialize() {
        listViewBombs.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> updateBombDetails(newVal));
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setFacade(MaFacadeBomberman facade) {
        this.facade = facade;

        ObservableList<AbstractBomb> observableBombs = FXCollections.observableArrayList();
        listViewBombs.setItems(facade.getTypeBomb());

    }
    
    private void updateBombDetails(AbstractBomb bomb) {
        if (bomb != null) {
            selectedBomb = bomb;
            selectedBombName.setText(bomb.getName());
            selectedBombDescription.setText(bomb.getDescription());
            String imagePath = "/images/" + bomb.getName() + ".png";
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            selectedBombImage.setImage(image);
        }
    }

    @FXML
    public void handleValider() {
            trierlist(listViewBombs.getSelectionModel().getSelectedItem());
            validerClicked = true;
            dialogStage.close();
    }

    public void trierlist(AbstractBomb bomb ){
        if(bomb instanceof Bomb) {
            facade.trierlist(Bomb.class);
        }
        else if (bomb instanceof RowBomb){
            facade.trierlist(RowBomb.class);

        }
        else if ( bomb instanceof ColumnBomb){
            facade.trierlist(ColumnBomb.class);

        }
        else if (bomb instanceof LargeBomb){
            facade.trierlist(LargeBomb.class);

        }
    }




    @FXML
    private void handleAnnuler() {
        dialogStage.close();
    }

    public AbstractBomb getSelectedBomb() {
        return selectedBomb;
    }

    public boolean isValiderClicked() {
        return validerClicked;
    }
}