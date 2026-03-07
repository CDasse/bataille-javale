package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

import static school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation.HORIZONTAL;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation.VERTICAL;

public class Grid {
    private final Cell[][] cells;

    public Grid(int height, int width) {
        this.cells = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                cells[row][column] = new Cell();
            }
        }
    }

    public boolean placeShip(Ship ship, int x, int y, Orientation orientation) {
        if (canPLaceShip(ship, x, y, orientation)) {
            for (int i = 0; i < ship.getSize(); i++) {
                int currentX = (orientation == HORIZONTAL) ? x + i: x;
                int currentY = (orientation == VERTICAL) ? y + i: y;
                cells[currentX][currentY].setShip(ship);
            }
            return true;
        }
        return false;
    }

    public boolean canPLaceShip(Ship ship, int x, int y, Orientation orientation) {
        for (int i = 0; i < ship.getSize(); i++) {
            int currentX = x;
            int currentY = y;
            if (orientation == HORIZONTAL) {
                currentX = x + i;
            } else {
                currentY = y + i;
            }

            if (currentX < 0 || currentX >= cells.length || currentY < 0 || currentY >= cells[0].length || !(cells[currentX][currentY].isEmpty())) {
                return false;
            }
        }
        return true;
    }

    public boolean shoot(int x, int y) {
        if (x < 0 || x >= cells.length || y < 0 || y >= cells[0].length) {
            return false;
        }
        return cells[x][y].receiveShot();
    }

    public boolean allShipsSunk () {
        for (Cell[] cell : cells) {
            for (Cell currentCell : cell) {
                if (currentCell.isShipAlive()){
                    return false;
                }
            }
        }
        return true;
    }
}
