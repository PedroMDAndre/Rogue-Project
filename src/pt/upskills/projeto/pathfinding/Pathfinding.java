package pt.upskills.projeto.pathfinding;

import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.rogue.utils.Vector2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// https://towardsdatascience.com/a-star-a-search-algorithm-eb495fb156bb
public class Pathfinding {
    public static void main(String[] args) {
        int[][] mapa = new int[10][10];
        {mapa[0][1] = 1;
        mapa[0][2] = 1;
        mapa[0][3] = 1;
        mapa[0][5] = 1;
        mapa[0][6] = 1;
        mapa[0][7] = 1;
        mapa[0][8] = 1;
        mapa[0][4] = 1;


        mapa[1][2] = 0;
        mapa[2][2] = 1;
        mapa[3][2] = 1;
        mapa[4][2] = 1;
        mapa[5][2] = 1;
        mapa[6][2] = 1;
        mapa[7][2] = 1;
        mapa[8][2] = 1;

        mapa[1][4] = 1;
        mapa[4][3] = 1;}

        System.out.println("Starting map:");
        printMap(mapa);

        Pathfinding pathfinding = new Pathfinding(new Position(0, 0), new Position(8, 1), mapa);
        System.out.println(pathfinding.algorithm());

    }


    // Attributes
    private ArrayList<Node> openList;      // 1.  Initialize the open list
    private ArrayList<Node> closeList;     // 2.  Initialize the closed list
    private Position startPosition;
    private Position endPosition;
    private int[][] map;

    public Pathfinding(Position startPosition, Position endPosition, int[][] map) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.openList = new ArrayList<>();
        this.closeList = new ArrayList<>();
        this.map = map;
        int h = manhattanDistance(startPosition, endPosition);

        Node initialNode = new Node(null, startPosition, 0, h);
        this.openList.add(initialNode);        // put the starting node on the open list (you can leave its f at zero)
    }

    // A* Search Algorithm
    public Position algorithm() {
        Set<Position> invalidVisitedPositions = new HashSet<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 1) {
                    //invalidVisitedPositions.contains(new Position(j, i));
                    invalidVisitedPositions.add(new Position(j, i));
                }
            }
        }


        //3.  while the open list is not empty
        while (!openList.isEmpty()) {
            // a) find the node with the least f on the open list, call it "q"
            Node q = openList.get(0); // 1st try for the least f
            for (Node node : openList) {
                if (node.getF() < q.getF()) {
                    q = node;
                }
            }


            // b) pop q off the open list
            openList.remove(q);

            // c) generate q's 8 successors and set their parents to q
            ArrayList<Vector2D> directions = listVectorDirections();


            for (Vector2D vector : directions) {
                Position position = q.getPosition().plus(vector);


                if (position.getX() < 0 || position.getX() > 9 || position.getY() < 0 || position.getY() > 9) {
                    continue;
                }

                if (!isInList(position, invalidVisitedPositions)) {
                    int g = q.getG() + Math.abs(vector.getX()) + Math.abs(vector.getY());
                    int h = manhattanDistance(position, endPosition);
                    Node newNode = new Node(q, position, g, h);
                    Node inList = nodeInList(openList, newNode);

                    if (inList != null) {
                        if (inList.getG() > newNode.getG()) {
                            openList.remove(inList);
                            openList.add(newNode);
                        }
                    } else {

                        openList.add(newNode);

                        if (position.equals(endPosition)) {
                            System.out.println(newNode);
                            printMap(finalPath(map, newNode));
                            return nextMove(newNode.toString());
                        }

                    }
                }


                // e) push q on the closed list
                closeList.add(q);

                // Add to invalid positions so it isn't used again
                invalidVisitedPositions.add(q.getPosition());
            }
        }
        return nextMove(nodeWithLeastG(closeList).toString());
    }


    public Node nodeInList(ArrayList<Node> nodeList, Node node) {
        for (Node i : nodeList) {
            if (i.equals(node)) {
                return i;
            }
        }
        return null;
    }

    public boolean isInList(Position position, Set<Position> list) {
        for (Position pos : list) {
            if (pos.equals(position)) {
                return true;
            }
        }
        return false;
    }

    private Node nodeWithLeastG(ArrayList<Node> finalNodes) {
        if (finalNodes != null) {
            Node nodeMinG = finalNodes.get(0);
            for (Node node : finalNodes) {
                if(node.getG()<node.getG()){
                    nodeMinG = node;
                }
            }
            return nodeMinG;
        } else {
            return null;
        }
    }


    /**
     * Heuristic<p>
     * Sum of absolute values of differences in the goal’s x and y coordinates and the current cell’s <p>
     * Used when? When we are allowed to move only in four directions only (right, left, top, bottom)
     */
    public static int manhattanDistance(Position current, Position goal) {
        int distance = Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY());
        return distance;
    }

    /**
     * Heuristic<p>
     * It is the maximum of absolute values of differences in the goal’s x and y coordinates and the current
     * cell’s x and y coordinates respectively <p>
     * Used when? When we are allowed to move in eight directions only (similar to a move of a King in Chess)
     */
    public static int diagonalDistance(Position current, Position goal) {
        int distance = Math.max(Math.abs(current.getX() - goal.getX()), Math.abs(current.getY() - goal.getY()));
        return distance;
    }

    /**
     * Heuristic<p>
     * It is the maximum of absolute values of differences in the goal’s x and y coordinates and the current
     * cell’s x and y coordinates respectively <p>
     * Used when? When we are allowed to move in eight directions only (similar to a move of a King in Chess)
     * <p><i>The result is converted into an Integer
     */
    public static int euclideanDistance(Position current, Position goal) {
        int distance = (int) Math.sqrt(Math.pow(current.getX() - goal.getX(), 2) +
                Math.pow(current.getY() - goal.getY(), 2));
        return distance;
    }

    public Position nextMove(String input) {
        String[] inputTokens = input.split(";");
        if (inputTokens.length > 2) {
            String[] strPosition = inputTokens[2].split(",");
            int x = Integer.parseInt(strPosition[0].trim().replace("(", ""));
            int y = Integer.parseInt(strPosition[1].trim().replace(")", ""));
            return new Position(x, y);
        }
        return null;
    }

    private ArrayList<Vector2D> listVectorDirections() {
        ArrayList<Vector2D> directions = new ArrayList<>();
        directions.add(new Vector2D(0, 1));
        directions.add(new Vector2D(-1, 0));
        directions.add(new Vector2D(0, -1));
        directions.add(new Vector2D(1, -1));
        directions.add(new Vector2D(1, 0));
        directions.add(new Vector2D(-1, -1));
        directions.add(new Vector2D(-1, 1));
        directions.add(new Vector2D(1, 1));

        return directions;
    }

    public static void printMap(int[][] map){
        for (int[] x : map) {
            for (int y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }

    public static int[][] finalPath(int[][] map, Node node){
        String input = node.toString();
        String[] inputTokens = input.split(";");

        for (int i = 1; i < inputTokens.length; i++) {
            String[] strPosition = inputTokens[i].split(",");
            int x = Integer.parseInt(strPosition[0].trim().replace("(", ""));
            int y = Integer.parseInt(strPosition[1].trim().replace(")", ""));
            map[y][x] = 2;
        }
        return map;
    }

}
