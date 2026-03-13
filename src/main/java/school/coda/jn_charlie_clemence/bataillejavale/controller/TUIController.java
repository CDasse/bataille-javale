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

    public void launchApp(){
        boolean keepRunning = true;

        while (keepRunning){
            view.displayMainMenu();
            String choice = IO.readln();

            switch (choice){
                case "1":
                    play();
                    break;
                case "2":
                    keepRunning = false;
                    view.displayMessage("Au revoir!");
                    break;
                default:
                    view.displayMessage("Choix invalide, réeassayer!");
                    break;
            }
        }
    }

    public void play(){
        cpu.resetStrategy();

        int newWidth = view.askGridSize("largeur");
        int newHeight = view.askGridSize("hauteur");

        this.human = new HumanPlayer(human.getName(), newWidth, newHeight);
        this.cpu = new BotPlayer(cpu.getName(), newWidth, newHeight);
        this.game = new Game(human, cpu);

        setupStage();

        while (!game.isGameOver()){
            playTurn();
        }

        String winner;
        if (cpu.getGrid().allShipsSunk()){
            winner = human.getName();
        } else {
            winner = cpu.getName();
        }
        view.displayMessage("Partie terminée en " + game.getCurrentTurn() + " tours.");
        view.displayEndMenu(winner);
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
        view.displayTurnCount(game.getCurrentTurn());

        AttackResult humanResult = null;

        view.displayGrid(human.getGrid(), "--- MA FLOTTE ---", false);
        view.displayGrid(cpu.getGrid(), "--- RADAR ---", false);

        view.displayMessage("=== PHASE DE TIR ===");

        while (humanResult == null){
            int[] coords = view.askForCoordinates(cpu.getGrid().getWidth()-1, cpu.getGrid().getHeight());
            humanResult = game.nextHumanTurn(coords[0], coords[1]);

            if (humanResult == null){
                view.displayErrorMessage("Case déjà visée! Choissisez une autre cible");
                view.displayGrid(cpu.getGrid(), "--- RADAR ---", false);
            }
        }
        view.displayAttackResult(humanResult, human.getName());

        if (!game.isGameOver()) {
            view.displayMessage("\nTour de l'adversaire...");
            try { Thread.sleep(250); } catch (InterruptedException _) {};
            AttackResult cpuResult = game.nextCpuTurn();
            cpu.recordResult(cpuResult, human.getGrid());
            view.displayAttackResult(cpuResult, cpu.getName());
        }
    }
}
