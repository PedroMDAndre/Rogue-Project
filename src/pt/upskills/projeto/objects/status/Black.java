package pt.upskills.projeto.objects.status;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Black implements ImageTile {
    // Attributes
    private Position position;

    // Constructor
    public Black(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Black";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public int getRank() {
        return 12;
    }
}
