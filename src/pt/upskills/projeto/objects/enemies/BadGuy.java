package pt.upskills.projeto.objects.enemies;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.game.IntroMenu;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.objects.doors.Door;
import pt.upskills.projeto.objects.doors.DoorOpen;
import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.savefiles.Save;

import java.util.Observable;

import static pt.upskills.projeto.game.Engine.hero;

public class BadGuy extends Enemy {
    // Attributes

    // Constructor
    public BadGuy(Position position) {
        super(position);
    }

    // Methods
    @Override
    public String getName() {
        return "BadGuy";
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
            Engine.hero.addScore(EnemyInfo.BADGUY.killScore());

            // hero enters new room
            DoorOpen door = new DoorOpen(new Position(5,5),0,"U","room6.txt",0);
            ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
            Engine.tiles.add(door);
            gui.addImage(door);
            Engine.hero.heroEnterRoom(door);
        }
    }
}
