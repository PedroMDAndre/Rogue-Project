package pt.upskills.projeto.leaderboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Leaderboard {
    public static void main(String[] args) {
        // Just for testing...
        File leaderboardFile = new File("leaderboards/board1.txt");

        Leaderboard lb = new Leaderboard(leaderboardFile);

        Player p1 = new Player("Jane", 1010);
        Player p2 = new Player("José", 1200);
        Player p3 = new Player("Carlos", 1150);
        Player p4 = new Player("António", 1040);
        Player p5 = new Player("Fernando", 1030);

        lb.addScore(p1);
        lb.addScore(p2);
        lb.addScore(p3);
        lb.addScore(p4);
        lb.addScore(p5);

        lb.printBoard(10);

        lb.saveFile();
        /*lb.loadFile();
        lb.printBoard(10);*/

    }

    // Attributes
    private File fileLoad;
    private ArrayList<Player> board;

    // Constructor
    public Leaderboard(File fileLoad) {
        this.fileLoad = fileLoad;
        this.board = new ArrayList<>();
    }

    // Methods
    public void loadFile() {
        try {
            Scanner fileScanner = new Scanner(fileLoad);

            // Add information from file to ArrayList board
            while (fileScanner.hasNextLine()) {
                String fileLine = fileScanner.nextLine();
                String[] lineTokens = fileLine.split(";");
                board.add(new Player(lineTokens[0], Integer.parseInt(lineTokens[1])));
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Leaderboard file not found!");
        }
    }

    public void saveFile() {
        board.sort(Player::compareTo);
        try {

            PrintWriter printWriter = new PrintWriter(fileLoad);
            for (Player player : board) {
                printWriter.println(player);
            }

            printWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addScore(Player player) {
        // adds score, independently if it was the player high score
        board.add(player);
    }

    public void addHighScore(Player player) {
        // adds only if it is player best result and replaces the previous lower score
        int playerPosition = hasPlayer(player);

        if (playerPosition == -1) {
            board.add(player);
        } else {
            if (player.getScore() > board.get(playerPosition).getScore()) {
                board.remove(playerPosition);
                board.add(player);
            } else {
                // If the player exists but the score to add was lower, it won't be added.
                System.out.println("Player has a higher score in the Leaderboard.");
            }
        }
    }

    private int hasPlayer(Player checkPlayer) {
        // Checks if a player is present in the board
        // Returns their position if exists (positive integer)
        // Returns -1 if the player not present

        for (Player iplayer : board) {
            if (iplayer.getName().equals(checkPlayer.getName())) {
                return board.indexOf(iplayer);
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        return "Leaderboard{" +
                "fileLoad=" + fileLoad +
                '}';
    }

    public void printBoard(int topX) {
        board.sort(Player::compareTo);

        if (topX > board.size()) {
            topX = board.size();
        }

        System.out.println("-----------------------------------");
        System.out.println("###         Leaderboard         ###");
        System.out.println("-----------------------------------");
        System.out.printf("    %-20s %10s%n", "Name:", "Score:");

        for (int i = 0; i < topX; i++) {
            Player player = board.get(i);
            System.out.printf("%3d %-20s %10s%n", i + 1, player.getName(), player.getScore());
        }
    }



}
