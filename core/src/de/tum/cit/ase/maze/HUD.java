package de.tum.cit.ase.maze;

import de.tum.cit.ase.maze.Entities.Mobs.Player;

public class HUD {

    private int lives;
    private int key;
    private int score;
    public HUD(Player p){
        int lives = p.getLives();
        int key = p.getScore();
    }
}
