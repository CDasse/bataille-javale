package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class BotPlayerTest {

    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;

    /**
     * Test Double (Stub) pour isoler Random de manière déterministe
     * sans dépendre des mécanismes d'instrumentation bytecode sur java.base.
     */
    private static class StubRandom extends Random {
        private final int[] sequence;
        private int index = 0;

        StubRandom(int... sequence) {
            this.sequence = sequence;
        }

        @Override
        public int nextInt(int bound) {
            if (index < sequence.length) {
                return sequence[index++];
            }
            return sequence[sequence.length - 1];
        }
    }

    @Test
    @DisplayName("Doit retourner directement la dernière cible valide empilée dans nextTargets")
    void shouldReturnLastPushedTarget_whenNextTargetsContainsValidCoordinates() {
        // Arrange
        Grid playerGrid = new Grid(GRID_HEIGHT, GRID_WIDTH);
        BotPlayer bot = new BotPlayer("Bot", GRID_WIDTH, GRID_HEIGHT);
        AttackResult hitResult = new AttackResult(5, 5, true, false, null, false);
        bot.recordResult(hitResult, playerGrid);

        // Act
        int[] nextMove = bot.getNextMove(playerGrid);

        // Assert : le dernier voisin valide empilé par recordResult (voisin gauche [4, 5]) est dépilé
        assertThat(nextMove).containsExactly(4, 5);
    }

    @Test
    @DisplayName("Doit ignorer les cases empilées déjà ciblées et retourner la première case libre")
    void shouldSkipAlreadyTargetedCellsInStack_andReturnFirstAvailableTarget() {
        // Arrange
        Grid playerGrid = new Grid(GRID_HEIGHT, GRID_WIDTH);
        BotPlayer bot = new BotPlayer("Bot", GRID_WIDTH, GRID_HEIGHT);
        AttackResult hitResult = new AttackResult(5, 5, true, false, null, false);
        bot.recordResult(hitResult, playerGrid);
        playerGrid.shoot(4, 5);

        // Act
        int[] nextMove = bot.getNextMove(playerGrid);

        // Assert : la case (4, 5) étant déjà visée, elle est ignorée et la suivante (5, 6) est renvoyée
        assertThat(nextMove).containsExactly(5, 6);
    }

    @Test
    @DisplayName("Doit vider la pile de cibles déjà visées et basculer sur un tir aléatoire")
    void shouldFallbackToRandomShot_whenAllStackedTargetsAreAlreadyTargeted() {
        // Arrange
        Grid playerGrid = new Grid(GRID_HEIGHT, GRID_WIDTH);
        Random stubRandom = new StubRandom(1, 2);

        BotPlayer bot = new BotPlayer("Bot", GRID_WIDTH, GRID_HEIGHT, stubRandom);
        AttackResult hitResult = new AttackResult(5, 5, true, false, null, false);
        bot.recordResult(hitResult, playerGrid);

        playerGrid.shoot(5, 4);
        playerGrid.shoot(6, 5);
        playerGrid.shoot(5, 6);
        playerGrid.shoot(4, 5);

        // Act
        int[] nextMove = bot.getNextMove(playerGrid);

        // Assert : la pile étant entièrement épuisée, le repli aléatoire fournit (1, 2)
        assertThat(nextMove).containsExactly(1, 2);
    }

    @Test
    @DisplayName("Doit utiliser le tir aléatoire nominal lorsque la pile nextTargets est vide")
    void shouldUseRandomCoordinates_whenTargetStackIsEmpty() {
        // Arrange
        Grid playerGrid = new Grid(GRID_HEIGHT, GRID_WIDTH);
        Random stubRandom = new StubRandom(3, 7);
        BotPlayer bot = new BotPlayer("Bot", GRID_WIDTH, GRID_HEIGHT, stubRandom);

        // Act
        int[] nextMove = bot.getNextMove(playerGrid);

        // Assert
        assertThat(nextMove).containsExactly(3, 7);
    }

    @Test
    @DisplayName("Doit re-tirer une coordonnée aléatoire lorsqu'une collision survient avec une case déjà visée")
    void shouldRetryRandomCoordinates_whenFirstRandomTargetIsAlreadyTargeted() {
        // Arrange
        Grid playerGrid = new Grid(GRID_HEIGHT, GRID_WIDTH);
        playerGrid.shoot(2, 2);

        // Premier tirage aléatoire sur (2, 2) déjà ciblée, puis second tirage sur (4, 4) libre
        Random stubRandom = new StubRandom(2, 2, 4, 4);
        BotPlayer bot = new BotPlayer("Bot", GRID_WIDTH, GRID_HEIGHT, stubRandom);

        // Act
        int[] nextMove = bot.getNextMove(playerGrid);

        // Assert : la boucle do-while rejette (2, 2) et retourne la case libre (4, 4)
        assertThat(nextMove).containsExactly(4, 4);
    }

    @ParameterizedTest(name = "Tir réussi en ({0}, {1}) -> prochaine cible prioritaire attendue en ({2}, {3})")
    @CsvSource({
            "0, 0, 0, 1",
            "0, 5, 0, 6",
            "5, 5, 4, 5",
            "9, 9, 8, 9"
    })
    @DisplayName("Doit cibler la cellule adjacente valide selon la position du tir (angles, bords, centre)")
    void shouldTargetCorrectAdjacentCell_whenHitAtVariousGridPositions(int hitX, int hitY, int expectedNextX, int expectedNextY) {
        // Arrange
        Grid playerGrid = new Grid(GRID_HEIGHT, GRID_WIDTH);
        BotPlayer bot = new BotPlayer("Bot", GRID_WIDTH, GRID_HEIGHT);
        AttackResult hitResult = new AttackResult(hitX, hitY, true, false, null, false);

        // Act
        bot.recordResult(hitResult, playerGrid);
        int[] nextMove = bot.getNextMove(playerGrid);

        // Assert
        assertThat(nextMove).containsExactly(expectedNextX, expectedNextY);
    }

    @Test
    @DisplayName("Ne doit empiler aucune cible lorsque le tir est manqué")
    void shouldNotPushTargets_whenAttackMisses() {
        // Arrange
        Grid playerGrid = new Grid(GRID_HEIGHT, GRID_WIDTH);
        Random stubRandom = new StubRandom(8, 9);
        BotPlayer bot = new BotPlayer("Bot", GRID_WIDTH, GRID_HEIGHT, stubRandom);
        AttackResult missResult = new AttackResult(5, 5, false, false, null, false);

        // Act
        bot.recordResult(missResult, playerGrid);
        int[] nextMove = bot.getNextMove(playerGrid);

        // Assert : aucune case empilée, bascule directe sur l'aléa (8, 9)
        assertThat(nextMove).containsExactly(8, 9);
    }

    @Test
    @DisplayName("Doit réinitialiser la stratégie et vider la pile des cibles empilées lors de resetStrategy")
    void shouldClearStackedTargets_whenResetStrategyIsCalled() {
        // Arrange
        Grid playerGrid = new Grid(GRID_HEIGHT, GRID_WIDTH);
        Random stubRandom = new StubRandom(0, 0);
        BotPlayer bot = new BotPlayer("Bot", GRID_WIDTH, GRID_HEIGHT, stubRandom);
        AttackResult hitResult = new AttackResult(5, 5, true, false, null, false);
        bot.recordResult(hitResult, playerGrid);

        // Act
        bot.resetStrategy();
        int[] nextMove = bot.getNextMove(playerGrid);

        // Assert : la pile a été vidée, donc retour aléatoire
        assertThat(nextMove).containsExactly(0, 0);
    }
}
