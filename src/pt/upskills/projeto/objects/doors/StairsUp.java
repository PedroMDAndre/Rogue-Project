package pt.upskills.projeto.objects.doors;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.room.Room;
import pt.upskills.projeto.rogue.utils.Position;

public class StairsUp extends Door implements ImageTile, Room {
    // Attributes


    // Constructor
    public StairsUp(Position position, int doorNr, String doorType, String destinyRoom, int destinyDoor) {
        super(position, doorNr, doorType, destinyRoom, destinyDoor);
    }

    // Methods
    @Override
    public String getName() {
        return "StairsUp";
    }

    @Override
    public String toString() {
        return "StairsUp;" + getPosition().getX() + ";" + getPosition().getY() + ";" +
                getDoorNr() + ";" + getDoorType() + ";" + getDestinyRoom() + ";" + getDestinyDoor();
    }
}
