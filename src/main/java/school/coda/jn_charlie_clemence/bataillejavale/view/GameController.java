package school.coda.jn_charlie_clemence.bataillejavale.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.*;
import school.coda.jn_charlie_clemence.bataillejavale.logique.utils.ShipFactory;

import java.util.List;

public class GameController {
    
    @FXML
    private Label turnLabel;

    @FXML
    private GridPane botGridPane;

    @FXML
    private GridPane playerGridPane;
    
    @FXML
    private Label logLabel;

    private HumanPlayer humanPlayer;
    private BotPlayer botPlayer;

    Rectangle[][] humanCells;
    Rectangle[][] botCells;


    private boolean isPlayerTurn = true;
    
    @FXML
    public void initialize() {
        logLabel.setText("La bataille commence. Préparez vos canons !");

        // TODO : METTRE A JOUR AVEC LARGEUR ET HAUTEUR CHOISIES
        humanPlayer = new HumanPlayer("Capitain Nemo", 10, 10);
        List<Ship> playerFleet = ShipFactory.createFleet();
        IO.println(playerFleet);
        int ligne = 0;
        for (Ship ship : playerFleet) {
            humanPlayer.getGrid().placeShip(ship, 0, ligne, Orientation.HORIZONTAL);
            ligne += 2;
        }

        // TODO : METTRE A JOUR AVEC MEME LARGEUR ET HAUTEUR QUE JOUEUR HUMAIN
        botPlayer = new BotPlayer("AI", 10, 10);
//        botPlayer.placeCpuShip();
        Ship testShip = new Ship("Torpilleur", 2);
        botPlayer.getGrid().placeShip(testShip, 0, 0, Orientation.HORIZONTAL);
        // TODO : A INITIALISER AVEC LES HAUTEUR ET LARGEUR CHOISIES
        humanCells = new Rectangle[10][10];
        botCells = new Rectangle[10][10];

        drawGrid(playerGridPane, humanPlayer.getGrid(), false, humanCells);
        drawGrid(botGridPane, botPlayer.getGrid(), true, botCells);
    }

    private void drawGrid(GridPane playerGridPane, Grid grid, boolean isClickable, Rectangle[][] cellArray) {
        int rows = grid.getHeight();
        int cols = grid.getWidth();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Rectangle cell = new Rectangle(30, 30);
                cell.setStroke(Color.WHITE);

                if (!isClickable && !grid.isCellEmpty(col, row)) {
                    cell.setFill(Color.DARKBLUE);
                } else {
                    cell.setFill(Color.LIGHTBLUE);
                }

                cellArray[row][col] = cell;

                if (isClickable) {

                    final int r = row;
                    final int c = col;

                    cell.setOnMouseClicked(_ -> {
                        handlePlayerShot(r, c);
                    });
                }

                playerGridPane.add(cell, col, row);
            }
        }
    }

    private void handlePlayerShot(int row, int col) {
        if (!isPlayerTurn) return;

        Grid botGrid = botPlayer.getGrid();

        if (botGrid.isCellAlreadyTargeted(col, row)) {
            logLabel.setText("Vous avez déjà tiré sur ces coordonnées ! Rejouez");
            handlePlayerShot(row, col);
            return;
        }

        boolean isHit = botGrid.shoot(col, row);

        Rectangle clickedCell = botCells[row][col];

        if (isHit) {
            clickedCell.setFill(Color.RED);

            if (botGrid.allShipsSunk()) {
                logLabel.setText("VICTOIRE ! Tous les navires ennemis sont au fond de l'océan !");
                isPlayerTurn = false;
                return;
            } else {
                logLabel.setText("Navire ennemi TOUCHÉ en [" + col + "," + row + "] !");
            }
        } else {
            clickedCell.setFill(Color.DARKGRAY);
            logLabel.setText("Tir à l'eau en [" + col + "," + row + "].");
        }

        isPlayerTurn = false;
        turnLabel.setText("Tour de l'adversaire...");
        playBotTurn();
    }

    private void playBotTurn() {

        int[] botMove = botPlayer.getNextMove();
        int botX = botMove[0]; // col
        int botY = botMove[1]; // row

        Grid playerGrid = humanPlayer.getGrid();
        boolean isHit = playerGrid.shoot(botX, botY);

        Rectangle attackedCell = humanCells[botY][botX];

        if (isHit) {
            attackedCell.setFill(Color.RED);

            if (playerGrid.allShipsSunk()) {
                logLabel.setText("DÉFAITE... Notre flotte a été anéantie.");
                return;
            } else {
                logLabel.setText("Alerte ! Le Bot a touché notre navire en [" + botX + "," + botY + "] !");
            }
        } else {
            attackedCell.setFill(Color.DARKGRAY);
            logLabel.setText("Le Bot a tiré à l'eau en [" + botX + "," + botY + "].");
        }

        isPlayerTurn = true;
        turnLabel.setText("À votre tour!");
    }
}