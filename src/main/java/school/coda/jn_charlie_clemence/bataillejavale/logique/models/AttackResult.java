package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

public record AttackResult(int x, int y, boolean hit, boolean sunk, Ship shipHit, boolean gameOver) {
}
