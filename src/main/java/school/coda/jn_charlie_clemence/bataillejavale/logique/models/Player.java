package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

import school.coda.jn_charlie_clemence.bataillejavale.logique.utils.ShipFactory;

import java.util.List;

public class Player {
    private final String name;
    private final boolean isAI;
    private final Grid grid;
    private final List<Ship> ships;

    public Player( int widthGrid, int heightGrid) {
        this("AI", true, widthGrid, heightGrid);
    }

    public Player(String name, int widthGrid, int heightGrid) {
        this(name, false, widthGrid, heightGrid);
    }

    private Player(String name, boolean isAI, int widthGrid, int heightGrid) {
        this.name = name;
        this.isAI = isAI;
        this.grid = new Grid(widthGrid, heightGrid);
        this.ships = ShipFactory.createFleet();
    }

    public boolean hasLost() {
        for (Ship ship : this.ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }
}
