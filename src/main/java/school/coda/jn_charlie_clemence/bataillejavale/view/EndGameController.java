package school.coda.jn_charlie_clemence.bataillejavale.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import school.coda.jn_charlie_clemence.bataillejavale.view.utils.Winner;
import java.io.IOException;

public class EndGameController {

    @FXML
    Label gameResult;

    @FXML
    Label nbOfTurn;

    public void endGameView(int currentTurn, Winner winner) {
        if (winner == Winner.HUMAN) {
            gameResult.setText("VICTOIRE ! Vous avez écrasé votre adversaire !");
            gameResult.setTextFill(Color.GREEN);
        } else {
            gameResult.setText("DEFAITE ... Le bot vous a écrasé !");
            gameResult.setTextFill(Color.RED);
        }

        nbOfTurn.setText("Nombre de tours joués : " + currentTurn);

    }

    public void goBackToHomePage(ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(BatailleJavaleApplication.class.getResource("/school/coda/jn_charlie_clemence/bataillejavale/accueil.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 1080, 720);

            stage.setTitle("Bataille Javal - Accueil");
            stage.setScene(scene);
            stage.show();
    }
}
