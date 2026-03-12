package school.coda.jn_charlie_clemence.bataillejavale.view.tui;

import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Grid;

import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForChar.askForChar;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForInt.askForInt;

public class ConsoleView {

    public void displayPlacementMessage(String shipName, int shipSize) {
        System.out.println("\nPlacement du navire : " + shipName + " (Taille: " + shipSize + ")");
    }

    public void displayErrorMessage(String message) {
        System.err.println("ERREUR : " + message);
    }

    public int[] askForCoordinates(int maxX, int maxY) {
        int x = askForInt("Colonne (axe X) : ", 0, maxX);
        int y = askForChar("Ligne (axe Y) : ", maxY);
        return new int[]{x, y};
    }

    public void displayGrid(Grid grid, boolean isRadar) {
        // 1. Affichage de l'en-tête (les chiffres)
        System.out.print("   ");
        for (int i = 0; i < grid.getWidth(); i++) {
            System.out.print(i + " ");
        }
        System.out.println("");

        // 2. Affichage des lignes
        for (int row = 0; row < grid.getHeight(); row++) {
            // Lettres (A, B, C...)
            char letter = (char) ('A' + row);
            System.out.print(letter + "  ");

            for (int col = 0; col < grid.getWidth(); col++) {
                // On demande l'état de la case à la grille
                // Note : Tu auras besoin d'une méthode dans Grid ou Cell
                // pour savoir si c'est touché/occupé sans briser l'encapsulation
                String symbol = getCellSymbol(grid, col, row, isRadar);
                System.out.print(symbol);
            }
            System.out.println("");
        }
    }

    private String getCellSymbol(Grid grid, int x, int y, boolean isRadar) {
        if (grid.isCellAlreadyTargeted(x, y)) {
            return grid.isCellEmpty(x, y) ? "O " : "X ";
        } else if (!isRadar && !grid.isCellEmpty(x, y)) {
            return "S ";
        }
        return "~ ";
    }
}
