package pt.upskills.projeto.objects.enemies;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageMatrixGUI;

import pt.upskills.projeto.objects.items.GoodMeat;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.Observable;
import java.util.Observer;

import static pt.upskills.projeto.game.Engine.hero;

public class Bat extends Enemy implements Observer {
    // Attributes

    // Constructor
    public Bat(Position position) {
        super(position);
    }

    // Methods
    @Override
    public String getName() {
        return "Bat";
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
            Position newMove = moves.nextMoveTowardsHero(hero.getPosition(), this.getPosition());
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

    @Override
    public void takeDamage(int damage) {
        setEnemyLife(getEnemyLife() - damage);
        if (getEnemyLife() <= 0) {
            // Enemy dies
            // Remove enemy
            removeEnemy();

            // Add Score for Killing
            Engine.hero.addScore(EnemyInfo.BAT.killScore());

            // Drops an object GoodMeat, has a chance of 70%
            dropMeat(0.7);
        }
    }

    private void dropMeat(double chance){
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        double randNum = Math.random();
        if(randNum > 1-chance){
            GoodMeat meat = new GoodMeat(this.getPosition());
            Engine.tiles.add(meat);
            gui.addImage(meat);
        }
    }
}
