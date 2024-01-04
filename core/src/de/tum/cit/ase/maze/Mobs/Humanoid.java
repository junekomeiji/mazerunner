package de.tum.cit.ase.maze.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import de.tum.cit.ase.maze.Character;

public class Humanoid extends Character {
    public Humanoid(int xpos, int ypos, int direction){
        super(xpos, ypos, direction);
        this.texture = new Texture(Gdx.files.internal("mobs.png"));
    }
}
