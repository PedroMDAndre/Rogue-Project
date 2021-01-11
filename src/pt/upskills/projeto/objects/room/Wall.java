package pt.upskills.projeto.objects.room;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Wall implements ImageTile, Room {
    // Attributes
    private Position position;

    // Constructor
    public Wall(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Wall";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Wall;" + position.getX() + ";" + position.getY();
    }

    @Override
    public int getRank() {
        return 9;
    }
}
