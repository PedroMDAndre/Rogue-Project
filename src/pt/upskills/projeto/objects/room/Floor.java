package pt.upskills.projeto.objects.room;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Floor implements ImageTile {
    // Attributes
    private Position position;

    // Constructor
    public Floor(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Floor";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Floor;" + position.getX() + ";" + position.getY();
    }

    @Override
    public int getRank() {
        return 10;
    }
}
