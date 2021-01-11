package pt.upskills.projeto.objects.enemies;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.objects.room.Room;
import pt.upskills.projeto.objects.room.Trap;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.rogue.utils.Vector2D;

import java.util.Random;

public class Moves {
    public Moves() {
    }

    /**
     * Gives the position of points to generate a line between two points,
     * adapted from https://www.geeksforgeeks.org/bresenhams-line-generation-algorithm/
     *
     * @return a position for the next move
     */
    public Position nextMoveTowardsHero(Position heroPosition, Position enemyPosition) {
        int x0, y0, xf, yf;
        x0 = enemyPosition.getX();
        y0 = enemyPosition.getY();
        xf = heroPosition.getX();
        yf = heroPosition.getY();

        double x = x0;
        double y = y0;

        double deltaX = (xf - x0);
        double deltaY = (yf - y0);

        int x_increment = 1;
        int y_increment = 1;

        if (deltaX < 0) {
            x_increment = -1;
        }
        if (deltaY < 0) {
            y_increment = -1;
        }

        if (deltaX == 0) {
            return new Position((int) x, (int) (y + y_increment));
        } else if (deltaY == 0) {
            return new Position((int) (x + x_increment), (int) y);
        } else {

            double slopeM = deltaY / deltaX;
            double intersectB = yf - slopeM * xf;


            double next_xa = x + x_increment;                                            // distance by increment x = x + 1 or x -1
            double next_ya = Math.round(next_xa * slopeM + intersectB);
            double dx = Math.abs(Math.pow(next_xa - x, 2) + Math.pow(next_ya - y, 2));   // distance by increment x = x + 1 or x -1

            double next_yb = y + y_increment;                                            // distance by increment y = y + 1 or y - 1
            double next_xb = Math.round((next_yb - intersectB) / slopeM);
            double dy = Math.abs(Math.pow(next_xb - x, 2) + Math.pow(next_yb - y, 2));   // distance by increment y = y + 1 or y - 1

            if (dx < dy) {
                x = next_xa;
                y = next_ya;
            } else {
                x = next_xb;
                y = next_yb;
            }


            return new Position((int) x, (int) y);
        }
    }

    public Position nextMoveTowardsHeroByThief(Position heroPosition, Position enemyPosition) {
        int x0, y0, xf, yf;
        x0 = enemyPosition.getX();
        y0 = enemyPosition.getY();
        xf = heroPosition.getX();
        yf = heroPosition.getY();

        int x = x0;
        int y = y0;

        int deltaX = (xf - x0);
        int deltaY = (yf - y0);

        int x_increment = 1;
        int y_increment = 1;

        if (deltaX < 0) {
            x_increment = -1;
        }
        if (deltaY < 0) {
            y_increment = -1;
        }


        int next_xa = x + x_increment;                                            // distance by increment x = x + 1 or x -1
        int next_ya = y + y_increment;


        return new Position(next_xa, next_ya);
    }

    public Position randomMove(Enemy enemy) {
        Vector2D randomMove;
        int i = 0;
        while (i < 8) {
            if (enemy instanceof Thief) {
                randomMove = randomCalDirectionThief();
            } else {
                randomMove = randomCalcDirection();
            }

            if (isMovPossible(enemy.getPosition().plus(randomMove), enemy)) {
                return (enemy.getPosition().plus(randomMove));
            }
            i++;
        }
        return null;
    }

    public boolean isMovPossible(Position newPosition, Enemy enemy) {
        for (ImageTile i : Engine.tiles) {
            // Verifica se existe um objecto para a posição a mover
            if (i.getPosition().equals(newPosition)) {
                if (enemy instanceof Bat && i instanceof Trap) {// Checks if Object is a Trap and Enemy a Bat
                    // Bats can stay above traps
                    // System.out.println("Bat move to trap " + position);
                    return true;
                } else if (i instanceof Room) {     // Checks if Object is from Room Class
                    return false;
                } else if (i instanceof Enemy) {    // Checks if it is an Enemy
                    return false;
                } else if (i instanceof Hero) {     // Checks if it is the Hero
                    return false;
                }
                // Não sai fora do mapa se não existir parede.
            } else if (newPosition.getX() < 0 || newPosition.getX() > 9 || newPosition.getY() < 0 || newPosition.getY() > 9) {
                return false;
            }
        }
        return true;
    }

    private Vector2D randomCalcDirection() {
        // Generate direction for Down/Right/Up/Left
        Random random = new Random();
        boolean val1 = random.nextBoolean();
        int[] values = {-1, 0, 1};
        int position = random.nextInt(3);
        int x = 0;
        int y = 0;

        if (val1) {
            // Move in x position
            x = values[position];
        } else {
            // move in y position
            y = values[position];
        }
        return new Vector2D(x, y);
    }

    private Vector2D randomCalDirectionThief() {
        // Method to generate a binary results
        // For bishop move type
        Random random = new Random();
        int[] values = {-1, 1};
        int positionX = random.nextInt(2);
        int positionY = random.nextInt(2);

        int x = values[positionX];
        int y = values[positionY];

        return new Vector2D(x, y);
    }

    /**
     * Check if the enemy can move.
     *
     * @return true if there is at least one movement possible in the 4 directions
     */
    public boolean hasPossibleMoves(Enemy enemy) {
        for (Direction direction : Direction.values()) {
            if (isMovPossible(enemy.getPosition().plus(direction.asVector()), enemy)) {
                return true;
            }
        }
        return false;
    }

}