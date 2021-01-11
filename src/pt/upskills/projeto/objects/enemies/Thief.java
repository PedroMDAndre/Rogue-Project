package pt.upskills.projeto.objects.enemies;

import pt.upskills.projeto.rogue.utils.Position;

import java.util.Observable;
import java.util.Observer;

import static pt.upskills.projeto.game.Engine.hero;

public class Thief extends Enemy implements Observer {
    // Attributes

    // Constructor
    public Thief(Position position) {
        super(position);
    }

    // Methods
    @Override
    public String getName() {
        return "Thief";
    }

    @Override
    public void update(Observable o, Object arg) {
        Moves moves = new Moves();

        // If next to Hero, perform attack
        if (distanceToHero() == 1) {
            //System.out.println("Attack!");
            // Perform attack
            hero.takeDamage(getAttackDamage());
        } else if (distanceToHero() <= 10) {
            // If near Hero, move towards him (10 squares)
            Position newMove = moves.nextMoveTowardsHeroByThief(hero.getPosition(), this.getPosition());
            if (moves.isMovPossible(newMove, this)) {
                this.setPosition(newMove);
            } else {
                //try random move
                Position randomMove = moves.randomMove(this);
                if(randomMove != null){
                    setPosition(randomMove);
                }
            }
            // If move not possible or distance is large, tries a random move
        } else {
            Position randomMove = moves.randomMove(this);
            if(randomMove != null){
                setPosition(randomMove);
            }
        }
    }
}
