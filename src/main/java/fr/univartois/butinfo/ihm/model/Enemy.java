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

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Random;

/**
 * La classe Enemy représente un adversaire du joueur dans le jeu du BombermanApplication.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public class Enemy extends AbstractCharacter {

    /**
     * Le nom de ce personnage.
     */
    private final String name;
    private Timeline timeline;
    /**
     * Construit un nouvel Enemy.
     *
     * @param name Le nom du personnage.
     */
    public Enemy(MaFacadeBomberman facade, String name) {
        super(facade,1);
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.ihm.bomberman.fr.univartois.butinfo.ihm.model.AbstractCharacter#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    public void moveRandomly(){
        Random rand = new Random();
        int x = rand.nextInt(4);

        switch (x) {
            case 0:
                facade.moveHaut(this);
                break;
            case 1:
                facade.moveBas(this);
                break;
            case 2:
                facade.moveGauche(this);
                break;
            case 3:
                facade.moveDroite(this);
                break;
            default:
                break;
        }
        if (facade.getPlayer().getRow() == this.getRow() &&
                facade.getPlayer().getColumn() == this.getColumn()) {
            facade.getPlayer().decHealth();
        }




    }



    @Override
    public void decHealth(){
        super.decHealth();
        if (this.getHealth()==0){
            timeline.stop();

        }
    }



    public void animate() {
         timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> moveRandomly()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
