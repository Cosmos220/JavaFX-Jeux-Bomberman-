package fr.univartois.butinfo.ihm.controller;
import fr.univartois.butinfo.ihm.model.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * La classe BombermanController illustre le fonctionnement du contrôleur associé à une vue.
 * Cette classe gère l'interface utilisateur du jeu Bomberman et fait le lien entre la vue
 * et le modèle métier via la facade.
 *
 * @author DAVI Loick
 * @version 1.0.0
 */
public class BombermanController implements BombermaInterface {
    /**
     * La Scene de l'application
     */
    @FXML
    public Scene scene;

    /**
     * Le label de l'application, où le nombre de bombes restantes des joueurs est affiché
     */
    @FXML
    private Label nbBombs;

    /**
     * Le label de l'application, où le nombre de Pv (nombre de vies) restantes du joueur est affiché
     */
    @FXML
    private Label Pv;

    /**
     * Le GridPane de l'application, où la carte du jeu sera affichée
     */
    @FXML
    private GridPane grid;

    /**
     * Le button de l'application, qui permet de relancer une partie quand on gagne ou perd.
     */
    @FXML
    private Button restartButton;

    /**
     * Le label de l'application, où l'état de la partie est actualisé selon si on gagne ou perd.
     */
    @FXML
    private Label etatPartie;

    /**
     * La ListView de l'application, permettant la sélection des bombes.
     */
    @FXML
    private ListView<AbstractBomb> ListviewSelectBomb;

    /**
     * La facade de l'application, permettant la gestion du modèle.
     */
    private final MaFacadeBomberman facade = new MaFacadeBomberman();

    /**
     * La constante de l'application, donnant la taille de l'image du personnage en pixels.
     */
    private static final int TAILLE_PERSO = 64;

    /**
     * La constante de l'application, donnant la taille des cases du jeu en pixels.
     */
    private static final int TAILLE = 64;

    /**
     * L'ArrayList de l'application, ayant les différentes bombes du joueur.
     */
    public ArrayList<AbstractBomb> listBombs = new ArrayList<>();

    /**
     * La méthode initialize de l'application, permettant de lancer le jeu.
     * Cette méthode est appelée automatiquement après le chargement du fichier FXML.
     * Elle configure l'interface utilisateur et démarre le jeu.
     */
    @FXML
    public void initialize() {
        facade.setBombermanInterface(this);
        ListviewSelectBomb.setCellFactory(lv -> new ListCell<AbstractBomb>() {
            @Override
            protected void updateItem(AbstractBomb bomb, boolean empty) {
                super.updateItem(bomb, empty);
                if (empty || bomb == null) {
                    setText(null);
                } else {
                    setText(bomb.getName());
                }
            }
        });

        facade.start();
    }

    /**
     * La méthode Restart de l'application, permettant l'utilisation du bouton de relancer une partie quand on gagne ou perd.
     * Cette méthode est liée au bouton restart via FXML.
     */
    @FXML
    public void restart() {
        relancerPartie();
    }

    /**
     * La méthode relancerPartie de l'application, permettant de relancer une partie.
     * Elle remet à zéro tous les éléments du jeu et redémarre une nouvelle partie.
     */
    public void relancerPartie(){
        grid.getChildren().clear();
        ListviewSelectBomb.getItems().clear();
        facade.bombs.clear();
        facade.enemies.clear();
        facade.setGameOver(false);
        facade.start();
    }

