package school.coda.jn_charlie_clemence.bataillejavale.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class BatailleJavaleApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BatailleJavaleApplication.class.getResource("/school/coda/jn_charlie_clemence/bataillejavale/accueil.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
            stage.setTitle("Bataille Javal");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Une erreur est survenue : " + e.getMessage());
            alert.showAndWait();

            System.err.println("Une erreur est survenue : " + e.getMessage());
            e.getStackTrace();
        }
    }
}
