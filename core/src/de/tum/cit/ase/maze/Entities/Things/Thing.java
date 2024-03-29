package de.tum.cit.ase.maze.Entities.Things;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Entities.Entity;

public abstract class Thing extends Entity {
    int xoffset;
    int yoffset;

    // Constructor for creating a Thing instance.
    public Thing(int xpos, int ypos, int direction, int xoffset, int yoffset) {
        super(xpos, ypos, direction);
        this.xoffset = xoffset;
        this.yoffset = yoffset;
        loadAnimations(xoffset, yoffset);
    }

    // Load animations for the Mob, 2 frames each
    public void loadAnimations(int xoffset, int yoffset) {
        int frameWidth = 16;
        int frameHeight = 16;
        int walkAnimationFrames = 2;

        this.texture = new Texture(Gdx.files.internal("things.png"));

        Array<TextureRegion> characterDownAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterDownAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth) + xoffset, yoffset, frameWidth, frameHeight));
        }

        characterDownAnimation = new Animation<TextureRegion>(0.1f, characterDownAnimationFrames);


        Array<TextureRegion> characterLeftAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterLeftAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth) + xoffset, 16 + yoffset, frameWidth, frameHeight));
        }

        characterLeftAnimation = new Animation<TextureRegion>(0.1f, characterLeftAnimationFrames);

        Array<TextureRegion> characterRightAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterRightAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth) + xoffset, 32 + yoffset, frameWidth, frameHeight));
        }

        characterRightAnimation = new Animation<TextureRegion>(0.1f, characterRightAnimationFrames);

        Array<TextureRegion> characterUpAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterUpAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth) + xoffset, 48 + yoffset, frameWidth, frameHeight));
        }

        characterUpAnimation = new Animation<TextureRegion>(0.1f, characterUpAnimationFrames);
    }

    // Method to perform an action when the Thing is opened
    public void open() {
    }
}
