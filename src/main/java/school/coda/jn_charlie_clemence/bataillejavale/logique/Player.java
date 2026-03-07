package school.coda.jn_charlie_clemence.bataillejavale.logique;

import com.almasb.fxgl.core.collection.grid.Grid;
import school.coda.jn_charlie_clemence.bataillejavale.logique.utils.ShipFactory;

import java.util.List;

public class Player {
    private final String name;
    private final boolean isAI;
    private final Grid grid;
    private final List<Ship> ships;

    public Player() {
        this("AI", true);
    }

    public Player(String name) {
        this(name, false);
    }

    private Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
        this.grid = new Grid();
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
