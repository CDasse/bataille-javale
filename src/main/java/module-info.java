module school.coda.jn_charlie_clemence.bataillejavale {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens school.coda.jn_charlie_clemence.bataillejavale to javafx.fxml;
    exports school.coda.jn_charlie_clemence.bataillejavale;
    exports school.coda.jn_charlie_clemence.bataillejavale.view;
    opens school.coda.jn_charlie_clemence.bataillejavale.view to javafx.fxml;
}