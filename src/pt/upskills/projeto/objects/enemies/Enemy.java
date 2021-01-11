package pt.upskills.projeto.objects.enemies;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.Observer;

import static pt.upskills.projeto.game.Engine.hero;

public abstract class Enemy implements ImageTile, Observer {
    // Attributes
    private Position position;
    private int attackDamage;
    private int enemyLife;

    // Constructor
    public Enemy(Position position){
        this.position = position;
        this.attackDamage = EnemyInfo.valueOf(getName().toUpperCase()).attackDamage();
        this.enemyLife = EnemyInfo.valueOf(getName().toUpperCase()).enemyLife();
    }

    // Methods
    //-----//
    // Set //
    //-----//
    public void setPosition(Position position) {
        this.position = position;
    }

    public void setEnemyLife(int enemyLife) {
        this.enemyLife = enemyLife;
    }

    //-----//
    // Get //
    //-----//
    @Override
    public Position getPosition() {
        return position;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getEnemyLife() {
        return enemyLife;
    }

    public void takeDamage(int damage) {
        setEnemyLife(getEnemyLife() - damage);
        if (getEnemyLife() <= 0) {
            // Enemy dies
            // Remove enemy
            removeEnemy();

            // Add Score for Killing
            hero.addScore(EnemyInfo.valueOf(getName().toUpperCase()).killScore());
        }
    }

    @Override
    public int getRank() {
        return 2;
    }


    // Position Methods //
    public int distanceToHero() {
        // Calculates the number of squares from hero to enemy, does not consider obstacles in between
        Position heroPosition = Engine.hero.getPosition();
        int xHero = heroPosition.getX();
        int yHero = heroPosition.getY();

        int xEnemy = this.position.getX();
        int yEnemy = this.position.getY();

        return Math.abs(xHero - xEnemy) + Math.abs(yHero - yEnemy);
    }

    public void removeEnemy(){
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // Remove enemy
        gui.deleteObserver(this);
        gui.removeImage(this);
        Engine.tiles.remove(this);
    }

    // Abstract Methods //


    @Override
    public String toString() {
        return getName() + ";" + getPosition().getX() + ";" + getPosition().getY() + ";" + enemyLife;
    }
}