package school.coda.jn_charlie_clemence.bataillejavale.logique.models;


import school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForInt;

import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForChar.askForChar;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForInt.askForInt;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForOrientation.askForOrientation;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, int widthGrid, int heightGrid) {
        super(name, widthGrid, heightGrid);
    }

    @Override
    public int[] getNextMove() {
        IO.println("C'est à votre tour!\n PHASE DE TIR");
        int maxX = getGrid().getWidth() - 1;
        int maxY = getGrid().getHeight();
        int targetedX = askForInt("Coordonnées colonne (axe X) : ", 0, maxX);
        int targetedY = askForChar("Coordonnées ligne (axe Y) : ", maxY);

        while (getGrid().isCellAlreadyTargeted(targetedX, targetedY)) {
            System.out.println("Vous avez déjà tiré sur cette case ! Choisissez une autre cible.");
            targetedX = askForInt("Coordonnées colonne (axe X) : ", 0, maxX);
            targetedY = askForChar("Coordonnées ligne (axe Y) : ", maxY);

        }
        return new int[]{targetedX, targetedY};
    }

    public void placeShip() {
        int maxX = getGrid().getWidth() - 1;
        int maxY = getGrid().getHeight();

        for (Ship ship : getShips()) {
            boolean placed = false;
            while (!placed) {
                System.out.println("\nPlacement du navire : " + ship.getName() + " (Taille: " + ship.getSize() + ")");
                getGrid().display(false);

                int x = askForInt("Colonne (axe X) : ", 0, maxX);
                int y = askForChar("Ligne (axe Y) : ", maxY);
                Orientation orientation = askForOrientation();

                placed = getGrid().placeShip(ship, x, y, orientation);

                if (!placed) {
                    System.out.println("Position impossible, veuillez réessayer!");
                }
            }
        }
    }
}
