package fr.univartois.butinfo.ihm.model; /**
 * Ce logiciel est distribué à des fins éducatives.
 *
 * Il est fourni "tel quel", sans garantie d'aucune sorte, explicite
 * ou implicite, notamment sans garantie de qualité marchande, d'adéquation
 * à un usage particulier et d'absence de contrefaçon.
 * En aucun cas, les auteurs ou titulaires du droit d'auteur ne seront
 * responsables de tout dommage, réclamation ou autre responsabilité, que ce
 * soit dans le cadre d'un contrat, d'un délit ou autre, en provenance de,
 * consécutif à ou en relation avec le logiciel ou son utilisation, ou avec
 * d'autres éléments du logiciel.
 *
 * (c) 2022-2025 Romain Wallon - Université d'Artois.
 * Tous droits réservés.
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.util.Duration;

/**
 * La classe Tile représente une tuile composant la carte du jeu du BombermanApplication.
 * Une fois créée, une telle tuile devient fixe sur la carte : c'est son
 * contenu qui change au cours du jeu.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public class Tile {
    // Attributs row et column restent inchangés
    
    /**
     * La ligne où cette tuile est positionnée sur la carte.
     */

    private BooleanProperty exploded = new SimpleBooleanProperty(false);
    private final int row;
    
    /**
     * La colonne où cette tuile est positionnée sur la carte.
     */
    private final int column;
    
    /**
     * Le contenu de cette tuile.
     */
    private  ObjectProperty<TileContent> content = new SimpleObjectProperty<>();
    
    /**
     * Construit une nouvelle instance de Tile.
     * 
     * @param row La ligne où la tuile est positionnée sur la map.
     * @param column La colonne où la tuile est positionnée sur la map.
     */
    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
        this.content = new SimpleObjectProperty<>(TileContent.LAWN);

    }
    
    
    public BooleanProperty getExplodedPropertyies(){
        return exploded;
    }


    public void explode(){
        if (this.content.get().isDestroyableByExplosion()){
            getExplodedPropertyies().set(true);
            setContent(TileContent.LAWN);
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.5), e -> getExplodedPropertyies().set(false)));
            timeline.play();

        }

    }
    // Le constructeur reste inchangé
    
    // Les méthodes getRow() et getColumn() restent inchangées
    /**
     * Donne la ligne où cette tuile est positionnée sur la map.
     *
     * @return La ligne où cette tuile est positionnée sur la map.
     */
    public int getRow() {
        return row;
    }

    /**
     * Donne la colonne où cette tuile est positionnée sur la map.
     *
     * @return La colonne où cette tuile est positionnée sur la map.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Donne le contenu de cette tuile.
     *
     * @return Le contenu de cette tuile.
     */
    public TileContent getContent() {
        return content.get();
    }

    /**
     * Permet d'accéder à la propriété du contenu.
     *
     * @return La propriété du contenu de la tuile.
     */
    public ObjectProperty<TileContent> contentProperty() {
        return content;
    }

    /**
     * Change le contenu de cette tuile.
     *
     * @param content Le nouveau contenu de cette tuile.
     */
    public void setContent(TileContent content) {
        this.content.set(content);
    }

    /**
     * Vérifie si cette tuile est vide, c'est-à-dire que son contenu est vide.
     *
     * @return Si cette tuile est vide.
     *
     * @see ITileContent#isEmpty()
     */
    
    public boolean isEmpty() {

        return content.get().isEmpty();
    }

}