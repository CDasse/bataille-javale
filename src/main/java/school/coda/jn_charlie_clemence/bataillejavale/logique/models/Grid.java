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
        if (orientation == HORIZONTAL) {
            if (x < 0 || x + ship.getSize() > this.width) return false;
        } else {
            if (y < 0 || y + ship.getSize() > this.height) return false;
        }
        for (int i = 0; i < ship.getSize(); i++) {
            int currentX = (orientation == HORIZONTAL) ? x + i : x;
            int currentY = (orientation == VERTICAL) ? y + i : y;

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

    public void display (boolean isRadar) {
        //Affichage des nombres en haut
        IO.print("   ");
        for (int i = 0; i < width; i++){
            IO.print(i + " ");
        }
        IO.println("");

        for (int row = 0; row < height; row++) {
            //Affichage des lettres à gauche
            char letter = (char) ('A' + row);
            IO.print(letter + "  ");
            for (int column = 0; column < width; column++) {
                Cell cell = cells[row][column];
                //Affichage de la grille
                String symbol = "~ ";
                if (cell.isTargeted()) {
                    symbol = cell.isEmpty() ? "O " : "X ";
                } else if (!isRadar && !cell.isEmpty()) {
                    symbol = "S ";
                }
                IO.print(symbol);
            }
            IO.println("");
        }
    }
}
