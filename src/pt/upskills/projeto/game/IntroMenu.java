package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.leaderboard.Leaderboard;
import pt.upskills.projeto.leaderboard.Player;
import pt.upskills.projeto.savefiles.Save;

import java.io.File;
import java.util.Scanner;

public class IntroMenu {
    public static void main(String[] args) {
        IntroMenu intro = new IntroMenu(false);
        intro.loadMenu();
    }

    // Attributes
    private boolean fromGame;

    // Constructor
    public IntroMenu(boolean fromGame) {
        this.fromGame = fromGame;
    }

    // Methods
    public void loadMenu() {
        Scanner keyboardScanner = new Scanner(System.in);
        String userChoice;
        int choice;
        boolean condition = true;

        while (condition) {
            try {

                System.out.println("-----------------------------------");
                System.out.println("###         Rogue Game          ###");
                System.out.println("-----------------------------------");
                System.out.println("    1. Start new game");
                System.out.println("    2. Load previous game");
                System.out.println("    3. Show Leaderboard");
                System.out.println("    4. Exit game\n");
                System.out.print("    Enter your option: ");
                userChoice = keyboardScanner.nextLine();
                choice = Integer.parseInt(userChoice);


            } catch (NumberFormatException e) {
                System.out.print("    Enter your a valid option: ");
                userChoice = keyboardScanner.nextLine();
                choice = Integer.parseInt(userChoice);
            }

            switch (choice) {
                case 1:
                    condition = false;
                    clearPreviousSave();
                    startNewGame();

                    break;
                case 2:
                    Save save = new Save();
                    if (save.saveExists()) {
                        condition = false;
                        loadLastGame();
                    } else {
                        System.out.println("\nNo game is saved!\n");
                    }
                    break;
                case 3:
                    showLeaderboard();
                    break;
                case 4:
                default:
                    System.exit(0);
            }
        }
    }

    private void clearPreviousSave() {
        Save save = new Save();
        save.clearSaveFiles();
    }

    private void startNewGame() {
        Engine engine = new Engine();
        engine.init(false);

    }

    private void loadLastGame() {
        if (fromGame) {
            ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
            Save save = new Save();
            save.loadGame();
            gui.go();
        } else {
            Engine engine = new Engine();
            engine.init(true);
        }
    }

    private void showLeaderboard() {
        //Leaderboard;
        File leaderboardFile = new File("leaderboards/board1.txt");

        Leaderboard lb = new Leaderboard(leaderboardFile);

        lb.loadFile();
        lb.printBoard(10);
        System.out.println();
    }

    public void saveScoreToLeaderBoard(int score) {
        File leaderboardFile = new File("leaderboards/board1.txt");
        Leaderboard leaderboard = new Leaderboard(leaderboardFile);

        Scanner keyboardScanner = new Scanner(System.in);

        System.out.println("You had a score of: " + score);
        System.out.println("Enter your name: ");
        String name = keyboardScanner.nextLine();

        if (name.equals("")) {
            name = "Player 1";
        }

        Player player = new Player(name, score);

        leaderboard.loadFile();
        leaderboard.addScore(player);
        leaderboard.saveFile();

        showLeaderboard();
    }


}
