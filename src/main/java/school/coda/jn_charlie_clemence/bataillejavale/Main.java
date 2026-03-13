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
        ConsoleView view = new ConsoleView();
        Player human = new HumanPlayer("JOUEUR", 10, 10);
        BotPlayer cpu = new BotPlayer("ORDINATEUR", 10, 10);
        Game game = new Game(human, cpu);


        TUIController tui = new TUIController(view, game, human, cpu);

        tui.launchApp();

    }
}
