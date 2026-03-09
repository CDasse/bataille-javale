package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

import school.coda.jn_charlie_clemence.bataillejavale.logique.utils.ShipFactory;

import java.util.List;

public abstract class Player {
    private final String name;
    private final Grid grid;
    private final List<Ship> ships;

    protected Player(String name, int widthGrid, int heightGrid) {
        this.name = name;
        this.grid = new Grid(heightGrid, widthGrid);
        this.ships = ShipFactory.createFleet();
    }

    public abstract int[] getNextMove();

    public Grid getGrid() {
        return this.grid;
    }

    public List<Ship> getShips () {
        return this.ships;
    }
}
