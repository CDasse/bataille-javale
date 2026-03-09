package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

public class HumanPlayer extends Player{

    public HumanPlayer(String name, int widthGrid, int heightGrid) {
        super(name, widthGrid, heightGrid);
    }

    @Override
    public int[] getNextMove() {
        return new int[0];
    }
}
