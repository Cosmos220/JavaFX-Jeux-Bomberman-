package fr.univartois.butinfo.ihm.model;

import fr.univartois.butinfo.ihm.controller.BombermaInterface;
import fr.univartois.butinfo.ihm.view.GameMapFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

/**
 * Classe principale qui gère la logique du jeu BombermanApplication.
 * Cette façade centralise toutes les interactions entre les différents composants du jeu :
 * joueur, ennemis, bombes, carte de jeu et interface utilisateur.
 *
 * Elle implémente le pattern Facade pour simplifier l'accès aux fonctionnalités du jeu
 * et gère l'état global de la partie (en cours, game over, victoire).
 *
 * @author [Votre nom]
 * @version 1.0
 * @since 1.0
 */
public class MaFacadeBomberman {

    /** Le joueur principal du jeu */
    private Player player;

    /** La carte de jeu avec ses dimensions par défaut */
    private GameMap map = new GameMap(ROWS, COLS);

    /** Interface pour communiquer avec la vue */
    private BombermaInterface BombermanInterface;

    /** Nombre de lignes de la carte de jeu */
    private static final int ROWS = 13;

    /** Nombre de colonnes de la carte de jeu */
    private static final int COLS = 15;

    /** Nombre d'ennemis à placer sur la carte */
    private static final int NBENEMIES = 5;

    /** Liste des ennemis présents sur la carte */
    public ArrayList<Enemy> enemies = new ArrayList<>();

    /** Liste des bombes posées sur la carte */
    public ArrayList<AbstractBomb> bombs = new ArrayList<>();

    /** Propriété observable indiquant si la partie est terminée (défaite) */
    public final BooleanProperty gameOverProperty = new SimpleBooleanProperty(false);

    /** Propriété observable indiquant si la partie est gagnée */
    public final BooleanProperty gameWinProperty = new SimpleBooleanProperty(false);

    /** Instance de bombe standard */
    public final Bomb Bomb = new Bomb(this);

    /** Instance de bombe qui explose en ligne */
    public final RowBomb RowBomb = new RowBomb(this);

    /** Instance de bombe qui explose en colonne */
    public final ColumnBomb ColumBomb = new ColumnBomb(this);

    /** Instance de bombe avec grande zone d'explosion */
    public final LargeBomb LargeBomb = new LargeBomb(this);

    /** Liste observable des types de bombes disponibles */
    public ObservableList<AbstractBomb> bombType = FXCollections.observableArrayList(Bomb, RowBomb, ColumBomb, LargeBomb);

    /**
     * Initialise la liste des types de bombes disponibles.
     * Ajoute tous les types de bombes à la liste observable.
     */
    public void setBombType() {
        bombType.add(Bomb);
        bombType.add(RowBomb);
        bombType.add(ColumBomb);
        bombType.add(LargeBomb);
    }

    /**
     * Retourne la liste observable des types de bombes disponibles.
     *
     * @return ObservableList contenant tous les types de bombes
     */
    public ObservableList<AbstractBomb> getTypeBomb() {
        return bombType;
    }

    /**
     * Vérifie si la partie est terminée par une défaite.
     *
     * @return true si le jeu est terminé par game over, false sinon
     */
    public Boolean getGameOver() {
        return gameOverProperty.get();
    }

    /**
     * Vérifie si la partie est gagnée.
     *
     * @return true si le jeu est gagné, false sinon
     */
    public Boolean getGameWin() {
        return gameWinProperty.get();
    }

    /**
     * Pose une bombe à la position actuelle du joueur.
     * Utilise la première bombe disponible dans l'inventaire du joueur.
     */
    public void poserBomb() {
        poserBombIndex(0);
    }

