package school.coda.jn_charlie_clemence.bataillejavale;

import school.coda.jn_charlie_clemence.bataillejavale.logique.models.BotPlayer;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Grid;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.HumanPlayer;
import school.coda.jn_charlie_clemence.bataillejavale.logique.rules.Game;

public class Main {
    public static void main(String[] args) {
        boolean isGodMode= args.length >= 1 && args[0].equals("test");

        BotPlayer bot = new BotPlayer("michel", 10 , 10);
        HumanPlayer player = new HumanPlayer("jn", 10, 10);
        Game partie = new Game(player, bot);

        bot.placeCpuShip();
        player.placeShip();

        Grid grilleDeJn = player.getGrid();
        Grid grilleDeMichel = bot.getGrid();

        while (!partie.isGameOver()){
            System.out.println("\n--- VOTRE FLOTTE ---");
            grilleDeJn.display(false);

            System.out.println("\n--- RADAR ---");
            grilleDeMichel.display(!isGodMode);

            int[] coordAtck = player.getNextMove(grilleDeMichel);
            partie.nextHumanTurn(coordAtck[0], coordAtck[1]);
        }

        System.out.print("La partie est terminé.\nAnnonce des résultats, le vainqueur est : ");
        if (bot.getGrid().allShipsSunk()){
            System.out.println(player.getName());
        } else {
            System.out.println(bot.getName());
        }
    }
}
