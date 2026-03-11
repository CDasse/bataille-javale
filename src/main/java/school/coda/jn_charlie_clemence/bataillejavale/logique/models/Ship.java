package school.coda.jn_charlie_clemence.bataillejavale.logique.models;

public class Ship {
    private final String name;
    private final int size;
    private int hp;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.hp = this.size;
    }

    public int getSize(){
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    public int takeHit(){
        if (this.hp > 0) {
            this.hp -= 1;
        }
        return this.hp;
    }

    public boolean isSunk (){
        return this.hp == 0;
    }

    public boolean isTouched() {
        if (this.isSunk()) return false;
        if (this.hp < this.size) return true;

        return false;
    }

}

