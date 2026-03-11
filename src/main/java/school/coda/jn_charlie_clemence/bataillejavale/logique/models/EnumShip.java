package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

public enum EnumShip {
    PORTEAVIONS("Porte-avions", 5),
    CUIRASSE("Cuirassé", 4),
    DESTROYER("Destroyer", 3),
    SOUSMARIN("Sous-marin", 3),
    PATROUILLEUR("Patrouilleur", 2);

    public final String name;
    public final int size;

    EnumShip(String name, int size) {
        this.name = name;
        this.size = size;
    }
}
