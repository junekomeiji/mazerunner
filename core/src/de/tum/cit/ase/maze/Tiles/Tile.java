package de.tum.cit.ase.maze.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Tile {
    protected Texture texture;
    protected TextureRegion textureRegion;
    int xoffset;
    int yoffset;

    public Tile(int xoffset, int yoffset) {
        texture = new Texture(Gdx.files.internal("basictiles.png"));
        this.xoffset = xoffset;
        this.yoffset = yoffset;
        textureRegion = new TextureRegion(texture, xoffset, yoffset, 16, 16);
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}
