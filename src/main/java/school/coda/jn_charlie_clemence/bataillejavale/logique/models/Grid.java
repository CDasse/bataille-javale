package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

import java.util.ArrayList;
import java.util.List;

import static school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation.HORIZONTAL;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation.VERTICAL;

public class Grid {
    private final Cell[][] cells;
    private final int width;
    private final int height;
    private final List<String> shipsAlreadyPlaced;

    public Grid(int height, int width) {
        this.cells = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                cells[row][column] = new Cell();
            }
        }
        this.width = width;
        this.height = height;
        this.shipsAlreadyPlaced = new ArrayList<>(5);
    }

    private static boolean isInvalid(int xOrY) {
        return xOrY < 0;
    }

    public List<String> getListShipsPlaced() {
        List<String> shipList = new ArrayList<>(5);
        shipList.addAll(shipsAlreadyPlaced);
        return shipList;
    }

    public boolean placeShip(Ship ship, int x, int y, Orientation orientation) {
        if (canPlaceShip(ship, x, y, orientation)) {
            for (int i = 0; i < ship.getSize(); i++) {
                int currentX = (orientation == HORIZONTAL) ? x + i : x;
                int currentY = (orientation == VERTICAL) ? y + i : y;
                cells[currentY][currentX].setShip(ship);
            }
            shipsAlreadyPlaced.add(ship.getName());
            return true;
        }
        return false;
    }

    // Trop forte complexité cyclomatique
    public boolean canPlaceShip(Ship ship, int x, int y, Orientation orientation) {
        if (isShipAlreadyPlaced(ship)) return false;
        if (isShipTooBigToBePlaced(ship, x, y, orientation)) return false;

        for (int i = 0; i < ship.getSize(); i++) {
            int currentX = (orientation == HORIZONTAL) ? x + i : x;
            int currentY = (orientation == VERTICAL) ? y + i : y;

            // De manière générale votre code serait plus lisible si vous aviez créer des types "wrapper"
            // Exemple : plutôt que de parler de cells[0] -> on parlerait de row
            if (isNotEmpty(currentX, currentY)) {
                return false;
            }
        }
        return true;
    }

    private boolean isShipTooBigToBePlaced(Ship ship, int x, int y, Orientation orientation) {
        if (orientation == HORIZONTAL) {
            return x < 0 || x + ship.getSize() > this.width;
        }
        return y < 0 || y + ship.getSize() > this.height;
    }

    private boolean isNotEmpty(int currentX, int currentY) {
        return isInvalid(currentX)
                || currentX >= cells[0].length
                || isInvalid(currentY)
                || currentY >= cells.length
                || !(cells[currentY][currentX].isEmpty());
    }

    private boolean isShipAlreadyPlaced(Ship ship) {
        return shipsAlreadyPlaced.contains(ship.getName());
    }

    public boolean shoot(int x, int y) {
        if (isInvalid(x) || x >= cells[0].length || isInvalid(y) || y >= cells.length) {
            return false;
        }
        return cells[y][x].receiveShot();
    }

    public boolean allShipsSunk() {
        for (Cell[] cell : cells) {
            for (Cell currentCell : cell) {
                if (currentCell.isShipAlive()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isCellAlreadyTargeted(int x, int y) {
        return cells[y][x].isTargeted();
    }

    public boolean isCellEmpty(int x, int y) {
        return cells[y][x].isEmpty();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Ship getShipAt(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return cells[y][x].getShip();
        }
        return null;
    }
}
