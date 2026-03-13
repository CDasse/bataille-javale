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
import school.coda.jn_charlie_clemence.bataillejavale.logique.rules.Game;

import java.util.HashMap;
import java.util.Map;


public class GameController {
    @FXML
    private GridPane playerGridPane;

    @FXML
    private GridPane botGridPane;

    @FXML
    private VBox playerFleetStatusBox;

    @FXML
    private VBox botFleetStatusBox;

    @FXML
    private Label turnLabel;

    @FXML
    private TextArea logTextArea;

    private final Map<Ship, Label> humanShipLabels = new HashMap<>();
    private final Map<Ship, Label> botShipLabels = new HashMap<>();

    private Rectangle[][] humanCells;
    private Rectangle[][] botCells;

    private Game game;

    @FXML
    public void initialize() {
        logTextArea.appendText("La bataille commence. Préparez vos canons !\n\n");
    }

    public void initGameWithGrid(HumanPlayer humanPlayer, BotPlayer botPlayer) {
        game = new Game(humanPlayer, botPlayer);

        botPlayer.placeCpuShip();

        Grid playerGrid = humanPlayer.getGrid();

        humanCells = new Rectangle[playerGrid.getHeight()][playerGrid.getWidth()];
        botCells = new Rectangle[playerGrid.getHeight()][playerGrid.getWidth()];

        drawGrid(playerGridPane, humanPlayer.getGrid(), false, humanCells);
        drawGrid(botGridPane, botPlayer.getGrid(), true, botCells);

        initFleetStatus(humanPlayer, playerFleetStatusBox, humanShipLabels);
        initFleetStatus(botPlayer, botFleetStatusBox, botShipLabels);
    }

    private void drawGrid(GridPane playerGridPane, Grid grid, boolean isRadarGrid, Rectangle[][] cellArray) {
        int rows = grid.getHeight();
        int cols = grid.getWidth();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Rectangle cell = new Rectangle(30, 30);
                cell.setStroke(Color.WHITE);

                boolean isOceanCellWithShip = !isRadarGrid && !grid.isCellEmpty(col, row);
                if (isOceanCellWithShip) {
                    cell.setFill(Color.DARKBLUE);
                } else {
                    cell.setFill(Color.LIGHTBLUE);
                }

                cellArray[row][col] = cell;

                if (isRadarGrid) {

                    final int r = row;
                    final int c = col;

                    cell.setOnMouseClicked(_ -> handlePlayerShot(r, c));
                }
                playerGridPane.add(cell, col, row);
            }
        }
    }

    private void handlePlayerShot(int row, int col) {
        AttackResult result = game.nextHumanTurn(col, row);

        // return if it's not player turn or the cell has already been clicked
        if (result == null) {
            return;
        }

        Rectangle clickedCell = botCells[row][col];

        char letterRow = (char) ('A' + row);

        if (result.hit()) {
            clickedCell.setFill(Color.RED);
            if (result.sunk()) {
                logTextArea.appendText("BOUM ! Le " + result.shipHit().getName() + " ennemi a été COULÉ !\n");
                markShipAsSunk(result, botShipLabels);
            } else {
                logTextArea.appendText("Navire ennemi TOUCHÉ en [" + (col + 1) + "-" + letterRow + "] !\n");
            }
        } else {
            clickedCell.setFill(Color.DARKGRAY);
            logTextArea.appendText("Tir à l'eau en [" + (col + 1) + "-" + letterRow + "].\n");
        }

        if (result.gameOver()) {
            logTextArea.appendText("VICTOIRE ! Tous les navires ennemis sont au fond de l'océan !\n");
            return;
        }

        turnLabel.setText("L'adversaire réfléchit...");
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(_ -> handleBotShot());
        pause.play();
    }

    private void handleBotShot() {
        AttackResult result = game.nextCpuTurn();

        Rectangle attackedCell = humanCells[result.y()][result.x()];

        char letterRow = (char) ('A' + result.y());

        if (result.hit()) {
            attackedCell.setFill(Color.RED);
            if (result.sunk()) {
                logTextArea.appendText("OUPS ! L'adversaire a COULÉ ton " + result.shipHit().getName() + " !\n");
                markShipAsSunk(result, humanShipLabels);
            } else {
                logTextArea.appendText("Alerte ! L'adversaire' a touché votre navire en [" + (result.x() + 1) + "-" + letterRow + "] !\n");
            }
        } else {
            attackedCell.setFill(Color.DARKGRAY);
            logTextArea.appendText("L'adversaire a tiré à l'eau en [" + (result.x() + 1) + "-" + letterRow + "].\n");
        }

        if (result.gameOver()) {
            logTextArea.appendText("DÉFAITE... Votre flotte a été anéantie.\n");
            return;
        }

        turnLabel.setText("Tour " + result.currentTurn());
    }

    private void initFleetStatus(Player player, VBox statusBox, Map<Ship, Label> labelsMap) {
        statusBox.getChildren().clear();
        labelsMap.clear();

        for (Ship ship : player.getShips()) {
            Label statusLabel = new Label(ship.getName());
            statusLabel.setTextFill(Color.DARKGREEN);
            statusBox.getChildren().add(statusLabel);

            labelsMap.put(ship, statusLabel);
        }
    }

    private void markShipAsSunk(AttackResult result, Map<Ship, Label> labelsMap) {
        Label labelToUpdate = labelsMap.get(result.shipHit());

        labelToUpdate.setText(result.shipHit().getName() + "- COULÉ ☠️");
        labelToUpdate.setTextFill(Color.DARKRED);
    }
}