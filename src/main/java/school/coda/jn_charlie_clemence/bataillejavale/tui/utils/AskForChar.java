package school.coda.jn_charlie_clemence.bataillejavale.tui.utils;

import java.util.Scanner;

public class AskForChar {
    private static final Scanner scanner = new Scanner(System.in);

    public static int askForChar(String message, int gridSize) {
        char maxLetter = (char) ('A' + gridSize - 1);

        while (true) {
            System.out.print(message + " (A-" + maxLetter + ") : ");
            String input = scanner.next().toUpperCase();

            if (!input.isEmpty()) {
                char letter = input.charAt(0);
                if (letter >= 'A' && letter <= maxLetter) {
                    return letter - 'A';
                }
            }
            System.out.println("Lettre invalide ! Entrez une lettre entre A et " + maxLetter + ".");
        }
    }
}
