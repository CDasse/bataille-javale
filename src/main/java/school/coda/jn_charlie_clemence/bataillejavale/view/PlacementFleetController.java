package school.coda.jn_charlie_clemence.bataillejavale.view;

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

import javafx.scene.media.AudioClip;
import java.net.URL;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class PlacementFleetController {

    private final URL goutteSFX = getClass().getResource("/sounds/goutte.mp3");
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
        this.shipToPlace = EnumShip.PORTEAVIONS;
        welcomeText.setText("Placement : Porte-Avions");
    }

    @FXML
    private void addCuirasse() {
        this.shipToPlace = EnumShip.CUIRASSE;
        welcomeText.setText("Placement : Cuirassé");
    }

    @FXML
    private void addDestroyer() {
        this.shipToPlace = EnumShip.DESTROYER;
        welcomeText.setText("Placement : Destroyer");
    }

    @FXML
    public void addSousMarin() {
        this.shipToPlace = EnumShip.SOUSMARIN;
        welcomeText.setText("Placement : Sous-marin");
    }

    @FXML
    public void addPatrouilleur() {
        this.shipToPlace = EnumShip.PATROUILLEUR;
        welcomeText.setText("Placement : Patrouilleur");
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

    private void initializeGrid(Grid grid) {
        this.gridPane.getChildren().clear();

        int rows = grid.getHeight();
        int cols = grid.getWidth();

        casesCoordinates = new Rectangle[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Rectangle cell = new Rectangle(20, 20);
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.WHITE);

                final int r = row;
                final int c = col;

                casesCoordinates[row][col] = cell;

                cell.setOnMouseEntered(_ -> {
                    if (shipToPlace != null) {
                        Ship ship = getShipToPlace(playerFleet);
                        visualisationOnMouseEnter(grid, ship, r, c);
                    }
                });

                cell.setOnMouseExited(_ -> {
                    if (shipToPlace != null) {
                        Ship ship = getShipToPlace(playerFleet);
                        if (ship != null) {
                            hideVisualisationOnMouseExit(r, c, ship.getSize());
                        }
                    }
                });

                cell.setOnMouseClicked(_ -> {
                    if (shipToPlace != null) {
                        Ship ship = getShipToPlace(playerFleet);
                        assert ship != null;
                        if (grid.canPlaceShip(ship, c, r, currentOrientation)) {
                            grid.placeShip(ship, c, r, currentOrientation);
                            fixShipToGrid(r, c, ship.getSize());
                            updateFleetStatus(playerFleetStatusBox, ship);
                            shipAlreadyPlaced.setText("");
                            playPlacementSound();
                        } else {
                            shipAlreadyPlaced.setText("Bateau déja placé !");
                            shipAlreadyPlaced.setTextFill(Color.RED);
                        }
                    }
                });
                gridPane.add(cell, col, row);
            }
        }
        toggleOrientation();
    }

    private void visualisationOnMouseEnter(Grid grid, Ship ship, int row, int col) {
        boolean canPlace = grid.canPlaceShip(ship, col, row, currentOrientation);

        for (int i = 0; i < ship.getSize(); i++) {
            Rectangle voisin = getTargetCell(row, col, i);
            if (voisin != null) {
                int targetCol = (currentOrientation == Orientation.HORIZONTAL) ? col + i : col;
                int targetRow = (currentOrientation == Orientation.HORIZONTAL) ? row : row + i;

                if (grid.isCellEmpty(targetCol, targetRow)) {
                    voisin.setFill(canPlace ? Color.LIGHTGREEN : Color.LIGHTCORAL);
                }
            }
        }

    }

    private void hideVisualisationOnMouseExit(int row, int col, int size) {
        for (int i = 0; i < size; i++) {

            Rectangle voisin = getTargetCell(row, col, i);

            if (voisin != null) {
                int targetRow = (currentOrientation == Orientation.HORIZONTAL) ? row : row + i;
                int targetCol = (currentOrientation == Orientation.HORIZONTAL) ? col + i : col;

                if (voisin != null && humanPlayer.getGrid().isCellEmpty(targetCol, targetRow)) {
                    voisin.setFill(Color.LIGHTBLUE);
                }
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

    private void fixShipToGrid(int row, int col, int size) {
        for (int i = 0; i < size; i++) {
            Rectangle cell = getTargetCell(row, col, i);
            if (cell != null) cell.setFill(Color.DARKSLATEGRAY);
        }
    }

    private Rectangle getCellFromGrid(int row, int col) {
        if (casesCoordinates != null && row >= 0 && row < casesCoordinates.length && col >= 0 && col < casesCoordinates[0].length) {
            return casesCoordinates[row][col];
        }
        return null;
    }

    public void gameButton(ActionEvent event) throws IOException {
        if (humanPlayer.getGrid().getListShipsPlaced().size() < 5) {
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/school/coda/jn_charlie_clemence/bataillejavale/game-view.fxml"));
        Parent root = fxmlLoader.load();

        GameController gameController = fxmlLoader.getController();
        gameController.initGameWithGrid(humanPlayer, botPlayer);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, 1080, 720);
        stage.setTitle("Bataille Javal");
        stage.setScene(scene);
        stage.show();
    }

    private Rectangle getTargetCell(int startRow, int startCol, int index) {
        int targetR = (currentOrientation == Orientation.HORIZONTAL) ? startRow : startRow + index;
        int targetC = (currentOrientation == Orientation.HORIZONTAL) ? startCol + index : startCol;
        return getCellFromGrid(targetR, targetC);
    }
}