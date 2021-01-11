package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

// This is the object that can be picked
public class Fire extends Items implements ImageTile {
    // Attributes
    private Position position;

    // Constructor
    public Fire(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Fire";
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
        return "Fire;" + position.getX() + ";" + position.getY();
    }

}
