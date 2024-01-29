package de.tum.cit.ase.maze.Entities.Things;

public class Chest extends Thing{

    public Chest(int xpos, int ypos, int direction){
        super(xpos, ypos, direction, 96, 0);
    }

    // Changes appearance of chest from closed to open
    @Override
    public void open(){
        this.setDirection(1);
    }
}
