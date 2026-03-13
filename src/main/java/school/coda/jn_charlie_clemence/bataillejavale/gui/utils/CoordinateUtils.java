package school.coda.jn_charlie_clemence.bataillejavale.gui.utils;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CoordinateUtils {

    private CoordinateUtils() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne doit pas être instanciée.");
    }

    public static void showNameOfGridCols(int cols, GridPane playerGridPane) {
        for (int col = 0; col < cols; col++) {
            Label colLabel = new Label(String.valueOf(col + 1));

            colLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
            colLabel.setPrefSize(30, 30);
            colLabel.setAlignment(javafx.geometry.Pos.CENTER);

            playerGridPane.add(colLabel, col + 1, 0);
        }
    }

    public static void showNameOfGridRows(int rows, GridPane playerGridPane) {
        for (int row = 0; row < rows; row++) {
            char letter = (char) ('A' + row);
            Label rowLabel = new Label(String.valueOf(letter));

            rowLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
            rowLabel.setPrefSize(30, 30);
            rowLabel.setAlignment(javafx.geometry.Pos.CENTER);

            playerGridPane.add(rowLabel, 0, row + 1);
        }
    }
}
