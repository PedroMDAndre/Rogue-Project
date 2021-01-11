package pt.upskills.projeto.pathfinding;

import java.util.ArrayList;

public class Path {
    // Attributes
    private ArrayList<Node> predecessors;
    private Node successor;

    // Constructor

    public Path(ArrayList<Node> predecessors, Node successor) {
        this.predecessors = predecessors;
        this.successor = successor;
    }


    // Methods
    public ArrayList<Node> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(ArrayList<Node> predecessors) {
        this.predecessors = predecessors;
    }

    public Node getSuccessor() {
        return successor;
    }

    public void setSuccessor(Node successor) {
        this.successor = successor;
    }

    @Override
    public String toString() {
        return "Path{" +
                "predecessors=" + predecessors +
                ", successor=" + successor +
                '}';
    }
}
