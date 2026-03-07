package school.coda.jn_charlie_clemence.bataillejavale.logique;

public class Ship {
    private final String name;
    private final int size;
    private int hp;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.hp = this.size;
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


}
