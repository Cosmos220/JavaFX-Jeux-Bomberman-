package fr.univartois.butinfo.ihm.controller;

import fr.univartois.butinfo.ihm.model.AbstractBomb;
import fr.univartois.butinfo.ihm.model.AbstractCharacter;
import fr.univartois.butinfo.ihm.model.Player;

import java.util.ArrayList;

public interface BombermaInterface {
    void updateView();
    void liaisonPlayer(AbstractCharacter character);
    void liaisonBombs(Player player);
    void afficherBombe(AbstractBomb bomb, int row, int cols);
    void liaisonEnemies();
    void liaisonPv(Player player);
    void stockerBombes(ArrayList<AbstractBomb> bombs);
}