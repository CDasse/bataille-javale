package school.coda.jn_charlie_clemence.bataillejavale.view;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.*;


public class GameController {
    
    @FXML
    private Label turnLabel;

    @FXML
    private GridPane playerGridPane;

    @FXML
    private VBox playerFleetStatusBox;

    @FXML
    private GridPane botGridPane;

    @FXML
    private VBox botFleetStatusBox;

    @FXML
    private TextArea logTextArea;

    private HumanPlayer humanPlayer;
    private BotPlayer botPlayer;

    private Rectangle[][] humanCells;
    private Rectangle[][] botCells;

    private boolean isPlayerTurn = true;
    private int gameTurn = 1;
    
    @FXML
    public void initialize() {
        logTextArea.appendText("La bataille commence. Préparez vos canons !\n\n");
    }

    public void initGameWithGrid(HumanPlayer humanPlayer, BotPlayer botPlayer) {
        this.humanPlayer = humanPlayer;
        this.botPlayer = botPlayer;

        botPlayer.placeCpuShip();

        Grid playerGrid = humanPlayer.getGrid();

        humanCells = new Rectangle[playerGrid.getHeight()][playerGrid.getWidth()];
        botCells = new Rectangle[playerGrid.getHeight()][playerGrid.getWidth()];

        drawGrid(playerGridPane, humanPlayer.getGrid(), false, humanCells);
        drawGrid(botGridPane, botPlayer.getGrid(), true, botCells);

        updateFleetStatus(humanPlayer, playerFleetStatusBox);
        updateFleetStatus(botPlayer, botFleetStatusBox);
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

                    cell.setOnMouseClicked(_ -> handlePlayerShot(r, c));
                }
                playerGridPane.add(cell, col, row);
            }
        }
    }

    private void handlePlayerShot(int row, int col) {
        if (!isPlayerTurn) return;

        Grid botGrid = botPlayer.getGrid();

        if (botGrid.isCellAlreadyTargeted(col, row)) {
            logTextArea.appendText("Vous avez déjà tiré sur ces coordonnées ! Rejouez\n");
            return;
        }

        boolean isHit = botGrid.shoot(col, row);

        Rectangle clickedCell = botCells[row][col];

        if (isHit) {
            clickedCell.setFill(Color.RED);
            if (botGrid.allShipsSunk()) {
                logTextArea.appendText("VICTOIRE ! Tous les navires ennemis sont au fond de l'océan !\n");
                isPlayerTurn = false;
                return;
            } else {
                logTextArea.appendText("Navire ennemi TOUCHÉ en [" + col + "," + row + "] !\n");
            }
        } else {
            clickedCell.setFill(Color.DARKGRAY);
            logTextArea.appendText("Tir à l'eau en [" + col + "," + row + "].\n");
        }

        isPlayerTurn = false;
        updateFleetStatus(botPlayer, botFleetStatusBox);

        turnLabel.setText("L'adversaire réfléchit...");
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(_ -> playBotTurn());
        pause.play();
    }

    private void playBotTurn() {
        Grid playerGrid = humanPlayer.getGrid();
        int[] botMove = botPlayer.getNextMove(playerGrid);
        int botX = botMove[0]; // col
        int botY = botMove[1]; // row


        boolean isHit = playerGrid.shoot(botX, botY);

        Rectangle attackedCell = humanCells[botY][botX];

        if (isHit) {
            attackedCell.setFill(Color.RED);

            if (playerGrid.allShipsSunk()) {
                logTextArea.appendText("DÉFAITE... Notre flotte a été anéantie.\n");
                return;
            } else {
                logTextArea.appendText("Alerte ! Le Bot a touché notre navire en [" + botX + "," + botY + "] !\n");
            }
        } else {
            attackedCell.setFill(Color.DARKGRAY);
            logTextArea.appendText("Le Bot a tiré à l'eau en [" + botX + "," + botY + "].\n");
        }

        isPlayerTurn = true;
        gameTurn++;
        updateFleetStatus(humanPlayer, playerFleetStatusBox);
        turnLabel.setText("Tour " + gameTurn);
    }

    private void updateFleetStatus(Player player, VBox statusBox) {
        statusBox.getChildren().clear();

        for (Ship ship : player.getShips()) {
            if (ship.isSunk()) {
                Label statusLabel = new Label(ship.getName() + " - COULÉ ☠️");
                statusLabel.setTextFill(Color.DARKRED);
                statusBox.getChildren().add(statusLabel);
            }
        }

        if (statusBox.getChildren().isEmpty()) {
            Label allSafeLabel = new Label("Tous les navires sont à flot.");
            allSafeLabel.setTextFill(Color.GRAY);
            statusBox.getChildren().add(allSafeLabel);
        }
    }
}