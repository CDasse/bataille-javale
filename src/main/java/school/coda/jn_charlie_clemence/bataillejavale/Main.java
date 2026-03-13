package school.coda.jn_charlie_clemence.bataillejavale;

import school.coda.jn_charlie_clemence.bataillejavale.controller.TUIController;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.BotPlayer;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Grid;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.HumanPlayer;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Player;
import school.coda.jn_charlie_clemence.bataillejavale.logique.rules.Game;
import school.coda.jn_charlie_clemence.bataillejavale.view.tui.ConsoleView;

import javax.swing.text.View;

public class Main {
    public static void main(String[] args) {
        Player human = new HumanPlayer("JN", 13, 13);
        BotPlayer cpu = new BotPlayer("ORDINATEUR", 13, 13);

        Game game = new Game(human, cpu);
        ConsoleView view = new ConsoleView();

        TUIController tui = new TUIController(view, game, human, cpu);

        tui.launchApp();

    }
}
