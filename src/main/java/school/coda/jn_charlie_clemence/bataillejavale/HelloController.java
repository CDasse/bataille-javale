package school.coda.jn_charlie_clemence.bataillejavale;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private GridPane monGridPane;

    @FXML
    public void initialize() {
        genererGrille();
    }

    private void genererGrille() {
        int taille = 20;

        for (int row = 0; row < taille; row++) {
            for (int col = 0; col < taille; col++) {

                Rectangle cell = new Rectangle(20, 20); // Taille de 20px
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.WHITE);

                // Variables pour la lambda
                final int r = row;
                final int c = col;

                // Action au clic
                cell.setOnMouseClicked(_ -> {

                    char lettre = (char) ('A' + c);
                    System.out.println(r + ":" + lettre);

                    // Change la couleur pour marquer le clic
                    cell.setFill(Color.DARKBLUE);
                });

                // Placement dans le GridPane (colonne, ligne)
                monGridPane.add(cell, col, row);
            }
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Grille générée !");
        genererGrille();
    }
}