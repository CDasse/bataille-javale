package school.coda.jn_charlie_clemence.bataillejavale.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Grid;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Orientation;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Ship;



public class HelloController {

    @FXML
    private Slider widthSlider;

    @FXML
    private Slider heightSlider;

    @FXML
    private Label welcomeText;

    @FXML
    private GridPane myGridPane;

    @FXML
    private VBox scene;

    private Orientation currentOrientation = Orientation.HORIZONTAL;

    @FXML
    protected void initializeGridView() {
        int height = (int) heightSlider.getValue();
        int width = (int) widthSlider.getValue();

        Grid grid = new Grid(height, width);
        initializeGrid(grid, myGridPane);
        welcomeText.setText("New grid generated !");
    }

    @FXML
    private Rectangle[][] casesCoordinates;

    private void initializeGrid(Grid grid, GridPane gridPane) {
        gridPane.getChildren().clear();

        int rows = grid.getWidth();
        int cols = grid.getHeight();

        casesCoordinates = new Rectangle[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Rectangle cell = new Rectangle(20, 20); // Taille de 20px
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.WHITE);

                casesCoordinates[row][col] = cell;

                final int r = row;
                final int c = col;

                Ship ship = new Ship("porte-avions", 3);

                cell.setOnMouseEntered(_ -> {
                    for (int i = 0; i < ship.getSize(); i++) {
                        if (currentOrientation == Orientation.HORIZONTAL) {
                            Rectangle voisin = getCellFromGrid(r, c + i);
                            if (grid.canPLaceShip(ship, r, c, currentOrientation)) {
                                if (voisin != null) voisin.setFill(Color.LIGHTGREEN);
                            } else {
                                if (voisin != null) voisin.setFill(Color.LIGHTCORAL);
                            }
                        } else {
                            Rectangle voisin = getCellFromGrid(r + i, c);
                            if (grid.canPLaceShip(ship, r, c, currentOrientation)) {
                                if (voisin != null) voisin.setFill(Color.LIGHTGREEN);
                            } else {
                                if (voisin != null) voisin.setFill(Color.LIGHTCORAL);
                            }
                        }
                    }
                });

                cell.setOnMouseExited(_ -> {
                    for (int i = 0; i < 3; i++) {
                        if (currentOrientation == Orientation.HORIZONTAL) {
                            Rectangle voisins = getCellFromGrid(r, c + i);
                            if (voisins != null) voisins.setFill(Color.LIGHTBLUE);
                        } else {
                            Rectangle voisins = getCellFromGrid(r + i, c);
                            if (voisins != null) voisins.setFill(Color.LIGHTBLUE);
                        }
                    }
                });



//                cell.setOnMouseClicked(_ -> {
//                    char lettre = (char) ('A' + c);
//                    System.out.println(r + ":" + c);
//
//                    cell.setFill(Color.DARKBLUE);
//                });

                gridPane.add(cell, col, row);
            }
        }
        setupKeyboardControls();
    }

    private void setupKeyboardControls() {
        myGridPane.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.R) {
                // On bascule l'orientation
                currentOrientation = (currentOrientation == Orientation.HORIZONTAL)
                        ? Orientation.VERTICAL : Orientation.HORIZONTAL;
                System.out.println("Orientation : " + currentOrientation);
            }
        });
    }

    private Rectangle getCellFromGrid(int r, int c) {
        // On vérifie si les coordonnées r et c sont bien dans les limites du tableau
        if (casesCoordinates != null && r >= 0 && r < casesCoordinates.length && c >= 0 && c < casesCoordinates[0].length) {
            return casesCoordinates[r][c];
        }
        return null;
    }

}