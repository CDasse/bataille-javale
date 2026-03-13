package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

public enum EnumShip {
    PORTEAVIONS("Porte-avions"),
    CUIRASSE("Cuirassé"),
    DESTROYER("Destroyer"),
    SOUSMARIN("Sous-marin"),
    PATROUILLEUR("Patrouilleur");

    public final String name;

    EnumShip(String name) {
        this.name = name;
    }
}
