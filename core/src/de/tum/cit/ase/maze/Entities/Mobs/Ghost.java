package de.tum.cit.ase.maze.Entities.Mobs;

public class Ghost extends Mob{
    public Ghost(int xpos, int ypos, int direction) {
        super(xpos, ypos, direction, 96, 64);
        this.setHealth(3);
    }

}
