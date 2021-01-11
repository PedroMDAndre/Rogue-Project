package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.game.FireBallThread;
import pt.upskills.projeto.game.IntroMenu;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.doors.*;
import pt.upskills.projeto.objects.enemies.Enemy;
import pt.upskills.projeto.objects.items.*;
import pt.upskills.projeto.objects.room.*;
import pt.upskills.projeto.objects.status.*;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.rogue.utils.Vector2D;
import pt.upskills.projeto.savefiles.Save;

import java.awt.event.KeyEvent;
import java.util.*;

import static pt.upskills.projeto.game.Engine.hero;

public class Hero implements ImageTile, Observer {
    private final int MEAT_LIFE_GAIN = 4;       // Life again from GoodMeat
    private final int START_LIFE = 60;          // Starting life
    private final int BASE_DAMAGE = 3;
    private final int SWORD_DAMAGE = 1;
    private final int HAMMER_DAMAGE = 2;
    private final int TRAP_DAMAGE = 1;

    // SCORE BONUS //
    private final int INICIAL_SCORE = 1000;
    private final int MOV_PENALTY = -1;         // Score penalty by move
    private final int PICK_GOOD_MEAT = 20;      // Score from picking GoodMeat
    private final int PICK_FIRE = 20;           // Score from picking Fire
    private final int OPEN_CLOSED_DOOR = 50;    // Score for Opening a ClosedDoor
    private final int NEW_ROOM_SCORE = 100;     // Score for visiting a room for 1st time, except Room0

    // Attributes
    private int score;                          // number of points for life, kills, grabbed objects...
    private Position position;                  // current position
    private boolean hasSword;                   // to know if Hero has Sword
    private boolean hasHammer;                  // to know if Hero has Hammer
    private Stack<ImageTile> fireBalls;         // fireball inventory
    private List<ImageTile> inventory;          // object inventory
    private int life;                           // current life
    private Set<Integer> roomsVisited;
    private Direction lastDirection;
    private int heroRoomNr;                     // the room where the hero is
    private int entryDoorNr;                    // door from here Hero entered the room, for Room0 is -1


    // Constructor
    public Hero(Position position) {
        this.score = INICIAL_SCORE;
        this.position = position;
        this.hasSword = false;
        this.hasHammer = false;
        this.fireBalls = new Stack<>();
        this.life = START_LIFE;
        this.inventory = new ArrayList<>();
        this.roomsVisited = new HashSet<>();
        this.lastDirection = Direction.RIGHT;
        this.heroRoomNr = 0;
        entryDoorNr = -1;

        roomsVisited.add(0);
    }

    // Methods
    //-----//
    // Set // used also to load hero
    //-----//
    public void setScore(int score) {
        this.score = score;
    }

    public void setLife(int life) {
        this.life = life;
        takeDamage(0); // Just Updates life Status Bar
    }

    public void setHeroRoomNr(int heroRoomNr) {
        // Set the Room Number where the Hero currently is
        this.heroRoomNr = heroRoomNr;
    }

    public void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setHasSword(boolean hasSword) {
        this.hasSword = hasSword;
    }

    public void setHasHammer(boolean hasHammer) {
        this.hasHammer = hasHammer;
    }

    public void setEntryDoorNr(int entryDoorNr) {
        this.entryDoorNr = entryDoorNr;
    }


    //-----//
    // Add //
    //-----//
    public void addScore(int value) {
        if (score - value < 0) {
            // Doesn't let score to be negative
            score = 0;
        } else {
            score += value;
        }
    }

    public void addVisitedRoom(int roomNr) {
        roomsVisited.add(roomNr);
    }


    //-----//
    // Get //
    //-----//
    @Override
    public String getName() {
        return "Hero";
    }

