module school.coda.jn_charlie_clemence.bataillejavale {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires javafx.media;

    exports school.coda.jn_charlie_clemence.bataillejavale;
    opens school.coda.jn_charlie_clemence.bataillejavale.view to javafx.fxml;
    exports school.coda.jn_charlie_clemence.bataillejavale.view;
}