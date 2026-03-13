package school.coda.jn_charlie_clemence.bataillejavale.tui;

import school.coda.jn_charlie_clemence.bataillejavale.logique.models.AttackResult;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Grid;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation;

import static school.coda.jn_charlie_clemence.bataillejavale.tui.utils.AskForChar.askForChar;
import static school.coda.jn_charlie_clemence.bataillejavale.tui.utils.AskForInt.askForInt;
import static school.coda.jn_charlie_clemence.bataillejavale.tui.utils.AskForOrientation.askForOrientation;

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
            System.out.printf("%2d  ", i + 1);
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
            return grid.isCellEmpty(x, y) ? " O  " : " X  ";
        } else if (!isRadar && !grid.isCellEmpty(x, y)) {
            return " S  ";
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
        int x = askForInt("  > Colonne (1-" + (maxX + 1) + ") : ", 0, maxX)-1;
        int y = askForChar("  > Ligne", maxY);
        return new int[]{x, y};
    }

    public Orientation askForShipOrientation() {
        return askForOrientation();
    }

    public void displayMainMenu() {
        System.out.println("______       _             _  _  _         _   _                    _      ");
        System.out.println("| ___ \\     | |           (_)| || |       | \\ | |                  | |     ");
        System.out.println("| |_/ / __ _| |_ __ _ _ _  _ | || | ___   |  \\| | __ ___   ____ _  | | ___ ");
        System.out.println("| ___ \\/ _` | __/ _` | | | | | || |/ _ \\  | . ` |/ _` \\ \\ / / _` | | |/ _ \\");
        System.out.println("| |_/ / (_| | || (_| | | |_| | || |  __/  | |\\  | (_| |\\ V / (_| | | |  __/");
        System.out.println("\\____/ \\__,_|\\__\\__,_|_|\\__,_|_||_|\\___|  \\_| \\_/\\__,_| \\_/ \\__,_|_|_|\\___|");
        System.out.println("                                                                              ");
        System.out.println("\n[1] NOUVELLE PARTIE");
        System.out.println("[2] QUITTER");
        System.out.print("\n> ");
    }

    public void displayEndMenu(String winnerName) {
        System.out.println("\n*******************************************");
        System.out.println("          FIN DE LA PARTIE                 ");
        System.out.println("*******************************************");
        System.out.println("   VICTOIRE DE : " + winnerName.toUpperCase());
        System.out.println("*******************************************");
        System.out.println("\nAppuyez sur [ENTRÉE] pour revenir au menu...");

        IO.readln();
    }

    public int askGridSize(String dimension){
        int size = 0;
        boolean isValid = false;

        while (!isValid){
            System.out.print("Renseignez la " + dimension + " de la grille (entre 8 er 15) : ");
            String input = IO.readln();

            try{
                size = Integer.parseInt(input);
                if (size >= 8 && size <= 15){
                    isValid = true;
                } else {
                    System.out.println("Taille hors spectre, veuillez choisir une valeur entre 8 et 15");
                }
            } catch (NumberFormatException e){
                System.out.println(" Ce n'est pas un nombre, veuillez recommancer!");
            }
        }
        return size;
    }

    public void displayTurnCount (int turn){
        System.out.println("\n----------------------------");
        System.out.println("        TOUR NUMÉRO : " + turn);
        System.out.println("----------------------------");
    }
}