package pt.upskills.projeto.objects.doors;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public abstract class Door implements ImageTile {
    // Attributes
    private Position position;
    private int doorNr;
    private String doorType;
    private String destinyRoom;
    private int destinyDoor;


    // Constructor
    public Door(Position position, int doorNr, String doorType, String destinyRoom, int destinyDoor) {
        this.position = position;
        this.doorNr = doorNr;
        this.doorType = doorType;
        this.destinyRoom = destinyRoom;
        this.destinyDoor = destinyDoor;
    }


    // Methods
    //-----//
    // Set //
    //-----//
    public void setPosition(Position position) {
        this.position = position;
    }

    //-----//
    // Get //
    //-----//
    @Override
    public Position getPosition() {
        return position;
    }

    public int getDoorNr() {
        return doorNr;
    }

    public String getDoorType() {
        return doorType;
    }

    public String getDestinyRoom() {
        return destinyRoom;
    }

    public int getDestinyRoomNr() {
        String strNr = destinyRoom.substring(4, destinyRoom.length() - 4);
        return Integer.parseInt(strNr);
    }

    public int getDestinyDoor() {
        return destinyDoor;
    }

    public int getDestinyDoorNr() {
        return destinyDoor;
    }


    @Override
    public String toString() {
        return getName() + ";" + getPosition().getX() + ";" + getPosition().getY() + ";" +
                getDoorNr() + ";" + getDoorType() + ";" + getDestinyRoom() + ";" + getDestinyDoor();
    }

    @Override
    public int getRank() {
        return 4;
    }
}
