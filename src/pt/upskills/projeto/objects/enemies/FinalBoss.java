package pt.upskills.projeto.objects.enemies;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.game.IntroMenu;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.savefiles.Save;

import java.util.Observable;

import static pt.upskills.projeto.game.Engine.hero;

public class FinalBoss extends Enemy {
    // Attributes

    // Constructor
    public FinalBoss(Position position) {
        super(position);
    }

    // Methods
    @Override
    public String getName() {
        return "FinalBoss";
    }

    @Override
    public void update(Observable o, Object arg) {
        Moves moves = new Moves();

        // If next to Hero, perform attack
        if (distanceToHero() == 1) {
            //System.out.println("Attack!");
            // Perform attack
            hero.takeDamage(getAttackDamage());
        } else if (distanceToHero() <= 20) {
            // If near Hero, move towards him (20 squares)
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
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

        setEnemyLife(getEnemyLife() - damage);
        if (getEnemyLife() <= 0) {
            // Enemy dies
            // Remove enemy
            removeEnemy();

            // Add Score for Killing
            Engine.hero.addScore(EnemyInfo.FINALBOSS.killScore());

            // Eliminate save files
            Save save = new Save();
            save.clearSaveFiles();

            // Save Score
            IntroMenu menu = new IntroMenu(true);
            System.out.println("\n--- YOU WON THE GAME ---\n");
            menu.saveScoreToLeaderBoard(Engine.hero.getScore());
            System.exit(0);
        }
    }
}
