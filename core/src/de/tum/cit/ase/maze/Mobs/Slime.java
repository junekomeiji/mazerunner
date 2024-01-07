package de.tum.cit.ase.maze.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Slime extends Mob{
    public Slime(int xpos, int ypos, int direction){
        super(xpos, ypos, direction, 0, 64);
    }

}
