---
type: TP
number: 4
---

# Création d'une interface graphique pour le jeu du *Bomberman*

Le [*Bomberman*](https://www.crazygames.fr/jeu/bomber-friends) est un jeu où le
joueur incarne un poseur de bombes, son but étant de faire exploser ses
adversaires/ennemis pour gagner.
Dans ce TP, nous allons préparer une interface graphique pour ce jeu, que nous
complèterons lors des séances futures.
À l'issue de ce TP, vous devriez être capable de :

- Combiner plusieurs conteneurs pour disposer les composants graphiques de
  manière appropriée.
- Initialiser la vue depuis le code du contrôleur.
- Afficher des images dans une vue *JavaFX*.

## Création du projet

Nous allons commencer par créer un projet *Eclipse* pour votre application.

1. Décompressez l'archive projet `jfx-skeleton` et importez le dans *Eclipse*.
   Veillez à le nommer de manière appropriée, et à adapter les différents
   fichiers qu'il contient de manière à ceux que ceux-ci reflètent le thème
   de l'application.

2. Préparez une configuration *Gradle* pour pouvoir exécuter l'application,
   et assurez-vous qu'elle s'affiche correctement.

## Création de l'interface graphique

Ouvrez maintenant le fichier FXML de votre application avec le *SceneBuilder*.

3. Créez la vue graphique de votre application en choisissant les composants
   appropriés compte-tenu des informations suivantes (n'hésitez pas à parcourir
   les différents choix offerts par le *SceneBuilder*):

   - À l'arrière-plan, la carte du jeu sera affichée sous la forme d'une grille
     de taille variable comportant sur ses cases des murs, de l'herbe, *etc.*
   - Le personnage du joueur ainsi que les ennemis vont se déplacer au dessus
     de cette carte.
   - Sur les différentes extrêmités de la fenêtre, on pourra afficher
     différentes informations, notamment le nombre de points de vie restant pour
     le joueur, le nombre de bombes qu'il possède encore, le nombre d'ennemis
     à éliminer, *etc.*
     Il doit également être possible de (re)démarrer une part, ou encore
     d'ouvrir l'inventaire (il ne vous est pas demandé pour l'instant de
     créer la vue pour l'inventaire).
   - L'application doit être aussi *responsive*  que possible : veillez à
     renseigner correctement les dimensions de vos composants graphiques pour
     vous en assurer.

4. Spécifiez les informations nécessaires pour pouvoir manipuler votre vue
   depuis le contrôleur.
   Précisez en particulier un `fx:id` pour les composants qui en ont besoin (et
   uniquement ceux-là), ainsi que des méthodes `onAction` quand vous le jugez
   nécessaire.

5. Essayez à nouveau de lancer vote application pour vérifier que tout
   fonctionne bien.

## Initialisation de la vue depuis le contrôleur.

Pour le moment, l'interface graphique que vous avez réalisée est relativement
vide.
Vous allez maintenant la compléter depuis le contrôleur.

6. Si ce n'est pas encore fait, créez la classe qui servira de contrôleur
   pour votre vue, et compléter-là avec les attributs et méthodes correspondant
   à ce que vous avez déclaré dans votre fichier FXML.

7. Définissez une constante pour la hauteur de la carte de votre application,
   et une autre pour sa largeur.

8. Ajoutez dans cette classe une méthode `initialize()` visant à initialiser
   la vue de votre application, comme vous l'avez déjà fait dans vos
   applications précédentes.
   Celle-ci va devoir compléter la grille représentant la carte avec autant de
   composants `ImageView` que nécessaire en tenant compte des constantes de la
   question précédente.
   Donnez leur une taille appropriée en fonction de vos besoins.

9. Exécutez votre application pour constater la différence.

10. Modifiez les images affichées par les `ImageView` pour avoir un aperçu du
    résultat final.
    Vous pouvez pour cela choisir parmi [celles-ci](images), qu'il vous faudra
    placer dans un package du répertoire `resources` de votre application.
    Pour lire une image à partir d'un fichier, vous pouvez prendre exemple sur
    cette méthode :

    ```java
    private Image loadImage() {
        try {
            URL url = getClass().getResource("chemin/vers/image.png");
            return new Image(url.toExternalForm(), 16, 16, true, true);

        } catch (NullPointerException | IllegalArgumentException e) {
            throw new NoSuchElementException("Could not load image", e);
        }
    }
    ```

    Il ne vous ai pas demandé de faire une carte élaborée pour le moment :
    vous devez uniquement placer des images pour vérifier leur affichage.

12. Relancez votre application.
    Vos images s'affichent-elles comme souhaité.

## Réflexion sur le modèle objet du jeu

Pour pouvoir jouer au *Bomberman*, nous allons avoir besoin de représenter
le jeu sous la forme de classes.

13. De quelles classes avez-vous besoin pour représenter le jeu ?
    Quels attributs doivent-elles avoir ?
    Quelles méthodes ?
    Réfléchissez à ces questions sans implémenter de code pour le moment :
    Cette implémentation sera réalisée lors de la prochaine séance.
