package school.coda.jn_charlie_clemence.bataillejavale.controller;

import school.coda.jn_charlie_clemence.bataillejavale.logique.models.*;
import school.coda.jn_charlie_clemence.bataillejavale.logique.rules.Game;
import school.coda.jn_charlie_clemence.bataillejavale.view.tui.ConsoleView;

public class TUIController {
    private Game game;
    private ConsoleView view;
    private Player human;
    private BotPlayer cpu;

    public TUIController(ConsoleView view, Game game, Player player, BotPlayer cpu) {
        this.view = view;
        this.game = game;
        this.human = player;
        this.cpu = cpu;
    }

    public void play(){
        setupStage();

        while (!game.isGameOver()){
            playTurn();
        }

        view.displayMessage("=== LA PARTIE EST TERMINÉ ===");
        if (cpu.getGrid().allShipsSunk()){
            view.displayMessage("!!!!! Félicitations vous avez remporté cette partie !!!!!");
        } else {
            view.displayMessage("!!!!! Dommage... l'ordinateur a remporté cette partie!!!!!");
        }
    }

    public void setupStage(){
        view.displayMessage("=== PHASE DE PLACEMENT ===");

        for (Ship ship : human.getShips()){
            boolean placed = false;
            while (!placed){
                view.displayGrid(human.getGrid(), "--- MA FLOTTE ---", false);
                view.displayPlacementHeader(ship.getName(), ship.getSize());
                int[] coords = view.askForCoordinates(human.getGrid().getWidth()-1, human.getGrid().getHeight());
                Orientation orientation = view.askForShipOrientation();
                placed = human.getGrid().placeShip(ship, coords[0], coords[1], orientation);

                if (!placed){
                    view.displayErrorMessage("Position impossible(hors-grille ou collision). Veuillez réessayer!");
                }
            }
        }

        view.displayMessage("\nL'ordinateur est en train de placer ses navires...");
        cpu.placeCpuShip();
    }

    public void playTurn(){
        AttackResult humanResult = null;

        view.displayGrid(human.getGrid(), "--- MA FLOTTE ---", false);
        view.displayGrid(cpu.getGrid(), "--- RADAR ---", false);

        view.displayMessage("=== PHASE DE TIR ===");

        while (humanResult == null){
            int[] coords = view.askForCoordinates(cpu.getGrid().getWidth()-1, cpu.getGrid().getHeight());
            humanResult = game.nextHumanTurn(coords[0], coords[1]);

            if (humanResult == null){
                view.displayErrorMessage("Case déjà visée! Choissisez une autre cible");
            }
        }
        view.displayAttackResult(humanResult, human.getName());

        if (!game.isGameOver()) {
            view.displayMessage("\nTour de l'adversaire...");
            try { Thread.sleep(1500); } catch (InterruptedException e) {};
            AttackResult cpuResult = game.nextCpuTurn();
            view.displayAttackResult(cpuResult, cpu.getName());
        }
    }
}
