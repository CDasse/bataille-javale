package school.coda.jn_charlie_clemence.bataillejavale.view.tui;

import school.coda.jn_charlie_clemence.bataillejavale.logique.models.AttackResult;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Grid;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation;

import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForChar.askForChar;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForInt.askForInt;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.utils.AskForOrientation.askForOrientation;

public class ConsoleView {

    // --- AFFICHAGE GÉNÉRAL ---

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayErrorMessage(String message) {
        System.err.println("\n [!] " + message);
    }

    public void displayPlacementHeader(String shipName, int shipSize) {
        System.out.println("\n-------------------------------------------");
        System.out.println(" Placement : " + shipName + " (Taille : " + shipSize + ")");
        System.out.println("-------------------------------------------");
    }

    // --- AFFICHAGE DES GRILLES ---

    public void displayGrid(Grid grid, String label, boolean isRadar) {
        System.out.println("\n" + label.toUpperCase());

        // 1. En-tête (Chiffres)
        System.out.print("   ");
        for (int i = 0; i < grid.getWidth(); i++) {
            System.out.printf("%2d  ", i);
        }
        System.out.println();

        // 2. Lignes (Lettre + Symboles)
        for (int row = 0; row < grid.getHeight(); row++) {
            char letter = (char) ('A' + row);
            System.out.print(letter + "  ");

            for (int col = 0; col < grid.getWidth(); col++) {
                System.out.print(getCellSymbol(grid, col, row, isRadar));
            }
            System.out.println();
        }
    }

    private String getCellSymbol(Grid grid, int x, int y, boolean isRadar) {
        if (grid.isCellAlreadyTargeted(x, y)) {
            return grid.isCellEmpty(x, y) ? "O " : "X ";
        } else if (!isRadar && !grid.isCellEmpty(x, y)) {
            return "S ";
        }
        return " ~  ";
    }

    // --- GESTION DES RÉSULTATS ---

    public void displayAttackResult(AttackResult result, String playerName) {
        if (result == null) return;

        System.out.print("\n[" + playerName + "] tire en " + (char)('A' + result.y()) + result.x() + " : ");

        if (result.hit()) {
            System.out.print("TOUCHÉ ! ");
            if (result.sunk()) {
                System.out.print("--- COULÉ (" + result.shipHit().getName() + ") ---");
            }
        } else {
            System.out.print("MANQUÉ ! ");
        }
        System.out.println();
    }

    // --- SAISIES UTILISATEUR ---

    public int[] askForCoordinates(int maxX, int maxY) {
        int x = askForInt("  > Colonne (0-" + maxX + ") : ", 0, maxX);
        int y = askForChar("  > Ligne", maxY);
        return new int[]{x, y};
    }

    public Orientation askForShipOrientation() {
        return askForOrientation();
    }
}