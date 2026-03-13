package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

import java.util.Random;
import java.util.Stack;

public class BotPlayer extends Player {
    private final Stack<int[]> nextTargets = new Stack<>();

    public BotPlayer(String name, int widthGrid, int heightGrid) {
        super(name, widthGrid, heightGrid);
    }

    @Override
    public int[] getNextMove(Grid playerGrid) {
        // verifying the stack, if the cell is already targeted pop the last element inserted, if no return the pop element as the coordinates to target
        while (!nextTargets.empty()){
            int[] potentialTarget = nextTargets.peek();
            if (playerGrid.isCellAlreadyTargeted(potentialTarget[0], potentialTarget[1])) {
                nextTargets.pop();
            } else {
                return nextTargets.pop();
            }
        }

        Random rand = new Random();

        int maxX = this.getGrid().getWidth();
        int maxY = this.getGrid().getHeight();

        int targetedX;
        int targetedY;

        do {
            targetedX = rand.nextInt(maxX);
            targetedY = rand.nextInt(maxY);
        } while (playerGrid.isCellAlreadyTargeted(targetedX, targetedY));



        return new int[]{targetedX, targetedY};
    }
    // method for improving the cpu behavior, when he hit a cell with a ship in it, he will target the possible adjacent cell
    public void recordResult (AttackResult result, Grid playerGrid){
        if (!result.hit()){
            return;
        }

        int[][] cardinalites = {
                {result.x(), result.y() - 1},
                {result.x() + 1, result.y()},
                {result.x(), result.y() + 1},
                {result.x() - 1, result.y()},
        };

        for (int[] voisin : cardinalites){
            int vX = voisin[0];
            int vY = voisin[1];

            if (vX >= 0 && vX < playerGrid.getWidth() && vY >= 0 && vY < playerGrid.getHeight() && !playerGrid.isCellAlreadyTargeted(vX, vY)){
                nextTargets.push(voisin);
            }
        }


    }

    public void placeCpuShip () {
        Random rand = new Random();
        int randX;
        int randY;
        Orientation randomOrientation;

        for (Ship ship: getShips()) {
            do {
                randX = rand.nextInt(getGrid().getWidth());
                randY = rand.nextInt(getGrid().getHeight());

                randomOrientation = (rand.nextBoolean()) ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            } while (!(this.getGrid().placeShip(ship, randX, randY, randomOrientation)));
        }
    }

    // clearing the cpu memory for new game
    public void resetStrategy (){
        this.nextTargets.clear();
    }

}
