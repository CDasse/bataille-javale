package school.coda.jn_charlie_clemence.bataillejavale.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import school.coda.jn_charlie_clemence.bataillejavale.logique.models.Ship;
import school.coda.jn_charlie_clemence.bataillejavale.view.utils.Winner;

import java.io.IOException;
import java.util.List;

public class EndGameController {

    @FXML
    Label gameResult;

    @FXML
    Label nbOfTurn;

    @FXML
    VBox botFleetStatusBox;

    @FXML
    VBox humanFleetStatusBox;

    public void endGameView(int currentTurn, Winner winner, List<Ship> humanFleet, List<Ship> botFleet) {
        if (winner == Winner.HUMAN) {
            gameResult.setText("VICTOIRE ! Vous avez écrasé votre adversaire !");
            gameResult.setTextFill(Color.GREEN);
        } else {
            gameResult.setText("DEFAITE ... Le bot vous a écrasé !");
            gameResult.setTextFill(Color.RED);
        }

        updateFleetStatus(humanFleetStatusBox, humanFleet);
        updateFleetStatus(botFleetStatusBox, botFleet);

        nbOfTurn.setText("Nombre de tours joués : " + currentTurn);

    }

    private void updateFleetStatus (VBox statusBox,  List<Ship> fleet) {
        statusBox.getChildren().clear();

        for (Ship ship : fleet) {
            Label label;

            if (ship.isSunk()) {
                label = new Label(ship.getName() + " - COULÉ ☠️");
                label.setTextFill(Color.DARKRED);
            } else {
                label = new Label(ship.getName() + " - EN VIE");
                label.setTextFill(Color.DARKGREEN);
            }

            statusBox.getChildren().add(label);
        }
    }

    public void goBackToHomePage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/school/coda/jn_charlie_clemence/bataillejavale/accueil.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 1080, 720);

            stage.setTitle("Bataille Javal - Accueil");
            stage.setScene(scene);
            stage.show();

        } catch (IOException | RuntimeException e){
            System.err.println("Erreur lors du retour à l'accueil : " + e.getMessage());
        }
    }
}
