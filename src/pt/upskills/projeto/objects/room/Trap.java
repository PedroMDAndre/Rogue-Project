package pt.upskills.projeto.objects.room;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Trap implements ImageTile, Room {
    // Attributes
    private Position position;

    // Constructor
    public Trap(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Trap";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Trap;" + position.getX() + ";" + position.getY();
    }

    @Override
    public int getRank() {
        return 8;
    }
}
