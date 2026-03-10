package school.coda.jn_charlie_clemence.bataillejavale.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameController {
    
    @FXML
    private Label turnLabel;
    
    @FXML
    private GridPane playerGridPane;
    
    @FXML
    private GridPane opponentGridPane;
    
    @FXML
    private Label logLabel;
    
    @FXML
    public void initialize() {
        logLabel.setText("La bataille commence. Préparez vos canons !");
        
        drawGrid(playerGridPane);
        drawGrid(opponentGridPane);
    }

    private void drawGrid(GridPane playerGridPane) {
    }
}