package school.coda.jn_charlie_clemence.bataillejavale;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Grid;


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


    private void initializeGrid(Grid grid, GridPane gridPane) {
        gridPane.getChildren().clear();

        int rows = grid.getWidth();
        int cols = grid.getHeight();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Rectangle cell = new Rectangle(20, 20); // Taille de 20px
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.WHITE);

                final int r = row;
                final int c = col;

                cell.setOnMouseClicked(_ -> {
                    char lettre = (char) ('A' + c);
                    System.out.println(r + ":" + lettre);

                    cell.setFill(Color.DARKBLUE);
                });

                gridPane.add(cell, col, row);
            }
        }
    }

}