package school.coda.jn_charlie_clemence.bataillejavale.logique.models;


import school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForInt;

import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForChar.askForChar;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForInt.askForInt;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForOrientation.askForOrientation;

public class HumanPlayer extends Player{

    public HumanPlayer(String name, int widthGrid, int heightGrid) {
        super(name, widthGrid, heightGrid);
    }

    @Override
    public int[] getNextMove() {
        IO.println("C'est à votre tour!\n PHASE DE TIR");
        int targetedX = askForInt("Coordonnées colonne (axe X) : ");
        int targetedY = askForChar("Coordonnées ligne (axe Y) : ");
        return new int[]{targetedX, targetedY};
    }

    public void placeShip(){
        for (Ship ship : getShips()){
            boolean placed = false;
            while (!placed) {
                System.out.println("\nPlacement du navire : " + ship.getName() + " (Taille: " + ship.getSize() + ")");
                getGrid().display(false);

                int x = askForInt("Colonne (axe X) : ");
                int y = askForChar("Ligne (axe Y) : ");
                Orientation orientation = askForOrientation();

                placed = getGrid().placeShip(ship, x, y , orientation);

                if (!placed){
                    System.out.println("Position impossible, veuillez réessayer!");
                }
            }
        }
    }
}
