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
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.EnumShip;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Grid;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Ship;
import school.coda.jn_charlie_clemence.bataillejavale.logique.utils.ShipFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class PlacementFleetController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Slider widthSlider;

    @FXML
    private Slider heightSlider;

    @FXML
    private Label welcomeText;

    @FXML
    private VBox scene;

    private Orientation currentOrientation = Orientation.HORIZONTAL;

    private Grid grid;

    @FXML
    protected void initializeGridView() {
        int width = (int) widthSlider.getValue();
        int height = (int) heightSlider.getValue();

        initializeGrid(height,width);
        welcomeText.setText("New grid generated !");
    }

    List<Ship> playerFleet = ShipFactory.createFleet();

    private EnumShip shipToPlace;

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
    private Rectangle[][] casesCoordinates;

    private void initializeGrid(int height,int width) {
        this.grid = new Grid(height, width);
        this.gridPane.getChildren().clear();

        int rows = grid.getWidth();
        int cols = grid.getHeight();

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
                        Ship ship = shipToPlaceTest(playerFleet);
                        visualisationOnMouseEnter(grid, ship, r, c);
                    }
                });

                cell.setOnMouseExited(_ -> {
                    if (shipToPlace != null) {
                        Ship ship = shipToPlaceTest(playerFleet);
                        hideVisualisationOnMouseExit(r, c, ship.getSize());
                    }
                });

                cell.setOnMouseClicked(_ -> {
                    if (shipToPlace != null) {
                        Ship ship = shipToPlaceTest(playerFleet);
                        if (grid.canPLaceShip(ship, r, c, currentOrientation)) {
                            grid.placeShip(ship, r, c, currentOrientation);
                            fixerBateauSurVisuel(r, c, ship.getSize());
                        }
                    }
                });

                gridPane.add(cell, col, row);
            }
        }
        toggleOrientation();
    }

    private Ship shipToPlaceTest(List<Ship> playerFleet) {
        if (shipToPlace == null) return null;
        for (Ship shipOfFleet : playerFleet) {
            if (Objects.equals(shipOfFleet.getName(), shipToPlace.name)) {
                return shipOfFleet;
            }
        }
        return new Ship("error", 1);
    }

    private void hideVisualisationOnMouseExit(int row, int col, int size) {
        for (int i = 0; i < size; i++) {
            int targetR = (currentOrientation == Orientation.HORIZONTAL) ? row : row + i;
            int targetC = (currentOrientation == Orientation.HORIZONTAL) ? col + i : col;

            Rectangle voisin = getCellFromGrid(targetR, targetC);
            if (voisin != null && grid.isCellEmpty(targetR, targetC)) {
                voisin.setFill(Color.LIGHTBLUE);
            }
        }
    }

    private void fixerBateauSurVisuel(int row, int col, int size) {
        for (int i = 0; i < size; i++) {
            int targetR = (currentOrientation == Orientation.HORIZONTAL) ? row : row + i;
            int targetC = (currentOrientation == Orientation.HORIZONTAL) ? col + i : col;

            Rectangle b = getCellFromGrid(targetR, targetC);
            if (b != null) b.setFill(Color.DARKSLATEGRAY);
        }
    }

    private void visualisationOnMouseEnter(Grid grid, Ship ship, int row, int col) {
        for (int i = 0; i < ship.getSize(); i++) {
            int targetR = (currentOrientation == Orientation.HORIZONTAL) ? row : row + i;
            int targetC = (currentOrientation == Orientation.HORIZONTAL) ? col + i : col;

            Rectangle voisin = getCellFromGrid(targetR, targetC);
            setColorGrid(grid, ship, row, col, voisin);
        }
    }

    private void setColorGrid(Grid grid, Ship ship, int row, int col, Rectangle voisin) {
        if (grid.canPLaceShip(ship, row, col, currentOrientation)) {
            if (voisin != null) voisin.setFill(Color.LIGHTGREEN);
        } else {
            if (voisin != null) voisin.setFill(Color.LIGHTCORAL);
        }
    }

    private void toggleOrientation() {
        gridPane.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.R) {
                currentOrientation = (currentOrientation == Orientation.HORIZONTAL)
                        ? Orientation.VERTICAL : Orientation.HORIZONTAL;
            }
        });
    }

    private Rectangle getCellFromGrid(int row, int col) {
        if (casesCoordinates != null && row >= 0 && row < casesCoordinates.length && col >= 0 && col < casesCoordinates[0].length) {
            return casesCoordinates[row][col];
        }
        return null;
    }

    public void gameButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/school/coda/jn_charlie_clemence/bataillejavale/game-view.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, 1080, 720);
        stage.setTitle("Bataille Javal");
        stage.setScene(scene);
        stage.show();
    }
}