package pt.upskills.projeto.leaderboard;

public class Player implements Comparable<Player> {
    // Attributes
    private String name;
    private int score;

    // Constructor
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }


    // Methods
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return name + ";" + score;
    }

    @Override
    public int compareTo(Player o) {
        if (this.score > o.score) {
            return -1;
        } else if (this.score < o.score) {
            return 1;
        } else {
            return 0;
        }
    }
}
