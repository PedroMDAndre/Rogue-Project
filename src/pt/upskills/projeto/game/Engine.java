package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.*;
import pt.upskills.projeto.objects.doors.*;
import pt.upskills.projeto.objects.enemies.*;
import pt.upskills.projeto.objects.items.*;
import pt.upskills.projeto.objects.room.*;
import pt.upskills.projeto.objects.status.*;
import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.savefiles.Save;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Engine {

    public static List<ImageTile> tiles = new ArrayList<>();
    public static List<ImageTile> statusImages = new ArrayList<>();
    public static Hero hero = new Hero(new Position(0,0));

    public void init(boolean loadLastGame) {
        // Several instances of gui can be initialized
        // Used to add and remove ImageTile from screen
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.setName("Rogue Game");

        Save save = new Save();

        if(loadLastGame){

            save.loadGame();
        }else {

            // Adds Floor
            save.addFloor();

            // Reads the map 'tiles' and adds them to List<ImageTile> tiles
            readMapFile( 0, gui);

            // Start status bar
            startStatus();

            // Sort tiles according to rank, to avoid unwanted overlapping, some objects hiding others...
            CompareImageTile compare = new CompareImageTile();
            tiles.sort(compare);

            gui.newImages(tiles);
        }
        gui.go();

        while (true) {
            gui.update();
        }
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.init(false);
    }

    public static void readMapFile(int level, ImageMatrixGUI gui) {
        // Read Room map
        // reads file "rooms/room + level + .txt"

        // Room file to read
        File fileRoom = new File("rooms/room" + level + ".txt");

        // Doors and Keys Lists
        ArrayList<Key> keyList = createKeysList(fileRoom);
        Map<Integer, Door> doorList = createDoorsList(fileRoom);

        // Create Room and other objects
        try {

            Scanner fileScanner = new Scanner(fileRoom);


            int i = 0;
            while (fileScanner.hasNextLine()) {
                String nextLine = fileScanner.nextLine();
                String[] separar = nextLine.split("");
                // ect...
                if (separar[0].equals("*") || separar[0].equals("#")) {
                    continue;

                } else {
                    for (int j = 0; j < separar.length; j++) {

                        switch (separar[j]) {
                            case "W":
                                // Add Wall
                                tiles.add(new Wall(new Position(j, i)));
                                break;
                            //-------------//
                            // Add Enemies //
                            //-------------//
                            case "B":
                                // Add Bat
                                Bat bat = new Bat(new Position(j, i));
                                gui.addObserver(bat);
                                tiles.add(bat);

                                break;
                            case "V":
                                // Add FinalBoss
                                FinalBoss finalBoss = new FinalBoss(new Position(j, i));
                                gui.addObserver(finalBoss);
                                tiles.add(finalBoss);

                                break;
                            case "G":
                                // Add BadGuy
                                BadGuy badGuy = new BadGuy(new Position(j, i));
                                gui.addObserver(badGuy);
                                tiles.add(badGuy);
                                break;
                            case "S":
                                // Add Skeleton
                                Skeleton skeleton = new Skeleton(new Position(j, i));
                                gui.addObserver(skeleton);
                                tiles.add(skeleton);

                                break;
                            case "T":
                                // Add Thief
                                Thief thief = new Thief(new Position(j, i));
                                gui.addObserver(thief);
                                tiles.add(thief);
                                break;

                            //-------------//
                            //  Add Items  //
                            //-------------//
                            case "F":
                                // Add Fire
                                Fire fire = new Fire(new Position(j, i));
                                tiles.add(fire);
                                break;
                            case "m":
                                // Add GoodMeat
                                GoodMeat goodMeat = new GoodMeat(new Position(j, i));
                                tiles.add(goodMeat);
                                break;
                            case "h":
                                // Add Hammer
                                Hammer hammer = new Hammer(new Position(j, i));
                                tiles.add(hammer);
                                break;

                            case "s":
                                // Add Sword
                                Sword sword = new Sword(new Position(j, i));
                                tiles.add(sword);
                                break;

                            //--------------//
                            //Add Room Items//
                            //--------------//
                            case "0":
                            case "1":
                            case "2":
                            case "3":
                            case "4":
                            case "5":
                            case "6":
                            case "7":
                            case "8":
                            case "9":
                                // Add Doors
                                try {
                                    Door door = doorList.get(Integer.parseInt(separar[j]));
                                    door.setPosition(new Position(j, i));
                                    tiles.add(door);

                                    if(hero.getEntryDoorNr() == Integer.parseInt(separar[j])){
                                        hero.setPosition(((ImageTile) door).getPosition());
                                    }

                                } catch (NullPointerException e) {
                                    System.out.println("ERROR: Room file might not have information on the doors. File: " + fileRoom);
                                }
                                break;
                            case "g":
                                // Add Grass
                                tiles.add(new Grass(new Position(j, i)));
                                break;

                            case "x":
                                // Add Trap
                                tiles.add(new Trap(new Position(j, i)));
                                break;
                            case "H":
                                // Add Hero
                                hero.setPosition(new Position(j, i));
                                break;

                        }

                    }

                    i++;
                }

            }
            // Create hero above all tiles, that's why is last to enter the tiles
            gui.addObserver(hero);
            //tiles.sort();
            tiles.add(hero);
            fileScanner.close();

        } catch (
                FileNotFoundException e) {
            System.out.println("Não foi possível ler o ficheiro do mapa.");
        }

        // Create keys
        tiles.addAll(keyList);

    }


    public void startStatus() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        for (int i = 0; i < 10; i++) {
            statusImages.add(new Black(new Position(i, 0)));
            if (i > 2 && i < 7) {
                statusImages.add(new Green(new Position(i, 0)));
            }
        }
        gui.newStatusImages(statusImages);
    }

    public static void enterNewRoom(int roomNr) {
        Save load = new Save();
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // Empty Start
        load.clearTilesObserversGUI();

        // Add tiles to Engine.tiles
        load.addFloor();
        readMapFile(roomNr, gui);

        // Hero loads last, so he isn't overlapped by other tiles
        Engine.tiles.add(Engine.hero);

        // Load all tiles into the GUI
        CompareImageTile compare = new CompareImageTile();
        Engine.tiles.sort(compare);
        gui.newImages(Engine.tiles);

        gui.addObserver(hero);
    }

    private static ArrayList<Key> createKeysList(File fileRoom) {
        // 0;1  ;2    ;3    ;4     ;5
        // *;key;pos_x;pos_y;doorNr;roomNr
        ArrayList<Key> keyList = new ArrayList<>();
        try {
            Scanner fileScanner = new Scanner(fileRoom);

            while (fileScanner.hasNextLine()) {
                String nextLine = fileScanner.nextLine();
                String[] separar = nextLine.split(";");

                if (separar[0].equals("*")) {
                    try {

                        if (separar.length > 1) {
                            int x = Integer.parseInt(separar[2]);
                            int y = Integer.parseInt(separar[3]);
                            int doorNr = Integer.parseInt(separar[4]);
                            int roomNr = Integer.parseInt(separar[5]);
                            keyList.add(new Key(new Position(x, y), doorNr, roomNr));
                        }

                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                        System.out.println("Check if all keys are well defined!");
                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return keyList;

    }

    private static Map<Integer, Door> createDoorsList(File fileRoom) {
        //0;1     ;2       ;3          ;4
        //#;doorNr;doorType;destinyRoom;destinyDoor
        Map<Integer, Door> doorList = new HashMap<>();
        try {
            Scanner fileScanner = new Scanner(fileRoom);

            while (fileScanner.hasNextLine()) {
                String nextLine = fileScanner.nextLine();
                String[] separar = nextLine.split(";");

                if (separar[0].equals("#")) {
                    try {

                        if (separar.length > 1) {
                            int doorNr = Integer.parseInt(separar[1]);
                            String doorType = separar[2];
                            String destinyRoom = separar[3];
                            int destinyDoor = Integer.parseInt(separar[4]);

                            switch (doorType) {
                                case "C":
                                    doorList.put(doorNr, new DoorClosed(new Position(0, 0), doorNr, doorType, destinyRoom, destinyDoor)); // Position needs to be updated later
                                    break;
                                case "O":
                                    doorList.put(doorNr, new DoorOpen(new Position(0, 0), doorNr, doorType, destinyRoom, destinyDoor)); // Position needs to be updated later
                                    break;
                                case "w":
                                    doorList.put(doorNr, new DoorWay(new Position(0, 0), doorNr, doorType, destinyRoom, destinyDoor)); // Position needs to be updated later
                                    break;
                                case "D":
                                    doorList.put(doorNr, new StairsDown(new Position(0, 0), doorNr, doorType, destinyRoom, destinyDoor)); // Position needs to be updated later
                                    break;
                                case "U":
                                    doorList.put(doorNr, new StairsUp(new Position(0, 0), doorNr, doorType, destinyRoom, destinyDoor)); // Position needs to be updated later
                                    break;
                            }


                        }

                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                        System.out.println("Check if all keys are well defined!");
                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return doorList;
    }


}

