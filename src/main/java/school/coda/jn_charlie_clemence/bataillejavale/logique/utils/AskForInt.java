package school.coda.jn_charlie_clemence.bataillejavale.logique.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AskForInt {
    private static Scanner scanner = new Scanner(System.in);

    public static int askForInt(String message, int min, int max) {
        while (true) {
            try {
                System.out.print(message);
                int result = scanner.nextInt();
                if (result >= min && result <= max) {
                    return result;
                }
                System.out.println("Hors limite ! Choisissez entre " + min + " et " + max + ".");
            } catch (InputMismatchException e) {
                System.out.println("Erreur : Entrez un nombre entier.");
                scanner.next();
            }
        }
    }
}