    public int getScore() {
        return score;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public int getRank() {
        return 1;
    }

    public boolean isHasSword() {
        // Check if hero has a Sword
        return hasSword;
    }

    public boolean isHasHammer() {
        // Check if Hero has a Hammer
        return hasHammer;
    }

    public int getLife() {
        return life;
    }

    public int getEntryDoorNr() {
        return entryDoorNr;
    }

    public int getHeroRoomNr() {
        return heroRoomNr;
    }


    //-----------//
    // Inventory //
    //-----------//
    public void addInventoryFireBall() {
        // Pick the fireball
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        Fire fire = new Fire(new Position(fireBalls.size(), 0));
        gui.addStatusImage(fire);
        fireBalls.push(fire);
    }

    public void addInventoryFireBall(int fireBallAmount) {
        // Adds several fireBalls to inventory fireBallAmount, up to 3
        int minNrFireBalls = Math.min(3, fireBallAmount);
        for (int i = 0; i < minNrFireBalls; i++) {
            addInventoryFireBall();
        }
    }

    public void addInventorySword(Position position) {
        // Add a Sword to Inventory
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        Sword sword = new Sword(position);
        gui.addStatusImage(sword);
        inventory.add(sword);
        this.hasSword = true;
    }

    public void addInventoryHammer(Position position) {
        // Add a Hammer to Inventory
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        Hammer hammer = new Hammer(position);
        gui.addStatusImage(hammer);
        inventory.add(hammer);
        this.hasHammer = true;
    }

    public void addInventoryKey(Key keyToAdd) {
        addInventoryKey(keyToAdd.getPosition(), keyToAdd);
    }

    public void addInventoryKey(Position position, Key keyToAdd) {
        // Add Key to Inventory
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        Key key = new Key(position, keyToAdd.getDoorNr(), keyToAdd.getRoomNr());
        gui.addStatusImage(key);
        inventory.add(key);
    }

    private int availableInventoryPos() {
        Map<Integer, Boolean> posNotAvailable = new HashMap<>();
        for (ImageTile obj : inventory) {
            posNotAvailable.put(obj.getPosition().getX() - 7, Boolean.FALSE);
        }
        for (int i = 0; i < 3; i++) {
            if (posNotAvailable.get(i) == null) {
                return i;
            }
        }


        return -1;
    }

    //-------------//
    // Clear Lists //
    //-------------//
    public void clearAllHeroLists() {
        // Clear Inventory
        clearInventory();
        // Clear FireBalls
        clearFireBalls();
        // Clear RoomsVisited
        clearRoomsVisited();
    }

    public void clearInventory() {
        inventory.clear();
    }

    public void clearFireBalls() {
        fireBalls.clear();
    }

    public void clearRoomsVisited() {
        roomsVisited.clear();
    }


    //------//
    // Hero //
    //------//
    private void moveHero(Direction direction) {
        if (movPossible(position.plus(direction.asVector()))) {
            position = position.plus(direction.asVector());
            addScore(MOV_PENALTY);  // Penalty for movement
        }
    }

    public void heroEnterRoom(Door door) {
        int roomNr = door.getDestinyRoomNr();

        // Saves the game before hero enters the room
        Save save = new Save();
        setEntryDoorNr(door.getDoorNr());
        save.saveGame();

        // Update hero status with information of the new room
        hero.entryDoorNr = door.getDestinyDoorNr();
        hero.heroRoomNr = roomNr;

        if (roomsVisited.contains(roomNr)) {
            save.loadGameRoom(roomNr);
        } else {
            // room not visited yet
            addScore(NEW_ROOM_SCORE);
            roomsVisited.add(roomNr);
            Engine.enterNewRoom(roomNr);
        }
    }

    private void fireFireBall() {
        // Creates Fire for animation
        FireBall fireBall = new FireBall(position);

        Direction fireDirection = lastDirection;

        FireBallThread fireBallThread = new FireBallThread(fireDirection, fireBall);
        fireBallThread.start();

        // Fire animation
        Engine.tiles.add(fireBall);
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.addImage(fireBall);

        // Removes Fire from status
        ImageTile fireStatus = fireBalls.pop();
        Engine.statusImages.remove(fireStatus);
        gui.removeStatusImage(fireStatus);
    }

    /**
     * This method is called whenever the observed object is changed. This function is called when an
     * interaction with the graphic component occurs {{@link ImageMatrixGUI}}
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        Integer keyCode = (Integer) arg;

        if (keyCode == KeyEvent.VK_P) {
            // saveFile
            Save save = new Save();
            hero.setEntryDoorNr(-1);
            save.saveGame();

        }
        if (keyCode == KeyEvent.VK_O) {
            // loadFile
            Save save = new Save();
            if (save.saveExists()) {
                save.loadGame();
            } else {
                System.out.println("No save game exists.");
            }
        }
        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
            // do something
            lastDirection = Direction.DOWN;
            moveHero(lastDirection);
        }
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
            // do something
            lastDirection = Direction.UP;
            moveHero(lastDirection);
        }
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            // do something
            lastDirection = Direction.LEFT;
            moveHero(lastDirection);
        }
        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
            // do something
            lastDirection = Direction.RIGHT;
            moveHero(lastDirection);
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            // Fire FireBall Event
            // Only fires Fire if it exists in the inventory Stack fireBalls
            if (fireBalls.size() > 0) {
                fireFireBall();
            }
        }
        if (keyCode == KeyEvent.VK_1) {
            // Drop Object 1
            dropObject(0);
        }
        if (keyCode == KeyEvent.VK_2) {
            // Drop Object 2
            dropObject(1);
        }
        if (keyCode == KeyEvent.VK_3) {
            // Drop Object 3
            dropObject(2);
        }
    }

    public int attackDamage() {
        int damage = BASE_DAMAGE;
        if (hasSword) {
            damage += SWORD_DAMAGE;
        }
        if (hasHammer) {
            damage += HAMMER_DAMAGE;
        }

        return damage;
    }

    public void takeDamage(int damage) {
        // Hero takes damage
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

        int lifePerStatusSquare = START_LIFE / 4;

        // Update life //
        if (life - damage < 0) {
            // Don't let life to be negative
            life = 0;

        } else if (life - damage > START_LIFE) {
            // In case of life gain
            life = START_LIFE;
        } else {
            life -= damage;
        }
        // System.out.println("Hero Life: " + life);

        // Update Status Bar //
        List<ImageTile> newStatusImages = new ArrayList<>();
        // Add black
        for (int i = 0; i < 10; i++) {
            newStatusImages.add(new Black(new Position(i, 0)));
        }

        // Update life bar
        for (int i = 1; i < 5; i++) {
            if (life >= lifePerStatusSquare * i) {
                newStatusImages.add(new Green(new Position(i + 2, 0)));
            } else if (life <= lifePerStatusSquare * (i - 1)) {
                newStatusImages.add(new Red(new Position(i + 2, 0)));
            } else{
                newStatusImages.add(new RedGreen(new Position(i + 2, 0)));
            }
        }

        // Add Inventory
        newStatusImages.addAll(inventory);

        // Add fireballs
        newStatusImages.addAll(fireBalls);

        // restart Status
        gui.clearStatus();
        // Add elements to Status
        gui.newStatusImages(newStatusImages);

        if(life == 0){
            // Game Over
            newStatusImages.add(new Red(new Position(3, 0)));
            IntroMenu menu = new IntroMenu(true);
            System.out.println("\n--- GAME OVER ---\n");
            menu.saveScoreToLeaderBoard(score);
            menu.loadMenu();
        }

    }


    public Set<Integer> getRoomsVisited() {
        return roomsVisited;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }

    public List<ImageTile> getInventory() {
        return inventory;
    }


    private boolean movPossible(Position position) {
        for (ImageTile i : Engine.tiles) {
            // Check if there is an "Object" to the position to move and the action to be performed
            if (i.getPosition().equals(position)) {
                if (i instanceof Wall) {            // Check if there is a Wall
                    // Hero cannot move to this position
                    return false;

                } else if (i instanceof Enemy) {   // Check if it has an enemy
                    // Hero cannot move to this position
                    // Damage enemy
                    ((Enemy) i).takeDamage(attackDamage());
                    return false;

                } else if (i instanceof Sword) {  // Check if it is a Sword
                    // If Hero already has a sword it doesn't pick another one
                    // Hero can move to this position
                    if (!hasSword) {
                        // Pick the sword
                        if (inventory.size() < 3) {
                            addInventorySword(new Position(7 + availableInventoryPos(), 0));
                            removeTileFromGuiEngine(i);
                        }
                    }
                    return true;

                } else if (i instanceof Hammer) {  // Check if it is a Hammer
                    // If Hero already has a hammer it doesn't pick another one
                    // Hero can move to this position
                    if (!hasHammer) {
                        // Pick the hammer
                        if (inventory.size() < 3) {
                            addInventoryHammer(new Position(7 + availableInventoryPos(), 0));
                            removeTileFromGuiEngine(i);
                        }
                    }
                    return true;

                } else if (i instanceof Fire) {  // Check if it is a Fire(ball)
                    // Hero can move to this position
                    if (fireBalls.size() < 3) {
                        // Pick the fireball
                        addInventoryFireBall();
                        removeTileFromGuiEngine(i);
                        addScore(PICK_FIRE);    // Add score from picking fire
                    }
                    return true;

                } else if (i instanceof GoodMeat) {  // Catch GoodMeat
                    // Hero can move to this position
                    if (life < START_LIFE) {
                        // Pick the GoodMeat - taking negative damage is the same as gaining life
                        takeDamage(-MEAT_LIFE_GAIN);
                        removeTileFromGuiEngine(i);
                        addScore(PICK_GOOD_MEAT);   // Add score for picking GoodMeat
                    }
                    return true;

                } else if (i instanceof Key) {  // Catch Key
                    // Hero can move to this position
                    // Catch key if space in the inventory available
                    if (inventory.size() < 3) {
                        addInventoryKey(new Position(7 + availableInventoryPos(), 0), (Key) i);
                        removeTileFromGuiEngine(i);
                    }
                    return true;

                } else if (i instanceof Trap) {  // Take damage from trap
                    // Hero can move to this position, but shouldn't...
                    // Take damage
                    takeDamage(TRAP_DAMAGE);
                    return true;
                }
                ///////////
                // Doors //
                ///////////
                else if (i instanceof DoorClosed) {  // Try to move to DoorClosed
                    // Hero cannot move to this position, but can try to open the door...
                    tryOpenDoor((DoorClosed) i);
                    return false;

                } else if (i instanceof DoorOpen || i instanceof DoorWay || i instanceof StairsDown || i instanceof StairsUp) {  // Move to new map
                    heroEnterRoom((Door) i);
                    return false;
                }

                // Não sai fora do mapa se não existir parede.
            } else if (position.getX() < 0 || position.getX() > 9 || position.getY() < 0 || position.getY() > 9) {
                // System.out.println("Hit Map limit! " + position);
                return false;
            }
        }
        // For the remaining cases the hero can move to the position
        return true;
    }

    private boolean tryOpenDoor(DoorClosed doorClosed) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        for (ImageTile obj : inventory) {
            if (obj instanceof Key) {
                if (((Key) obj).getDoorNr() == doorClosed.getDoorNr() && ((Key) obj).getRoomNr() == heroRoomNr) {
                    // Open door
                    // Create new DoorOpen
                    Position position = doorClosed.getPosition();
                    int doorNr = doorClosed.getDoorNr();
                    String doorType = "O";
                    String destinyRoom = doorClosed.getDestinyRoom();
                    int destinyDoor = doorClosed.getDestinyDoor();
                    DoorOpen doorOpen = new DoorOpen(position, doorNr, doorType, destinyRoom, destinyDoor);

                    // Remove Closed door
                    removeTileFromGuiEngine(doorClosed);

                    // New Open door
                    gui.addImage(doorOpen);
                    Engine.tiles.add(doorOpen);

                    // Remove key from Inventory
                    inventory.remove(obj);
                    // Remove key from Status Image
                    gui.removeStatusImage(obj);

                    // Add score for opening the door
                    hero.addScore(OPEN_CLOSED_DOOR);

                    return true;
                }
            }
        }

        return false;
    }

    public void dropObject(int nrObject) {
        // Check what object it was and update hasSword and hasHammer
        // Method to drop object base on number
        // Drops object based in the number in the list
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        if (canDropObject()) {
            for (ImageTile dropObj : inventory) {
                if (dropObj.getPosition().getX() == nrObject + 7) {

                    // Remove from Status Image
                    gui.removeStatusImage(dropObj);


                    // Drop Object to ground
                    ImageTile objGround;    // Object with same type as object to remove from inventory, but with Hero position
                    //System.out.println("Drop Object");
                    if (dropObj instanceof Sword) {
                        objGround = new Sword(this.position);
                        hasSword = false;
                    } else if (dropObj instanceof Hammer) {
                        objGround = new Hammer(this.position);
                        hasHammer = false;
                    } else {
                        objGround = new Key(this.position, ((Key) dropObj).getDoorNr(), ((Key) dropObj).getRoomNr());
                    }
                    Engine.tiles.add(objGround);
                    gui.addImage(objGround);
                    inventory.remove(dropObj);

                    break;
                }
            }
        }
    }

    private boolean canDropObject() {

        for (ImageTile i : Engine.tiles) {
            // Check if there is already an object on the ground.
            if (i.getPosition().equals(position)) {
                if (i instanceof Items || i instanceof Room) {  // Check if it can drop on ground
                    // System.out.println("Cannot drop object " + position);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        //# score; life; heroRoomNr; lastDirection; position.x; position.y; hasSword; SwordPosInventory.x; hasHammer; HammerPositionInventory.x; fireBalls; roomsVisited;
        return "hero;" + score + ";" + life + ";" + heroRoomNr + ";" + lastDirection + ";" +
                position.getX() + ";" + position.getY() + ";" +
                hasSword + ";" + swordStatusPos() + ";" +
                hasHammer + ";" + hammerStatusPos() + ";" +
                fireBalls.size() + ";" + roomsVisited + ";" + heroRoomNr + ";" + entryDoorNr;

    }

    // To String auxiliar functions //
    // Get Sword position on Status bar, if position is 0 the Hero doesn't have a Sword
    private int swordStatusPos() {
        if (hasSword) {
            for (ImageTile obj : inventory) {
                if (obj instanceof Sword) {
                    return obj.getPosition().getX();
                }
            }
            return 0;
        }
        return 0;
    }

    // Get Sword position on Status bar, if position is 0 the Hero doesn't have a Hammer
    private int hammerStatusPos() {
        if (hasHammer) {
            for (ImageTile obj : inventory) {
                if (obj instanceof Hammer) {
                    return obj.getPosition().getX();
                }
            }
            return 0;
        }
        return 0;
    }

    private void removeTileFromGuiEngine(ImageTile tile) {
        // Remove object from GUI and Tiles
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.removeImage(tile);
        Engine.tiles.remove(tile);

    }

}
