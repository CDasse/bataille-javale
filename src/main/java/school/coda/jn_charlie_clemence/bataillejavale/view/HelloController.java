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

import java.io.IOException;


public class HelloController {

    @FXML
    private GridPane gridPane
            ;
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

    private EnumShip shipToPlace;

    @FXML
    private void addPorteAvions() {
        shipToPlace = EnumShip.PORTEAVIONS;
    }
    @FXML
    private void addCuirasse() {
        shipToPlace = EnumShip.CUIRASSE;
    }@FXML
    private void addDestroyer() {
        shipToPlace = EnumShip.DESTROYER;
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

                Ship ship = new Ship("porte-avions", 3);

                visualisationOnMouseEnter(grid, cell, ship, row, col);

                hideVisualisationOnMouseExit(cell, row, col);

                gridPane.add(cell, col, row);

                cell.setOnMouseClicked(_ -> {
                    switch (shipToPlace) {
                        case PORTEAVIONS -> {
                            IO.println("PORTEAVIONS");
                            if (currentOrientation == Orientation.HORIZONTAL) {
                                for (int i = 0; i < 5; i++) {
                                    IO.println(r + ":" + (c + i));
                                    Rectangle voisins = getCellFromGrid(r, c + i);
                                    if (voisins != null) voisins.setFill(Color.DARKBLUE);
//                                    IO.println(5);
                                }
                            } else {
                                for (int i = 0; i < 5; i++) {
                                    IO.println((r + i) + ":" + c);
                                    Rectangle voisins = getCellFromGrid(r + i, c);
                                    if (voisins != null) voisins.setFill(Color.DARKBLUE);
                                }
                            }
                        }
                        case CUIRASSE -> IO.println("CUIRASSE");
                        case DESTROYER -> IO.println("DESTROYER");
                        default -> IO.println("Sélectionnez un bateau !");
                    }
                    IO.println(r + ":" + c);
                });
            }
        }
        toggleOrientation();
    }

    private void hideVisualisationOnMouseExit(Rectangle cell, int row, int col) {
        cell.setOnMouseExited(_ -> {
            for (int i = 0; i < 5; i++) {
                if (currentOrientation == Orientation.HORIZONTAL) {
                    Rectangle voisins = getCellFromGrid(row, col + i);
                    if (voisins != null) voisins.setFill(Color.LIGHTBLUE);
                } else {
                    Rectangle voisins = getCellFromGrid(row + i, col);
                    if (voisins != null) voisins.setFill(Color.LIGHTBLUE);
                }
            }
        });
    }

    private void visualisationOnMouseEnter(Grid grid, Rectangle cell, Ship ship, int row, int col) {
        cell.setOnMouseEntered(_ -> {
            for (int i = 0; i < ship.getSize(); i++) {
                if (currentOrientation == Orientation.HORIZONTAL) {
                    Rectangle voisin = getCellFromGrid(row, col + i);
                    setColorGrid(grid, ship, row, col, voisin);
                } else {
                    Rectangle voisin = getCellFromGrid(row + i, col);
                    setColorGrid(grid, ship, row, col, voisin);
                }
            }
        });
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

        Scene scene = new Scene(root, 1080, 720);stage.setTitle("Bataille Javal");
        stage.setScene(scene);
        stage.show();
    }
}