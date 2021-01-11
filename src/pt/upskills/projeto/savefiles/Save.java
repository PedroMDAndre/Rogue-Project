package pt.upskills.projeto.savefiles;

import pt.upskills.projeto.game.CompareImageTile;
import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.objects.doors.*;
import pt.upskills.projeto.objects.enemies.*;
import pt.upskills.projeto.objects.items.*;
import pt.upskills.projeto.objects.room.*;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Save {
    // Attributes
    private File saveFile; //"saves/saveGameRooms.txt"
    private File saveHero; //"saves/saveHero.txt"
    private File fileTemp;
    private File currentRoomFile;

    // Constructor
    public Save() {
        this.saveFile = new File("saves/saveGameRooms.txt");
        this.saveHero = new File("saves/saveHero.txt");
        this.fileTemp = new File("saves/temp.txt");
        this.currentRoomFile = new File("saves/saveCurrRoom.txt");
    }

    public Save(File saveFile, File saveHero, File fileTemp, File currentRoomFile) {
        this.saveFile = saveFile;
        this.saveHero = saveHero;
        this.fileTemp = fileTemp;
        this.currentRoomFile = currentRoomFile;
    }

    // Methods
    public boolean saveExists() {
        return saveFile.exists() && saveHero.exists();
    }

    //------//
    // Save //
    //------//
    private void saveGameHero() {
        Hero saveHero = Engine.hero;

        String stringHero = saveHero.toString();

        try {
            PrintWriter printWriter = new PrintWriter(this.saveHero);
            // Save Hero info
            printWriter.println("# Hero");
            printWriter.println("# hero; score; life; heroRoomNr; lastDirection; position.x; position.y; hasSword; SwordPosInventory.x; hasHammer; HammerPositionInventory.x; fireBalls; roomsVisited; heroRoomNr; entryDoorNr;");
            printWriter.println("# default position for Hammer and Sword is 0 => Don't exist");
            printWriter.println(stringHero);

            // Save inventory keys
            printWriter.println("# Keys in inventory");
            printWriter.println("# Key; KeyPositionInventory.x; KeyPositionInventory.y; doorNr; roomNr");
            for (ImageTile obj : saveHero.getInventory()) {
                if (obj instanceof Key) {
                    printWriter.println(obj);
                }
            }

            printWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println("Save file not found! File: " + saveFile);
        }
    }

    private void saveCurrentRoom() {
        int roomNr = Engine.hero.getHeroRoomNr();

        try {
            PrintWriter printWriter = new PrintWriter(currentRoomFile);
            printWriter.println("# Room" + roomNr);

            for (ImageTile imageTile : Engine.tiles) {
                String objString = imageTile.toString();
                String[] objStringTokens = objString.split(";");
                switch (objStringTokens[0]) {
                    case "BadGuy":
                    case "FinalBoss":
                    case "Bat":
                    case "Skeleton":
                    case "Thief":
                    case "Fire":
                    case "GoodMeat":
                    case "Hammer":
                    case "Key":
                    case "Sword":
                    case "DoorClosed":
                    case "DoorOpen":
                    case "DoorWay":
                    case "StairsDown":
                    case "StairsUp":
                        printWriter.println("Room" + roomNr + ";" + imageTile);
                    default:
                        // Objects that cannot change will not be saved to a file and will be read later from a room file
                        // Objects related to the Hero and the inventory will not be read here
                        break;
                }
            }

            printWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Save file not found! File: " + saveFile);
        }

    }

    private void saveGameRooms() {
        // Updates the current room in the save file
        int currentRoomNr = Engine.hero.getHeroRoomNr();

        if (!fileTemp.exists()) {
            try {
                fileTemp.createNewFile();
            } catch (IOException e) {
                System.out.println("ERROR: It wasn't possible to create a Save file.");
            }
        }

        copyTxtToTemp();

        File saveGame = new File("saves/saveGameRooms.txt");
        if (!saveGame.exists()) {
            try {
                saveGame.createNewFile();
            } catch (IOException e) {
                System.out.println("ERROR: It wasn't possible to create a Save file.");
            }
        }

        try {
            Scanner loadScanner = new Scanner(fileTemp);
            PrintWriter printWriter = new PrintWriter(saveGame);

            while (loadScanner.hasNextLine()) {
                String objString = loadScanner.nextLine();
                String[] objStringTokens = objString.split(";");
                if (objStringTokens[0].contains("Room") && !objStringTokens[0].contains("Room" + currentRoomNr)) {
                    printWriter.println(objString);
                }
            }

            saveCurrentRoom();


            loadScanner = new Scanner(currentRoomFile);
            while (loadScanner.hasNextLine()) {

                printWriter.println(loadScanner.nextLine());

            }


            loadScanner.close();
            printWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Save file not found! File: " + saveGame);
        }

    }

    private void copyTxtToTemp() {
        try {
            Scanner loadScanner = new Scanner(saveFile);
            PrintWriter printWriter = new PrintWriter(fileTemp);
            while (loadScanner.hasNextLine()) {
                printWriter.println(loadScanner.nextLine());
            }
            loadScanner.close();
            printWriter.close();
        } catch (FileNotFoundException e) {
            // System.out.println("ERROR:(copyTxtToTemp) Save file not found! File: " + saveFile);
            System.out.println("--");
        }
    }

    public void saveGame() {
        saveGameHero();
        saveGameRooms();
    }

    //------//
    // Load //
    //------//
    private void loadGameHero() {
        File loadFile = saveHero;
        try {
            if (!loadFile.exists()) {

                throw new FileNotFoundException("ERROR Load file not found! File: " + loadFile);

            }

            ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
            Hero loadHero = Engine.hero;

            // Status Bar (only): Reset FireBall, Life Bar and Inventory
            gui.clearStatus();

            // Clear inventory, fireBalls and visitedRooms
            loadHero.clearAllHeroLists();


            try {
                Scanner fileScanner = new Scanner(loadFile);
                while (fileScanner.hasNextLine()) {
                    String lineString = fileScanner.nextLine();
                    String[] lineTokens = lineString.split(";");
                    String objType = lineTokens[0];

                    switch (objType) {
                        case "hero":
                            loadHero.setScore(Integer.parseInt(lineTokens[1]));
                            loadHero.setLife(Integer.parseInt(lineTokens[2]));
                            loadHero.setHeroRoomNr(Integer.parseInt(lineTokens[3]));
                            loadHero.setLastDirection(Direction.valueOf(lineTokens[4]));
                            loadHero.setPosition(new Position(Integer.parseInt(lineTokens[5]), Integer.parseInt(lineTokens[6])));

                            loadHero.setHasSword(Boolean.parseBoolean(lineTokens[7]));
                            if (loadHero.isHasSword()) {
                                // add Sword to Inventory
                                loadHero.addInventorySword(new Position(Integer.parseInt(lineTokens[8]), 0));
                            }

                            loadHero.setHasHammer(Boolean.parseBoolean(lineTokens[9]));
                            if (loadHero.isHasHammer()) {
                                // add Hammer to Inventory
                                loadHero.addInventoryHammer(new Position(Integer.parseInt(lineTokens[10]), 0));
                            }

                            loadHero.addInventoryFireBall(Integer.parseInt(lineTokens[11]));

                            // Rooms visited
                            String roomsVisited = lineTokens[12];
                            String[] roomsVisitedTokens = roomsVisited.substring(1, roomsVisited.length() - 1).split(",");
                            try {
                                for (String roomNr : roomsVisitedTokens) {
                                    loadHero.addVisitedRoom(Integer.parseInt(roomNr.trim()));
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("ERROR: It was not possible to get the Rooms already visited.");
                                // if no rooms where found to be visited, is assumed that at least room 0 was visited
                                loadHero.addVisitedRoom(0);
                            }

                            loadHero.setHeroRoomNr(Integer.parseInt(lineTokens[13]));

                            loadHero.setEntryDoorNr(Integer.parseInt(lineTokens[14]));

                            break;

                        case "Key":
                            Position keyPosition = new Position(Integer.parseInt(lineTokens[1]), 0);
                            int doorNr = Integer.parseInt(lineTokens[3]);
                            int roomNr = Integer.parseInt(lineTokens[4]);
                            Key loadKey = new Key(keyPosition, doorNr, roomNr);
                            loadHero.addInventoryKey(loadKey);
                            break;
                    }

                }

                fileScanner.close();

            } catch (FileNotFoundException e) {
                System.out.println("ERROR: Load file not found! File: " + loadFile);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Load file not found! File: " + loadFile);
        }
    }

    private void loadGameRooms(int roomNr) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

        File roomFile = saveFile;

        try {
            Scanner fileScanner = new Scanner(roomFile);
            while (fileScanner.hasNextLine()) {
                String objString = fileScanner.nextLine();
                String[] objStringTokens = objString.split(";");

                // Loads from the save file only the room of interest
                if (objStringTokens[0].contains("Room" + roomNr) && objStringTokens.length > 1) {
                    switch (objStringTokens[1]) {
                        case "BadGuy":
                            // Create enemy type
                            BadGuy badGuy = new BadGuy(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])));
                            badGuy.setEnemyLife(Integer.parseInt(objStringTokens[4]));
                            gui.addObserver(badGuy);

                            // Load enemy
                            Engine.tiles.add(badGuy);
                            break;

                        case "FinalBoss":
                            // Create enemy type
                            FinalBoss finalBoss = new FinalBoss(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])));
                            finalBoss.setEnemyLife(Integer.parseInt(objStringTokens[4]));
                            gui.addObserver(finalBoss);

                            // Load enemy
                            Engine.tiles.add(finalBoss);
                            break;

                        case "Bat":
                            // Create enemy type
                            Bat bat = new Bat(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])));
                            bat.setEnemyLife(Integer.parseInt(objStringTokens[4]));
                            gui.addObserver(bat);


                            // Load enemy
                            Engine.tiles.add(bat);
                            break;

                        case "Skeleton":
                            // Create enemy type
                            Skeleton skeleton = new Skeleton(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])));
                            skeleton.setEnemyLife(Integer.parseInt(objStringTokens[4]));
                            gui.addObserver(skeleton);

                            // Load enemy
                            Engine.tiles.add(skeleton);
                            break;

                        case "Thief":
                            // Create enemy type
                            Thief thief = new Thief(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])));
                            thief.setEnemyLife(Integer.parseInt(objStringTokens[4]));
                            gui.addObserver(thief);

                            // Load enemy
                            Engine.tiles.add(thief);
                            break;

                        case "Fire":
                            Fire fire = new Fire(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])));

                            // Load item
                            Engine.tiles.add(fire);
                            break;

                        case "GoodMeat":
                            GoodMeat goodMeat = new GoodMeat(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])));

                            // Load item
                            Engine.tiles.add(goodMeat);
                            break;

                        case "Hammer":
                            Hammer hammer = new Hammer(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])));

                            // Load item
                            Engine.tiles.add(hammer);
                            break;

                        case "Sword":
                            Sword sword = new Sword(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])));

                            // Load item
                            Engine.tiles.add(sword);
                            break;

                        case "Key":
                            Key key = new Key(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])),
                                    Integer.parseInt(objStringTokens[4]), Integer.parseInt(objStringTokens[5]));

                            // Load item
                            Engine.tiles.add(key);
                            break;

                        case "DoorClosed":
                            DoorClosed doorClosed = new DoorClosed(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])),
                                    Integer.parseInt(objStringTokens[4]), objStringTokens[5], objStringTokens[6], Integer.parseInt(objStringTokens[7]));

                            // Load item
                            Engine.tiles.add(doorClosed);
                            break;

                        case "DoorOpen":
                            DoorOpen doorOpen = new DoorOpen(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])),
                                    Integer.parseInt(objStringTokens[4]), objStringTokens[5], objStringTokens[6], Integer.parseInt(objStringTokens[7]));

                            if (Engine.hero.getEntryDoorNr() == Integer.parseInt(objStringTokens[4])) {
                                Engine.hero.setPosition(doorOpen.getPosition());
                            }

                            // Load item
                            Engine.tiles.add(doorOpen);
                            break;

                        case "DoorWay":
                            DoorWay doorWay = new DoorWay(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])),
                                    Integer.parseInt(objStringTokens[4]), objStringTokens[5], objStringTokens[6], Integer.parseInt(objStringTokens[7]));

                            if (Engine.hero.getEntryDoorNr() == Integer.parseInt(objStringTokens[4])) {
                                Engine.hero.setPosition(doorWay.getPosition());
                            }

                            // Load item
                            Engine.tiles.add(doorWay);
                            break;

                        case "StairsDown":
                            StairsDown stairsDown = new StairsDown(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])),
                                    Integer.parseInt(objStringTokens[4]), objStringTokens[5], objStringTokens[6], Integer.parseInt(objStringTokens[7]));

                            if (Engine.hero.getEntryDoorNr() == Integer.parseInt(objStringTokens[4])) {
                                Engine.hero.setPosition(stairsDown.getPosition());
                            }

                            // Load item
                            Engine.tiles.add(stairsDown);
                            break;

                        case "StairsUp":
                            StairsUp stairsUp = new StairsUp(new Position(Integer.parseInt(objStringTokens[2]), Integer.parseInt(objStringTokens[3])),
                                    Integer.parseInt(objStringTokens[4]), objStringTokens[5], objStringTokens[6], Integer.parseInt(objStringTokens[7]));

                            if (Engine.hero.getEntryDoorNr() == Integer.parseInt(objStringTokens[4])) {
                                Engine.hero.setPosition(stairsUp.getPosition());
                            }

                            // Load item
                            Engine.tiles.add(stairsUp);
                            break;
                    }
                }
            }


        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Save file not found! File: " + saveFile);
        }

    }

    private void loadRoomImmutable(int heroRoomNr) {
        // Loads all immutable objects, like floor, walls...
        // Room file to read
        File fileRoom = new File("rooms/room" + heroRoomNr + ".txt");


        // Create Room objects
        try {

            Scanner fileScanner = new Scanner(fileRoom);

            int i = 0;
            while (fileScanner.hasNextLine()) {
                String nextLine = fileScanner.nextLine();
                String[] separar = nextLine.split("");

                if (separar[0].equals("*") || separar[0].equals("#")) {
                    continue;

                } else {
                    for (int j = 0; j < separar.length; j++) {

                        switch (separar[j]) {
                            case "W":
                                // Add Wall
                                Engine.tiles.add(new Wall(new Position(j, i)));
                                break;


                            case "g":
                                // Add Grass
                                Engine.tiles.add(new Grass(new Position(j, i)));
                                break;

                            case "x":
                                // Add Trap
                                Engine.tiles.add(new Trap(new Position(j, i)));
                                break;
                        }

                    }

                    i++;
                }

            }

            fileScanner.close();

        } catch (
                FileNotFoundException e) {
            System.out.println("Não foi possível ler o ficheiro do mapa.");
        }

    }

    public void addFloor() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Engine.tiles.add(new Floor(new Position(i, j)));
            }
        }
    }

    public void loadGame() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // Empty Start
        gui.deleteObservers();
        Engine.tiles.clear();
        gui.clearImages();
        gui.clearStatus();

        // Add tiles to Engine.tiles
        addFloor();
        loadGameHero();     // Hero Updated
        loadRoomImmutable(Engine.hero.getHeroRoomNr());
        loadGameRooms(Engine.hero.getHeroRoomNr());

        // Hero loads last, so he isn't overlapped by other tiles
        Engine.tiles.add(Engine.hero);

        // Sort tiles according to rank, to avoid unwanted overlapping, some objects hiding others...
        CompareImageTile compare = new CompareImageTile();
        Engine.tiles.sort(compare);

        // Load all tiles into the GUI
        gui.newImages(Engine.tiles);

        gui.addObserver(Engine.hero);
    }

    public void loadGameRoom(int roomNr) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // Empty Start
        clearTilesObserversGUI();
        Engine.hero.setHeroRoomNr(roomNr);


        // Add tiles to Engine.tiles
        addFloor();
        loadRoomImmutable(roomNr);
        loadGameRooms(roomNr);

        // Hero loads last, so he isn't overlapped by other tiles
        Engine.tiles.remove(Engine.hero);
        Engine.tiles.add(Engine.hero);

        // Load all tiles into the GUI
        gui.newImages(Engine.tiles);

        gui.addObserver(Engine.hero);
    }

    //-------//
    // Clear //
    //-------//
    public void clearTilesObserversGUI() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // Empty Start
        Engine.tiles.clear();
        gui.clearImages();
        gui.deleteObservers();
    }

    public void clearSaveFiles() {
        saveFile.delete();
        saveHero.delete();
        fileTemp.delete();
        currentRoomFile.delete();
    }

}