    /**
     * Pose une bombe spécifique à la position actuelle du joueur.
     * La bombe est programmée pour exploser après 5 secondes.
     *
     * @param index l'index de la bombe à poser dans l'inventaire du joueur
     */
    public void poserBombIndex(int index) {
        if (player.getBombs() != null && index >= 0 && index < player.getBombs().size()) {
            int row = this.player.getRow();
            int col = this.player.getColumn();

            AbstractBomb bomb = player.getBombs().get(index);
            bomb.setPosition(row, col);

            BombermanInterface.afficherBombe(bomb, row, col);
            player.retirerBombs(index);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(5), e -> bomb.explode()));
            timeline.play();
        }
    }

    /**
     * Définit l'état de game over de la partie.
     *
     * @param value true pour activer le game over, false pour le désactiver
     */
    public void setGameOver(boolean value) {
        gameOverProperty.set(value);
    }

    /**
     * Définit l'état de victoire de la partie.
     *
     * @param value true pour activer la victoire, false pour la désactiver
     */
    public void setGameWin(boolean value) {
        gameWinProperty.set(value);
    }

    /**
     * Termine la partie en cours.
     * Vérifie si tous les ennemis sont morts pour déterminer la victoire ou la défaite.
     * Nettoie les listes d'ennemis et de bombes.
     */
    public void endGame() {
        if (Alldied()) {
            setGameWin(true);
        } else {
            setGameOver(true);
        }

        for (Enemy enemy : enemies) {
            enemy.decHealth();
        }
        enemies.clear();
        bombs.clear();
    }

    /**
     * Vérifie si tous les ennemis sont morts.
     *
     * @return true si tous les ennemis sont morts ou s'il n'y a pas d'ennemi, false sinon
     */
    public Boolean Alldied() {
        if (enemies.isEmpty()) {
            return true; // Considère la victoire si aucun ennemi n'est présent
        }

        for (Enemy enemy : enemies) {
            if (enemy.getHealth() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gère l'explosion à une position donnée sur la carte.
     * Affecte la case, le joueur et les ennemis présents à cette position.
     * Vérifie après l'explosion si tous les ennemis sont morts pour terminer la partie.
     *
     * @param row la ligne de l'explosion
     * @param col la colonne de l'explosion
     */
    public void explode(int row, int col) {
        if (map.isOnMap(row, col)) {
            map.get(row, col).explode();
        }

        if (player.getRow() == row && player.getColumn() == col) {
            player.decHealth();
        }

        for (Enemy enemy : enemies) {
            if (enemy.getRow() == row && enemy.getColumn() == col) {
                enemy.decHealth();
            }
        }

        if (Alldied()) {
            endGame();
        }
    }

    /**
     * Initialise une nouvelle partie.
     * Remet à zéro tous les éléments du jeu : ennemis, bombes, états de jeu.
     * Crée un nouveau joueur, génère une nouvelle carte et place les personnages.
     * Met à jour l'affichage via l'interface.
     */
    public void start() {
        this.enemies.clear();
        this.bombs.clear();
        setGameOver(false);
        setGameWin(false);

        this.player = new Player(this, bombs);
        BombermanInterface.stockerBombes(bombs);

        this.map = GameMapFactory.createMapWithRandomBrickWalls(ROWS, COLS, 50);
        setMap(map);

        placerPerso(player);
        placeenemies(NBENEMIES);

        if (BombermanInterface != null) {
            BombermanInterface.updateView();
        }
    }

    /**
     * Définit la carte de jeu.
     *
     * @param map la nouvelle carte de jeu
     */
    public void setMap(GameMap map) {
        this.map = map;
    }

    /**
     * Définit l'interface de communication avec la vue.
     *
     * @param BombermanInterface l'interface à utiliser pour les mises à jour d'affichage
     */
    public void setBombermanInterface(BombermaInterface BombermanInterface) {
        this.BombermanInterface = BombermanInterface;
    }

    /**
     * Place un nombre donné d'ennemis aléatoirement sur la carte.
     * Les ennemis sont choisis aléatoirement parmi les types disponibles :
     * agent, minotaur, goblin.
     *
     * @param NBENEMIES le nombre d'ennemis à placer
     */
    public void placeenemies(int NBENEMIES) {
        for (int i = 0; i < NBENEMIES; i++) {
            int x = new Random().nextInt(3);
            Enemy enemy = null;
            switch (x) {
                case 0:
                    enemy = new Enemy(this, "agent");
                    break;
                case 1:
                    enemy = new Enemy(this, "minotaur");
                    break;
                case 2:
                    enemy = new Enemy(this, "goblin");
                    break;
                default:
                    break;
            }
            this.enemies.add(enemy);
            this.placerPerso(enemy);
            enemy.animate();
        }
    }

    /**
     * Retourne la carte de jeu actuelle.
     *
     * @return la carte de jeu
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Place un personnage à une position aléatoire sur une case vide de la carte.
     *
     * @param character le personnage à placer
     */
    public void placerPerso(AbstractCharacter character) {
        Random random = new Random();
        int x = random.nextInt(map.getEmptyTiles().size());
        character.setPosition(map.getEmptyTiles().get(x).getRow(), map.getEmptyTiles().get(x).getColumn());
    }

    /**
     * Retourne le joueur actuel.
     *
     * @return le joueur
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Déplace un personnage vers le haut si possible.
     * Vérifie que la destination est sur la carte et sur une case d'herbe.
     *
     * @param character le personnage à déplacer
     */
    public void moveHaut(AbstractCharacter character) {
        int newRow = character.getRow() - 1;
        int currentCol = character.getColumn();

        if (map.isOnMap(newRow, currentCol) &&
                map.get(newRow, currentCol).getContent() == TileContent.LAWN) {
            character.setPosition(newRow, currentCol);
        }
    }

    /**
     * Déplace un personnage vers le bas si possible.
     * Vérifie que la destination est sur la carte et sur une case d'herbe.
     *
     * @param character le personnage à déplacer
     */
    public void moveBas(AbstractCharacter character) {
        int newRow = character.getRow() + 1;
        int currentCol = character.getColumn();

        if (map.isOnMap(newRow, currentCol) &&
                map.get(newRow, currentCol).getContent() == TileContent.LAWN) {
            character.setPosition(newRow, currentCol);
        }
    }

    /**
     * Déplace un personnage vers la gauche si possible.
     * Vérifie que la destination est sur la carte et sur une case d'herbe.
     *
     * @param character le personnage à déplacer
     */
    public void moveGauche(AbstractCharacter character) {
        int currentRow = character.getRow();
        int newCol = character.getColumn() - 1;

        if (map.isOnMap(currentRow, newCol) &&
                map.get(currentRow, newCol).getContent() == TileContent.LAWN) {
            character.setPosition(currentRow, newCol);
        }
    }

    /**
     * Déplace un personnage vers la droite si possible.
     * Vérifie que la destination est sur la carte et sur une case d'herbe.
     *
     * @param character le personnage à déplacer
     */
    public void moveDroite(AbstractCharacter character) {
        int currentRow = character.getRow();
        int newCol = character.getColumn() + 1;

        if (map.isOnMap(currentRow, newCol) &&
                map.get(currentRow, newCol).getContent() == TileContent.LAWN) {
            character.setPosition(currentRow, newCol);
        }
    }

    /**
     * Déplace le joueur vers le haut.
     */
    public void movePlayerHaut() {
        moveHaut(player);
    }

    /**
     * Déplace le joueur vers le bas.
     */
    public void movePlayerBas() {
        moveBas(player);
    }

    /**
     * Déplace le joueur vers la gauche.
     */
    public void movePlayerGauche() {
        moveGauche(player);
    }

    /**
     * Déplace le joueur vers la droite.
     */
    public void movePlayerDroite() {
        moveDroite(player);
    }

    /**
     * Trie la liste des bombes du joueur pour mettre en premier les bombes du type sélectionné.
     * Si le type spécifié existe dans la liste, les bombes de ce type seront placées
     * au début de la liste.
     *
     * @param typeSelectionne la classe du type de bombe à prioriser
     */
    public void trierlist(Class<?> typeSelectionne) {
        if (listeContientType(typeSelectionne)) {
            player.getBombs().sort((a, b) -> {
                if (typeSelectionne.isInstance(a) && !typeSelectionne.isInstance(b)) {
                    return -1; // a avant b
                } else if (!typeSelectionne.isInstance(a) && typeSelectionne.isInstance(b)) {
                    return 1; // b avant a
                }
                return 0; // pas de changement
            });
        }
    }

    /**
     * Vérifie si la liste des bombes du joueur contient au moins une bombe du type spécifié.
     *
     * @param type la classe du type de bombe à rechercher
     * @return true si au moins une bombe de ce type existe, false sinon
     */
    public boolean listeContientType(Class<?> type) {
        for (Object o : player.getBombs()) {
            if (type.isInstance(o)) {
                return true;
            }
        }
        return false;
    }
    public int nbRestant(AbstractBomb bomb) {
        int count = 0;
        for (AbstractBomb nbbomb : bombs) {
            if (nbbomb.getClass().equals(bomb.getClass())) {
                count++;
            }
        }
        return count;
    }
}

