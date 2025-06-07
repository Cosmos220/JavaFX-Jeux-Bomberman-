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

/**
 * La classe SelectBombController gère la fenêtre de sélection des types de bombes.
 * Cette classe contrôle l'interface permettant au joueur de choisir quel type de bombe
 * utiliser parmi celles disponibles dans son inventaire.
 *
 * @author DAVI Loick
 * @version 1.0.0
 */
public class SelectBombController {

    /**
     * La ListView contenant la liste des bombes disponibles pour la sélection.
     */
    @FXML
    private ListView<AbstractBomb> listViewBombs;

    /**
     * L'ImageView affichant l'image de la bombe actuellement sélectionnée.
     */
    @FXML
    private ImageView selectedBombImage;

    /**
     * Le Label affichant le nom de la bombe actuellement sélectionnée.
     */
    @FXML
    private Label selectedBombName;

    /**
     * La TextArea affichant la description de la bombe actuellement sélectionnée.
     */
    @FXML
    private TextArea selectedBombDescription;

    /**
     * La fenêtre de dialogue (Stage) contenant cette interface.
     */
    private Stage dialogStage;

    /**
     * La facade permettant l'accès aux données et fonctionnalités du modèle.
     */
    private MaFacadeBomberman facade;

    /**
     * La bombe actuellement sélectionnée par l'utilisateur.
     */
    private AbstractBomb selectedBomb;

    /**
     * Indicateur booléen pour savoir si l'utilisateur a cliqué sur "Valider".
     */
    private boolean validerClicked = false;

    /**
     * Méthode d'initialisation automatiquement appelée après le chargement du fichier FXML.
     * Configure les listeners pour détecter les changements de sélection dans la ListView.
     */
    @FXML
    public void initialize() {
        listViewBombs.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateBombDetails(newVal));
    }

    /**
     * Définit la fenêtre de dialogue (Stage) pour ce contrôleur.
     *
     * @param dialogStage la fenêtre de dialogue à associer
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Définit la facade et initialise la liste des bombes disponibles.
     *
     * @param facade la facade du modèle Bomberman contenant les données des bombes
     */
    public void setFacade(MaFacadeBomberman facade) {
        this.facade = facade;

        ObservableList<AbstractBomb> observableBombs = FXCollections.observableArrayList();
        listViewBombs.setItems(facade.getTypeBomb());
    }

    /**
     * Met à jour les détails affichés pour la bombe sélectionnée.
     * Cette méthode actualise le nom, la description et l'image de la bombe sélectionnée.
     *
     * @param bomb la bombe dont les détails doivent être affichés, peut être null
     */
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

    /**
     * Gestionnaire d'événement pour le bouton "Valider".
     * Trie la liste des bombes selon le type sélectionné, marque la validation comme effectuée
     * et ferme la fenêtre de dialogue.
     */
    @FXML
    public void handleValider() {
        trierlist(listViewBombs.getSelectionModel().getSelectedItem());
        validerClicked = true;
        dialogStage.close();
    }

    /**
     * Trie la liste des bombes selon le type de la bombe sélectionnée.
     * Cette méthode identifie le type de bombe et appelle la méthode de tri appropriée
     * dans la facade.
     *
     * @param bomb la bombe dont le type détermine le critère de tri
     */
    public void trierlist(AbstractBomb bomb) {
        if(bomb instanceof Bomb) {
            facade.trierlist(Bomb.class);
        }
        else if (bomb instanceof RowBomb){
            facade.trierlist(RowBomb.class);
        }
        else if (bomb instanceof ColumnBomb){
            facade.trierlist(ColumnBomb.class);
        }
        else if (bomb instanceof LargeBomb){
            facade.trierlist(LargeBomb.class);
        }
    }

    /**
     * Gestionnaire d'événement pour le bouton "Annuler".
     * Ferme la fenêtre de dialogue sans valider la sélection.
     */
    @FXML
    private void handleAnnuler() {
        dialogStage.close();
    }

    /**
     * Retourne la bombe actuellement sélectionnée.
     *
     * @return la bombe sélectionnée, ou null si aucune bombe n'est sélectionnée
     */
    public AbstractBomb getSelectedBomb() {
        return selectedBomb;
    }

    /**
     * Indique si l'utilisateur a cliqué sur le bouton "Valider".
     *
     * @return true si le bouton "Valider" a été cliqué, false sinon
     */
    public boolean isValiderClicked() {
        return validerClicked;
    }
}