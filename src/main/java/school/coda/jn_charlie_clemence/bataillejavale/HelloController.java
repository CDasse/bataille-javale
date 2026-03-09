package school.coda.jn_charlie_clemence.bataillejavale;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
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
    protected void initializeGridView() {
        int height = (int) heightSlider.getValue();
        int width = (int) widthSlider.getValue();

        Grid grid = new Grid(height, width);
        initializeGrid(grid, myGridPane);
        welcomeText.setText("New grid generated !");
    }

    @FXML
    private Rectangle[][] casesGraphiques;

    private void initializeGrid(Grid grid, GridPane gridPane) {
        gridPane.getChildren().clear();

        int rows = grid.getWidth();
        int cols = grid.getHeight();

        casesGraphiques = new Rectangle[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Rectangle cell = new Rectangle(20, 20); // Taille de 20px
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.WHITE);

                casesGraphiques[row][col] = cell;

                final int r = row;
                final int c = col;


                Ship ship = new Ship("porte-avions", 3);

                cell.setOnMouseEntered(_ -> {
                    for (int i = 0; i < 3; i++) {
                        Rectangle voisin = getCellFromGrid(r, c + i);
                        if (grid.canPLaceShip(ship, r, c, Orientation.HORIZONTAL)) {
                            if (voisin != null) voisin.setFill(Color.LIGHTGREEN);
                        } else {
                            if (voisin != null) voisin.setFill(Color.LIGHTCORAL);
                        }
                    }
                });

                cell.setOnMouseExited(_ -> {
                    for (int i = 0; i < 3; i++) {
                        Rectangle voisin = getCellFromGrid(r, c + i);
                        if (voisin != null) voisin.setFill(Color.LIGHTBLUE);
                    }
                });

                cell.setOnMouseClicked(_ -> {
//                    char lettre = (char) ('A' + c);
                    System.out.println(r + ":" + c);

                    cell.setFill(Color.DARKBLUE);
                });

                gridPane.add(cell, col, row);
            }
        }
    }

    private Rectangle getCellFromGrid(int r, int c) {
        // On vérifie si les coordonnées r et c sont bien dans les limites du tableau
        if (casesGraphiques != null && r >= 0 && r < casesGraphiques.length && c >= 0 && c < casesGraphiques[0].length) {
            return casesGraphiques[r][c];
        }
        return null;
    }

}