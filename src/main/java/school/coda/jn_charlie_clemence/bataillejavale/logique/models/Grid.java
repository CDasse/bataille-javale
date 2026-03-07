package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

public class Grid {
    private final Cell[][] cells;

    public Grid(int width, int height) {
        this.cells = new Cell[width][height];
        for (int row = 0; row < width; row++){
            for (int column = 0; column < height; column++){
                cells[row][column] = new Cell();
            }
        }
    }

    public void placeShip(Ship ship, int x, int y) {
        if (x >= 0 && x < cells.length && y >= 0 && y < cells[0].length && cells[x][y].isEmpty()) {
            cells[x][y].setShip(ship);
        }
    }

    public boolean shoot(int x, int y){
        if (x < 0 || x >= cells.length || y < 0 || y >= cells[0].length) {
            return false;
        }
        return cells[x][y].receiveShot();
    }
}
