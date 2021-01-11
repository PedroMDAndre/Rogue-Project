package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageTile;

import java.util.Comparator;

public class CompareImageTile implements Comparator<ImageTile> {
    @Override
    public int compare(ImageTile o1, ImageTile o2) {
        if(o1.getRank() > o2.getRank()){
            return -1;
        }else if(o1.getRank() < o2.getRank()){
            return 1;
        }
        return 0;
    }

}
