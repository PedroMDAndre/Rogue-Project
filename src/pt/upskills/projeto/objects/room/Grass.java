package pt.upskills.projeto.objects.room;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Grass implements ImageTile {
    // Attributes
    private Position position;

    // Constructor
    public Grass(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Grass";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Grass;" + position.getX() + ";" + position.getY();
    }

    @Override
    public int getRank() {
        return 8;
    }
}
