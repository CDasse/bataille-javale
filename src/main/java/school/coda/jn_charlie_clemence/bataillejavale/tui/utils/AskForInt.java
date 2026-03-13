package school.coda.jn_charlie_clemence.bataillejavale.tui.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AskForInt {
    private static final Scanner scanner = new Scanner(System.in);

    public static int askForInt(String message, int min, int max) {
        while (true) {
            try {
                System.out.print(message);
                int result = scanner.nextInt();
                if (result >= min + 1 && result <= max + 1) {
                    return result;
                }
                System.out.println("Hors limite ! Choisissez entre " + (min + 1) + " et " + (max + 1) + ".");
            } catch (InputMismatchException e) {
                System.out.println("Erreur : Entrez un nombre entier.");
                scanner.next();
            }
        }
    }
}
