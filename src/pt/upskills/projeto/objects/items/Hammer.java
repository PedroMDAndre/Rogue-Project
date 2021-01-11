package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Hammer extends Items implements ImageTile {
    // Attributes
    private Position position;

    // Constructor
    public Hammer(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Hammer";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Hammer;" + position.getX() + ";" + position.getY();
    }
}
