package pt.upskills.projeto.objects.status;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Red implements ImageTile {
    // Attributes
    private Position position;

    // Constructor
    public Red(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Red";
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