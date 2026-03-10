package school.coda.jn_charlie_clemence.bataillejavale.logique.utils;

public class AskForInt {
    public static int askForInt(String message) {
        System.out.print(message);
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        return scanner.nextInt();
    }
}
