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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * La classe AbstractCharacter est la classe parente des différents personnages pouvant se
 * déplacer dans le jeu du BombermanApplication.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public abstract class AbstractCharacter {

    /**
     * La façade du jeu.
     */
    public MaFacadeBomberman facade;

    /**
     * La ligne où se trouve ce personnage.
     */
    public  IntegerProperty row;

    /**
     * La colonne où se trouve ce personnage.
     */
    public  IntegerProperty column;

    /**
     * Les points de vie restants pour ce personnage.
     */
    private IntegerProperty health;

    /**
     * Crée une nouvelle instance de AbstractCharacter.
     *
     * @param facade La façade du jeu
     * @param initialHealth Les points de vie initiaux du personnage.
     */
    protected AbstractCharacter(MaFacadeBomberman facade, int initialHealth) {
       this.facade = facade;
        this.health = new SimpleIntegerProperty(initialHealth);
        row = new SimpleIntegerProperty();
        column = new SimpleIntegerProperty();

    }

    /**
     * Donne la propriété de la ligne.
     *
     * @return La propriété de la ligne.
     */
    public IntegerProperty rowProperty() {
        return row;
    }

    /**
     * Donne la propriété de la colonne.
     *
     * @return La propriété de la colonne.
     */
    public IntegerProperty columnProperty() {
        return column;
    }
    
    // Le reste de la classe reste inchangé...
    /**
     * Donne le nom de ce personnage.
     *
     * @return Le nom de ce personnage.
     */
    public abstract String getName();

    /**
     * Donne la ligne où se trouve ce personnage.
     *
     * @return La ligne où se trouve ce personnage.
     */
    public int getRow() {
       return row.get();
    }

    /**
     * Donne la colonne où se trouve ce personnage.
     *
     * @return La colonne où se trouve ce personnage.
     */
    public int getColumn() {
        return column.get();
    }

    /**
     * Modifie la position de ce personnage.
     *
     * @param row La ligne où se trouve maintenant ce personnage.
     * @param column La colonne où se trouve maintenant ce personnage.
     */
    public void setPosition(int row, int column) {
        this.row.set(row);
        this.column.set(column);
    }

    /**
     * Donne les points de vie restants pour ce personnage.
     *
     * @return Les points de vie restants pour ce personnage.
     */
    public int getHealth() {
        return health.get();
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    /**
     * Augmente les points de vie de ce personnage.
     */
    public void incHealth() {
        health.set(health.get() + 1);
    }

    /**
     * Diminue les points de vie de ce personnage.
     */
    public void decHealth() {
        if (health.get() > 0) {

            health.set(health.get() - 1);
        }

    }

}