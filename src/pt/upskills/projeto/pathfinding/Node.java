package pt.upskills.projeto.pathfinding;

// https://www.geeksforgeeks.org/a-search-algorithm/

import pt.upskills.projeto.rogue.utils.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Node {
    public static void main(String[] args) {
        Set<Position> positions = new HashSet<>();
        Position a = new Position(1, 2);
        Position b = new Position(1, 2);
        positions.add(a);
        System.out.println(positions.contains(b));
    }

    // Attributes
    private Node parentNode;
    private Position position;
    private int f;
    private int g;      // the movement cost to move from the starting point to a given square on the grid, following the path generated to get there.
    private int h;      // the estimated movement cost to move from that given square on the grid to the final destination.

    // Constructor
    public Node(Node parentNode, Position position, int g, int h) {
        this.parentNode = parentNode;
        this.position = position;
        this.g = g;
        this.h = h;
        this.f = g + h;

    }

    public Node(Position position) {
        this.position = position;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    // Methods

    public Position getPosition() {
        return new Position(position.getX(), position.getY());
    }

    public void setPosition(Position position) {
        this.position = new Position(position.getX(), position.getY());
    }

    public int x() {
        return position.getX();
    }

    public int y() {
        return position.getY();
    }

    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }


    @Override
    /*public String toString() {
        return "Node{" +
                "parentNode=" + parentNode +
                ", position=" + position +
                ", f=" + f +
                ", g=" + g +
                ", h=" + h +
                '}';
    }*/

    public String toString() {
        return parentNode + ";" + position;
    }

    public boolean equals(Node node) {
        return this.position.equals(node.getPosition());
    }
}
