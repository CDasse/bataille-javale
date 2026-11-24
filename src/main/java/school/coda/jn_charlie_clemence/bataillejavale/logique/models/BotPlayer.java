package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

import java.util.Random;
import java.util.Stack;

public class BotPlayer extends Player {
    private final Stack<int[]> nextTargets = new Stack<>();
    private final Random random;

    public BotPlayer(String name, int widthGrid, int heightGrid) {
        this(name, widthGrid, heightGrid, new Random());
    }

    public BotPlayer(String name, int widthGrid, int heightGrid, Random random) {
        super(name, widthGrid, heightGrid);
        this.random = random != null ? random : new Random();
    }

    @Override
    public int[] getNextMove(Grid playerGrid) {
        int[] targetFromStack = pollTargetFromStack(playerGrid);
        if (targetFromStack != null) {
            return targetFromStack;
        }
        return generateRandomTarget(playerGrid);
    }

    /**
     * Dépile et retourne la prochaine cible valide dans la pile de chasse.
     * Ignore les cellules déjà visées sur la grille adverse.
     */
    private int[] pollTargetFromStack(Grid playerGrid) {
        while (!nextTargets.empty()) {
            int[] potentialTarget = nextTargets.peek();
            if (playerGrid.isCellAlreadyTargeted(potentialTarget[0], potentialTarget[1])) {
                nextTargets.pop();
            } else {
                return nextTargets.pop();
            }
        }
        return null;
    }

    /**
     * Génère un tir aléatoire sur une case non encore ciblée.
     */
    private int[] generateRandomTarget(Grid playerGrid) {
        int maxX = this.getGrid().getWidth();
        int maxY = this.getGrid().getHeight();

        int targetedX;
        int targetedY;
        do {
            targetedX = this.random.nextInt(maxX);
            targetedY = this.random.nextInt(maxY);
        } while (playerGrid.isCellAlreadyTargeted(targetedX, targetedY));

        return new int[]{targetedX, targetedY};
    }

    /**
     * En cas de tir réussi, empile les cases voisines valides pour le prochain tour.
     */
    public void recordResult(AttackResult result, Grid playerGrid) {
        if (!result.hit()) {
            return;
        }

        int[][] cardinalNeighbors = {
                {result.x(), result.y() - 1}, // Haut
                {result.x() + 1, result.y()}, // Droite
                {result.x(), result.y() + 1}, // Bas
                {result.x() - 1, result.y()}  // Gauche
        };

        for (int[] neighbor : cardinalNeighbors) {
            if (isWithinBoundsAndUntargeted(neighbor[0], neighbor[1], playerGrid)) {
                nextTargets.push(neighbor);
            }
        }
    }

    private boolean isWithinBoundsAndUntargeted(int x, int y, Grid playerGrid) {
        return x >= 0 && x < playerGrid.getWidth()
                && y >= 0 && y < playerGrid.getHeight()
                && !playerGrid.isCellAlreadyTargeted(x, y);
    }

    /**
     * Place aléatoirement tous les navires du bot sur sa grille.
     */
    public void placeCpuShip() {
        for (Ship ship : getShips()) {
            placeSingleShip(ship);
        }
    }

    private void placeSingleShip(Ship ship) {
        boolean placed = false;
        while (!placed) {
            int randX = this.random.nextInt(getGrid().getWidth());
            int randY = this.random.nextInt(getGrid().getHeight());
            Orientation orientation = this.random.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            placed = this.getGrid().placeShip(ship, randX, randY, orientation);
        }
    }

    /**
     * Réinitialise la stratégie et vide la mémoire des cibles empilées.
     */
    public void resetStrategy() {
        this.nextTargets.clear();
    }
}
