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

import fr.univartois.butinfo.ihm.controller.BombermaInterface;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * la classe Player représente le personnage du joueur qui utilise l'application.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public class Player extends AbstractCharacter {
    private final IntegerProperty nbBombs;
    private final ArrayList<AbstractBomb> bombs;

    public Player(MaFacadeBomberman facade, ArrayList<AbstractBomb> bombs) {
        super(facade, 3);
        this.bombs = bombs;
        this.nbBombs = new SimpleIntegerProperty(0);

        for (int x = 0; x < 20; x++) {
            if (x <= 7){
            RowBomb bomb = new RowBomb(facade);
            bombs.add(bomb);

        }
        else if (x <= 14){
            Bomb bomb = new Bomb(facade);
            bombs.add(bomb);
        }
        else if (x <= 18) {
            LargeBomb bomb = new LargeBomb(facade);
            bombs.add(bomb);
            }
        else  {
                ColumnBomb bomb = new ColumnBomb(facade);
                bombs.add(bomb);
            }

        }
        this.nbBombs.set(bombs.size());
    }


    @Override
    public void decHealth(){
        super.decHealth();
        if (this.getHealth()==0){
            facade.setGameOver(true);
            facade.endGame();
            bombs.clear();
            nbBombs.set(0);


        }
    }

    @Override
    public String getName() {
        return "guy";  // Retourne le nom du joueur
    }

    public IntegerProperty nbBombsProperty() {
        return nbBombs;
    }

    public int getNbBombs() {
        return nbBombs.get();
    }
    public ArrayList<AbstractBomb> getBombs(){
        return bombs;
    }

    public void retirerBombs(int x) {
        bombs.remove(x);
        nbBombs.set(bombs.size());
    }
}