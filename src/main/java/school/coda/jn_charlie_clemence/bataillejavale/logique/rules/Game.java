package school.coda.jn_charlie_clemence.bataillejavale.logique.rules;

import school.coda.jn_charlie_clemence.bataillejavale.logique.models.*;

public class Game {
    private final Player player;
    private final Player cpu;
    private Player currentPlayer;
    private boolean isGameEnded;
    private int currentTurn;

    public Game(Player player, Player cpu) {
        this.player = player;
        this.cpu = cpu;
        this.currentPlayer = player;
        this.isGameEnded = false;
        this.currentTurn = 1;
    }

    public boolean isGameOver() {
        return (player.getGrid().allShipsSunk()) || (cpu.getGrid().allShipsSunk());
    }

    private AttackResult executeShot(int x, int y) {
        if (this.isGameEnded) return null;

        Player opponent = (this.currentPlayer == player) ? cpu : player;
        boolean hit = opponent.getGrid().shoot(x, y);
        Ship shipHit = opponent.getGrid().getShipAt(x, y);
        boolean sunk = (shipHit != null && shipHit.isSunk());

        if (isGameOver()) {
            this.isGameEnded = true;
        }

        AttackResult result = new AttackResult(x, y, hit, sunk, shipHit, this.isGameEnded);

        if (!this.isGameEnded){
            this.currentPlayer = opponent;
        }

        return result;
    }

    public AttackResult nextHumanTurn(int x, int y) {
        if (this.currentPlayer instanceof BotPlayer) return null;
        if (cpu.getGrid().isCellAlreadyTargeted(x, y)) return null;
        return  executeShot(x, y);

    }

    public AttackResult nextCpuTurn() {
        int[] coords = cpu.getNextMove(player.getGrid());

        AttackResult result = executeShot(coords[0], coords[1]);

        if (!this.isGameEnded) {
            this.currentTurn++;
        }

        return result;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }
}
