package pt.upskills.projeto.objects.enemies;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.objects.room.Room;
import pt.upskills.projeto.objects.room.Wall;
import pt.upskills.projeto.pathfinding.Pathfinding;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.Observable;
import java.util.Observer;

import static pt.upskills.projeto.game.Engine.hero;

public class Skeleton extends Enemy implements Observer {
    // Attributes

    // Constructor
    public Skeleton(Position position) {
        super(position);
    }

    // Methods
    @Override
    public String getName() {
        return "Skeleton";
    }

    @Override
    public void update(Observable o, Object arg) {
        Moves moves = new Moves();

        int[][] map = new int[10][10];

        for (ImageTile tile : Engine.tiles) {
            if (tile instanceof Room || (tile instanceof Enemy && !tile.equals(this))){
                map[tile.getPosition().getY()][tile.getPosition().getX()] = 1;
            }
        }

        // If next to Hero, perform attack
        if (distanceToHero() == 1) {
            //System.out.println("Attack!");
            // Perform attack
            hero.takeDamage(getAttackDamage());
        } else if (distanceToHero() <= 20) {
            // If near Hero, move towards him (20 squares)
            Pathfinding path = new Pathfinding(this.getPosition(), hero.getPosition(), map);
            Position pos = path.algorithm();
            if (pos!= null && !pos.equals(hero.getPosition())) {
                this.setPosition(pos);
            } else {
                //try random move
                Position randomMove = moves.randomMove(this);
                if (randomMove != null) {
                    setPosition(randomMove);
                }
            }
            // If move not possible or distance is large, tries a random move
        } else {
            Position randomMove = moves.randomMove(this);
            if (randomMove != null) {
                setPosition(randomMove);
            }
        }
    }
}
