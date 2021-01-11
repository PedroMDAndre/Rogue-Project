package pt.upskills.projeto.objects.doors;

import pt.upskills.projeto.objects.room.Room;
import pt.upskills.projeto.rogue.utils.Position;

public class StairsDown extends Door implements Room {
    // Attributes

    // Constructor
    public StairsDown(Position position, int doorNr, String doorType, String destinyRoom, int destinyDoor) {
        super(position, doorNr, doorType, destinyRoom, destinyDoor);
    }

    // Methods
    @Override
    public String getName() {
        return "StairsDown";
    }
}
