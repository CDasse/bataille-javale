package school.coda.jn_charlie_clemence.bataillejavale.logique.utils;

import java.util.Scanner;

public class AskForChar {
    private static Scanner scanner = new Scanner(System.in);

    public static int askForChar(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.next().toUpperCase();
            char letter = input.charAt(0);

            if (letter >= 'A' && letter <= 'O') {
                return letter - 'A';
            } else {
                System.out.println("Erreur : Veuillez entrer une lettre comprise dans l'index des lignes.");
            }
        }
    }
}
