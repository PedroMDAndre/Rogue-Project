package pt.upskills.projeto.objects.status;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class RedGreen implements ImageTile {
    // Attributes
    private Position position;

    // Constructor
    public RedGreen(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "RedGreen";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public int getRank() {
        return 11;
    }
}