package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public abstract class  Items implements ImageTile {
    public abstract void setPosition(Position position);

    public abstract String toString();

    @Override
    public int getRank() {
        return 4;
    }
}
