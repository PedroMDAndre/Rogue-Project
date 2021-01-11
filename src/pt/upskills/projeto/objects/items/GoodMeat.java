package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class GoodMeat extends Items implements ImageTile {
    // Attributes
    private Position position;

    // Constructor
    public GoodMeat(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "GoodMeat";
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
        return "GoodMeat;" + position.getX() + ";" + position.getY();
    }
}
