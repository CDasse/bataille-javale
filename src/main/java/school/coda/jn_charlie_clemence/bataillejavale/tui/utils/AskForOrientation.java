package school.coda.jn_charlie_clemence.bataillejavale.tui.utils;

import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation;

public class AskForOrientation {
    public static Orientation askForOrientation() {
        System.out.print("Orientation (H pour Horizontal, V pour Vertical) : ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String choice = scanner.next().toUpperCase();
        return choice.equals("H") ? Orientation.HORIZONTAL : Orientation.VERTICAL;
    }
}
