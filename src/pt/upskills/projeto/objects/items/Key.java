package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Key extends Items implements ImageTile {
    // Attributes
    private Position position;
    private int doorNr;
    private int roomNr;

    // Constructor
    public Key(Position position, int doorNr, int roomNr) {
        this.position = position;
        this.doorNr = doorNr;
        this.roomNr = roomNr;
    }

    // Methods
    public int getDoorNr() {
        return doorNr;
    }

    public int getRoomNr() {
        return roomNr;
    }

    @Override
    public String getName() {
        return "Key";
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
        return "Key;" + position.getX() + ";" + position.getY() + ";" + doorNr + ";" + roomNr;
    }
}