    /**
     * La méthode loadImage de l'application, permettant de générer une image sélectionnée en paramètre.
     *
     * @param imageName le nom de l'image à charger (avec extension)
     * @return l'objet Image chargé avec les dimensions spécifiées, ou null en cas d'erreur
     */
    private Image loadImage(String imageName) {
        try {
            URL url = getClass().getResource("/images/" + imageName);
            if (url == null) {
                throw new IllegalArgumentException("Image non trouvée : " + imageName);
            }
            return new Image(url.toExternalForm(), TAILLE, TAILLE, true, true);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image " + imageName + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * La méthode updateView de l'application, permettant de mettre à jour le jeu.
     * Cette méthode actualise l'affichage de la carte, des personnages, des bombes
     * et gère les liaisons avec les propriétés du modèle.
     */
    @Override
    public void updateView() {
        GameMap map = facade.getMap();
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                ImageView imageView = new ImageView();
                imageView.setFitHeight(TAILLE);
                imageView.setFitWidth(TAILLE);

                Tile currentTile = map.get(i, j);

                imageView.imageProperty().bind(
                        Bindings.createObjectBinding(
                                () -> loadImage(currentTile.getContent().getName() + ".png"),
                                currentTile.contentProperty()
                        )
                );

                grid.add(imageView, j, i);

                ImageView explosionView = new ImageView();
                explosionView.setFitHeight(TAILLE);
                explosionView.setFitWidth(TAILLE);
                explosionView.setImage(loadImage("explosion.png"));
                explosionView.setVisible(false);
                GridPane.setConstraints(explosionView, j, i);
                grid.getChildren().add(explosionView);

                currentTile.getExplodedPropertyies().addListener((obs, oldValue, newValue) -> {
                    if (newValue) {
                        explosionView.setVisible(true);
                    } else {
                        explosionView.setVisible(false);
                    }
                });
            }

        }

        restartButton.visibleProperty().bind(
                Bindings.createBooleanBinding(
                        () -> (facade.getPlayer() != null && facade.getPlayer().getHealth() <= 0) || facade.getGameWin(),
                        facade.getPlayer().healthProperty(),
                        facade.gameWinProperty
                )
        );
        liaisonPlayer(facade.getPlayer());
        liaisonPv(facade.getPlayer());
        liaisonBombs(facade.getPlayer());
        liaisonEnemies();
        Set<AbstractBomb> uniqueBombs = new HashSet<>(facade.getPlayer().getBombs());
        ListviewSelectBomb.getItems().addAll(uniqueBombs);

        etatPartie.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    if (facade.getGameOver()) {
                        return "Game Over";
                    } else if (facade.getGameWin()) {
                        return "Victoire !";
                    } else {
                        return "";
                    }
                }, facade.gameOverProperty, facade.gameWinProperty)
        );

        etatPartie.styleProperty().bind(
                Bindings.createStringBinding(() -> {
                    if (facade.getGameOver()) {
                        return "-fx-text-fill: red; -fx-font-size: 24; -fx-font-weight: bold;";
                    } else if (facade.getGameWin()) {
                        return "-fx-text-fill: green; -fx-font-size: 24; -fx-font-weight: bold;";
                    } else {
                        return "-fx-text-fill: white; -fx-font-size: 18;";
                    }
                }, facade.gameOverProperty, facade.gameWinProperty)
        );

    }

    /**
     * La méthode stockerBombes de l'application, permettant de stocker les bombes du personnage dans l'ArrayList listBombs.
     *
     * @param bomb la liste des bombes à stocker
     */
    public void stockerBombes(ArrayList<AbstractBomb> bomb){
        this.listBombs = bomb;
    }

    /**
     * La méthode liaisonPlayer de l'application, permettant la liaison du personnage dans le jeu.
     * Elle crée l'affichage visuel du personnage et gère ses déplacements et son état de vie.
     *
     * @param character le personnage à afficher et lier
     */
    @Override
    public void liaisonPlayer(AbstractCharacter character) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(TAILLE_PERSO);
        imageView.setFitWidth(TAILLE_PERSO);

        Image image = loadImage(character.getName() + ".png");
        imageView.setImage(image);
        imageView.setStyle("-fx-alignment: center; -fx-content-display: center;");

        GridPane.setConstraints(imageView, character.getColumn(), character.getRow());
        grid.getChildren().add(imageView);

        character.rowProperty().addListener((obs, oldVal, newVal) ->
                GridPane.setRowIndex(imageView, newVal.intValue()));

        character.columnProperty().addListener((obs, oldVal, newVal) ->
                GridPane.setColumnIndex(imageView, newVal.intValue()));

        character.healthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() <= 0) {
                grid.getChildren().remove(imageView);
            }
        });

    }

    /**
     * La méthode liaisonEnemies de l'application, permettant la liaison des ennemis dans le jeu.
     * Elle parcourt tous les ennemis de la facade et les affiche en utilisant liaisonPlayer.
     */
    @Override
    public void liaisonEnemies() {
        for (Enemy enemy : facade.enemies) {
            liaisonPlayer(enemy);
        }
    }

    /**
     * La méthode liaisonBombs de l'application, permettant la liaison des bombes placées du joueur dans le jeu.
     * Elle gère l'affichage du compteur de bombes avec une icône et lie le nombre à la propriété du joueur.
     *
     * @param player le joueur dont on affiche le nombre de bombes
     */
    @Override
    public void liaisonBombs(Player player) {
        // Créer l'ImageView pour l'icône de la bombe
        ImageView bombIcon = new ImageView();
        bombIcon.setFitHeight(24);
        bombIcon.setFitWidth(24);

        bombIcon.imageProperty().bind(
                Bindings.createObjectBinding(() -> {
                            if (!player.getBombs().isEmpty()) {
                                // Prendre la première bombe de la liste
                                String imageName = player.getBombs().get(0).getName() + ".png";
                                return loadImage(imageName);
                            } else {
                                // Image par défaut si pas de bombes
                                return loadImage("bomb.png");
                            }
                        },
                        // Écouter les changements sur le nombre de bombes
                        player.nbBombsProperty()
                )
        );

        nbBombs.setGraphic(bombIcon);
        nbBombs.setGraphicTextGap(10);

        nbBombs.textProperty().bind(
                Bindings.createStringBinding(
                        () -> String.valueOf(player.nbBombsProperty().get()),
                        player.nbBombsProperty()
                ));

    }

    /**
     * La méthode liaisonPv de l'application, permettant la liaison du nombre de vies restantes du joueur dans le jeu.
     * Elle affiche une icône de cœur avec le nombre de points de vie du joueur.
     *
     * @param player le joueur dont on affiche les points de vie
     */
    @Override
    public void liaisonPv(Player player) {
        ImageView heartIcon = new ImageView(loadImage("heart.png"));
        heartIcon.setFitHeight(24);
        heartIcon.setFitWidth(24);

        Pv.setGraphic(heartIcon);
        Pv.setGraphicTextGap(10);
        Pv.textProperty().bind(
                Bindings.createStringBinding(
                        () -> String.valueOf(player.getHealth()),
                        player.healthProperty()
                ));
    }

    /**
     * La méthode afficherBombe de l'application, permettant d'afficher une bombe dans le jeu.
     * Elle crée l'affichage visuel de la bombe et gère sa disparition lors de l'explosion.
     *
     * @param bomb la bombe à afficher
     * @param row la ligne où placer la bombe
     * @param cols la colonne où placer la bombe
     */
    public void afficherBombe(AbstractBomb bomb, int row, int cols) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(TAILLE_PERSO);
        imageView.setFitWidth(TAILLE_PERSO);

        Image image = loadImage(bomb.getName() + ".png");
        imageView.setImage(image);
        imageView.setStyle("-fx-alignment: center; -fx-content-display: center;");
        GridPane.setConstraints(imageView, cols, row);
        grid.getChildren().add(imageView);

        bomb.explodedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {  // Si la bombe a explosé
                grid.getChildren().remove(imageView);  // On retire l'image de la bombe
            }
        });
    }

    /**
     * La méthode ouvrirSelecteurBombe de l'application, permettant d'ouvrir la page pour sélectionner le type de bombes.
     * Elle ouvre une fenêtre modale pour que le joueur puisse choisir quelle bombe utiliser.
     *
     * @throws IOException si le fichier FXML ne peut pas être chargé
     */
    public void ouvrirSelecteurBombe() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/univartois/butinfo/ihm/BombermanSelectBombview.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sélectionner une bombe");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SelectBombController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setFacade(facade);

            dialogStage.showAndWait();

            if (controller.isValiderClicked()) {
                AbstractBomb selectedBomb = controller.getSelectedBomb();
                // Utiliser la bombe sélectionnée
                int index = facade.getPlayer().getBombs().indexOf(selectedBomb);
                facade.poserBombIndex(index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * La méthode setScene de l'application, permettant les différents mouvements du joueur dans le jeu.
     * Elle configure les contrôles clavier pour déplacer le joueur et interagir avec le jeu.
     *
     * Contrôles disponibles :
     * - Flèches directionnelles : déplacement du joueur
     * - ESPACE : poser une bombe
     * - I : ouvrir le sélecteur de bombes
     *
     * @param scene la scène sur laquelle écouter les événements clavier
     */
    public void setScene(Scene scene) {
        this.scene = scene;
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            event.consume();

            switch (event.getCode()) {
                case UP:
                    facade.movePlayerHaut();
                    break;
                case DOWN:
                    facade.movePlayerBas();
                    break;
                case LEFT:
                    facade.movePlayerGauche();
                    break;
                case RIGHT:
                    facade.movePlayerDroite();
                    break;
                case SPACE:
                    facade.poserBomb();
                    break;
                case I:
                    ouvrirSelecteurBombe();
                    break;
                default:
                    break;
            }
        });
    }
}