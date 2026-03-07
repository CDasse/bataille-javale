package school.coda.jn_charlie_clemence.bataillejavale.logique.utils;

import school.coda.jn_charlie_clemence.bataillejavale.logique.Ship;

import java.util.ArrayList;
import java.util.List;

public class ShipFactory {

    public static List<Ship> createFleet() {
        List<Ship> fleet = new ArrayList<>();

        fleet.add(new Ship("Porte-avions", 5));
        fleet.add(new Ship("Cuirassé", 4));
        fleet.add(new Ship("Destroyer", 3));
        fleet.add(new Ship("Sous-marin", 3));
        fleet.add(new Ship("Patrouilleur", 2));

        return fleet;
    }
}
