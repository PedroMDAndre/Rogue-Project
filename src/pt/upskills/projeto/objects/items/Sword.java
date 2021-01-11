package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Sword extends Items implements ImageTile {
    // Attributes
    private Position position;

    // Constructor
    public Sword(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Sword";
    }

    @Override
    public Position getPosition() {
        return position;
    }


    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Sword;" + position.getX() + ";" + position.getY();
    }
}