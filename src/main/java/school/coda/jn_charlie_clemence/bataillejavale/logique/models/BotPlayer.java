package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

import java.util.Random;
import java.util.random.RandomGenerator;

public class BotPlayer extends Player {

    public BotPlayer(String name, int widthGrid, int heightGrid) {
        super(name, widthGrid, heightGrid);
    }

    @Override
    public int[] getNextMove() {
        RandomGenerator rand = new Random();
//RandomGenerator r = () -> 0;
        int maxX = this.getGrid().getWidth();
        int maxY = this.getGrid().getHeight();

        int targetedX;
        int targetedY;

        do {
            targetedX = rand.nextInt(maxX);
            targetedY = rand.nextInt(maxY);
        } while (getGrid().isCellAlreadyTargeted(targetedX, targetedY));

        return new int[]{targetedX, targetedY};
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
}
