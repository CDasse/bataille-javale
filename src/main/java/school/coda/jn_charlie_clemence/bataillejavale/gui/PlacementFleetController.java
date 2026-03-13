package school.coda.jn_charlie_clemence.bataillejavale.gui;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.net.URL;
import javafx.scene.media.AudioClip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.*;
import school.coda.jn_charlie_clemence.bataillejavale.logique.utils.ShipFactory;
import static school.coda.jn_charlie_clemence.bataillejavale.logique.models.EnumShip.*;
import static school.coda.jn_charlie_clemence.bataillejavale.gui.utils.CoordinateUtils.*;


public class PlacementFleetController {

    private final URL goutteSFX = getClass().getResource("/sounds/goutte.wav");
    private final AudioClip placeShipSound = (goutteSFX != null) ? new AudioClip(goutteSFX.toExternalForm()) : null;


    private void playPlacementSound() {
        if (placeShipSound != null) {
            placeShipSound.play();
        }
    }

    @FXML
    private GridPane gridPane;

    @FXML
    private Slider widthSlider;

    @FXML
    private Slider heightSlider;

    @FXML
    private Label welcomeText;

    @FXML
    private Label shipAlreadyPlaced;

    @FXML
    private Rectangle[][] casesCoordinates;

    @FXML
    private VBox playerFleetStatusBox;

    private Orientation currentOrientation = Orientation.HORIZONTAL;

    private EnumShip shipToPlace;

    private final List<Ship> playerFleet = ShipFactory.createFleet();

    private HumanPlayer humanPlayer;
    private BotPlayer botPlayer;

    @FXML
    private void addPorteAvions() {
        add(PORTEAVIONS);
    }

    @FXML
    private void addCuirasse() {
        add(CUIRASSE);
    }

    @FXML
    private void addDestroyer() {
        add(DESTROYER);
    }

    @FXML
    public void addSousMarin() {
        add(SOUSMARIN);
    }

    @FXML
    public void addPatrouilleur() {
        add(PATROUILLEUR);
    }

    private void add(EnumShip porteavions) {
        this.shipToPlace = porteavions;
        welcomeText.setText("Placement : " + shipToPlace.name);
    }

    @FXML
    protected void initializePlayers() {
        playerFleetStatusBox.getChildren().clear();
        int width = (int) widthSlider.getValue();
        int height = (int) heightSlider.getValue();

        humanPlayer = new HumanPlayer("Capitain Nemo", width, height);

        botPlayer = new BotPlayer("AI", width, height);

        initializeGrid(humanPlayer.getGrid());
        welcomeText.setText("New grid generated !");
    }

    /**
     * Visually initializes the ship placement grid in the user interface.
     * <p>
     * This method clears the current {@code GridPane}, then generates a two-dimensional
     * array of {@link Rectangle} matching the size of the logical grid. Each cell is
     * configured with its default appearance and assigned its event handlers
     * (hover for previewing and click for placement).
     * Finally, it activates the listener for changing the fleet's orientation.
     * </p>
     *
     * @param grid The player's logical grid ({@link Grid}), providing the dimensions
     * (width and height) needed to draw the interactive board.
     */
    private void initializeGrid(Grid grid) {
        this.gridPane.getChildren().clear();

        int rows = grid.getHeight();
        int cols = grid.getWidth();

        casesCoordinates = new Rectangle[rows][cols];

        showNameOfGridRows(rows, gridPane);
        showNameOfGridCols(cols, gridPane);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Rectangle cell = new Rectangle(30, 30);
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.WHITE);

                casesCoordinates[row][col] = cell;

                setOnMouseEntered(grid, cell, row, col);

                setOnMouseExited(cell, row, col);

                setOnMouseClicked(grid, cell, col, row);

