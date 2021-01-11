package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.FireTile;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.enemies.Enemy;
import pt.upskills.projeto.objects.room.Wall;
import pt.upskills.projeto.rogue.utils.Position;

// This is the object that can be thrown
public class FireBall extends Items implements FireTile {
    // Attributes
    private Position position;

    // Constructor
    public FireBall(Position position) {
        this.position = position;
    }

    // Methods
    @Override
    public String getName() {
        return "Fire";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean validateImpact() {
        //return false when hit an object
        for (ImageTile tile : Engine.tiles) {
            if (tile instanceof Wall) {
                if (tile.getPosition().equals(position)) {
                    removeFireTile();
                    return false;
                }
            } else if (tile instanceof Enemy) {
                if (tile.getPosition().equals(position)) {
                    // System.out.println("Burn Enemy!!!");
                    ((Enemy) tile).takeDamage(20);
                    removeFireTile();
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Fire{" +
                "position=" + position +
                '}';
    }

    public void removeFireTile(){
        // If not removed the Fire object continues to exist in the Engine
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        Engine.tiles.remove(this);
        gui.removeImage(this);
    }
}
