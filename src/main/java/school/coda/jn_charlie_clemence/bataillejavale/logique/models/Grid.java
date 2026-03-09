package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

import static school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation.HORIZONTAL;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation.VERTICAL;

public class Grid {
    private final Cell[][] cells;
    private final int width;
    private final int height;

    public Grid(int height, int width) {
        this.cells = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                cells[row][column] = new Cell();
            }
        }
        this.width = width;
        this.height = height;
    }

    public boolean placeShip(Ship ship, int x, int y, Orientation orientation) {
        if (canPLaceShip(ship, x, y, orientation)) {
            for (int i = 0; i < ship.getSize(); i++) {
                int currentX = (orientation == HORIZONTAL) ? x + i: x;
                int currentY = (orientation == VERTICAL) ? y + i: y;
                cells[currentY][currentX].setShip(ship);
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

            if (currentX < 0 || currentX >= cells[0].length || currentY < 0 || currentY >= cells.length || !(cells[currentY][currentX].isEmpty())) {
                return false;
            }
        }
        return true;
    }

    public boolean shoot(int x, int y) {
        if (x < 0 || x >= cells[0].length || y < 0 || y >= cells.length) {
            return false;
        }
        return cells[y][x].receiveShot();
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

    public boolean isCellAlreadyTargeted (int x, int y) {
        return cells[y][x].isTargeted();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