                gridPane.add(cell, col + 1, row + 1);
            }
        }
        toggleOrientation();
    }

    private void setOnMouseEntered(Grid grid, Rectangle cell, int r, int c) {
        cell.setOnMouseEntered(_ -> {
            if (shipToPlace != null) {
                Ship ship = getShipToPlace(playerFleet);
                visualisationOnMouseEnter(grid, ship, r, c);
            }
        });
    }

    private void setOnMouseExited(Rectangle cell, int r, int c) {
        cell.setOnMouseExited(_ -> {
            if (shipToPlace != null) {
                Ship ship = getShipToPlace(playerFleet);
                if (ship != null) {
                    hideVisualisationOnMouseExit(r, c, ship.getSize());
                }
            }
        });
    }

    private void setOnMouseClicked(Grid grid, Rectangle cell, int c, int r) {
        cell.setOnMouseClicked(_ -> {
            if (shipToPlace != null) {
                Ship ship = getShipToPlace(playerFleet);
                assert ship != null;
                if (grid.canPlaceShip(ship, c, r, currentOrientation)) {
                    grid.placeShip(ship, c, r, currentOrientation);
                    placeShipToGrid(r, c, ship.getSize());
                    updateFleetStatus(playerFleetStatusBox, ship);
                    shipAlreadyPlaced.setText("");
                    playPlacementSound();
                } else {
                    shipAlreadyPlaced.setText("Bateau déja placé !");
                    shipAlreadyPlaced.setTextFill(Color.RED);
                }
            }
        });
    }

    private void visualisationOnMouseEnter(Grid grid, Ship ship, int row, int col) {
        boolean canPlace = grid.canPlaceShip(ship, col, row, currentOrientation);

        for (int i = 0; i < ship.getSize(); i++) {
            int targetCol = (currentOrientation == Orientation.HORIZONTAL) ? col + i : col;
            int targetRow = (currentOrientation == Orientation.HORIZONTAL) ? row : row + i;

            Rectangle neighborCell = getCellFromGrid(targetRow, targetCol);

            if (neighborCell != null && grid.isCellEmpty(targetCol, targetRow)) {
                neighborCell.setFill(canPlace ? Color.LIGHTGREEN : Color.LIGHTCORAL);
            }
        }
    }

    private void hideVisualisationOnMouseExit(int row, int col, int size) {
        for (int i = 0; i < size; i++) {

            int targetRow = (currentOrientation == Orientation.HORIZONTAL) ? row : row + i;
            int targetCol = (currentOrientation == Orientation.HORIZONTAL) ? col + i : col;

            Rectangle neighborCell = getCellFromGrid(targetRow, targetCol);

            if (neighborCell != null && humanPlayer.getGrid().isCellEmpty(targetCol, targetRow)) {
                neighborCell.setFill(Color.LIGHTBLUE);
            }
        }
    }

    private void updateFleetStatus(VBox playerFleetStatusBox, Ship ship) {
        Label statusLabel = new Label(ship.getName());
        playerFleetStatusBox.getChildren().add(statusLabel);
    }

    private void toggleOrientation() {
        gridPane.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.R) {
                currentOrientation = (currentOrientation == Orientation.HORIZONTAL)
                        ? Orientation.VERTICAL : Orientation.HORIZONTAL;
            }
        });
    }

    private Ship getShipToPlace(List<Ship> playerFleet) {
        if (shipToPlace == null) return null;
        for (Ship shipOfFleet : playerFleet) {
            if (Objects.equals(shipOfFleet.getName(), shipToPlace.name)) {
                return shipOfFleet;
            }
        }
        return null;
    }

    private void placeShipToGrid(int row, int col, int size) {
        for (int i = 0; i < size; i++) {
            int targetRow = (currentOrientation == Orientation.HORIZONTAL) ? row : row + i;
            int targetCol = (currentOrientation == Orientation.HORIZONTAL) ? col + i : col;

            Rectangle cell = getCellFromGrid(targetRow, targetCol);

            if (cell != null) cell.setFill(Color.DARKSLATEGRAY);
        }
    }

    private Rectangle getCellFromGrid(int row, int col) {
        if (casesCoordinates != null && row >= 0 && row < casesCoordinates.length && col >= 0 && col < casesCoordinates[0].length) {
            return casesCoordinates[row][col];
        }
        return null;
    }

    public void startGame(ActionEvent event) throws IOException {
        if (humanPlayer.getGrid().getListShipsPlaced().size() < 5) {
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(BatailleJavaleApplication.class.getResource("/school/coda/jn_charlie_clemence/bataillejavale/game-view.fxml"));
        Parent root = fxmlLoader.load();

        GameController gameController = fxmlLoader.getController();
        gameController.initGameWithGrid(humanPlayer, botPlayer);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, 1600, 900);
        stage.setTitle("Bataille Javal - Jeu");
        stage.setScene(scene);
        stage.show();
    }
}