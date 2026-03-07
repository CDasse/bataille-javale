package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

public class Cell {
    private Ship ship;
    private boolean isTargeted;

    public Cell() {
        this.isTargeted = false;
        this.ship = null;
    }

    public void setShip(Ship ship){
        this.ship =ship;
    }

    public boolean receiveShot () {
        if (this.isTargeted){
            return false;
        }

        this.isTargeted = true;

        if (this.ship != null) {
            ship.takeHit();
            return true;
        }
        return false;
    }

    public boolean isEmpty(){
        return this.ship == null;
    }
}
