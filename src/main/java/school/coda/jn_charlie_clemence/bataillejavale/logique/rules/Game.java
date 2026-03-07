package school.coda.jn_charlie_clemence.bataillejavale.logique.rules;

import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Player;

public class Game {
    private final Player player;
    private final Player cpu;
    private Player currentPlayer;
    private boolean isGameEnded;

    public Game(Player player, Player cpu) {
        this.player = player;
        this.cpu = cpu;
        this.currentPlayer = player;
        this.isGameEnded = false;
    }

    public void nextTurn(int x, int y) {
        Player opponent;

        if (this.isGameEnded) return;

        if (this.currentPlayer == player) {
            opponent = this.cpu;
        } else {
            opponent = this.player;
        }

        opponent.getGrid().shoot(x, y);

        if (isGameOver()) {
            this.isGameEnded = true;
        } else {
            this.currentPlayer = opponent;
        }
    }

    public boolean isGameOver () {
        return (player.getGrid().allShipsSunk()) || (cpu.getGrid().allShipsSunk());
    }
}
