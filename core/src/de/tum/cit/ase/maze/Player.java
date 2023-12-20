package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Player {

    private Texture texture;
    private SpriteBatch spriteBatch;

    private int xpos;
    private int ypos;
    private int direction;

    private int lives;
    private int health;

    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    private Animation<TextureRegion> characterRightAnimation;

    public final int DOWN = 0;
    public final int RIGHT = 1;
    public final int UP = 2;
    public final int LEFT = 3;



    public int getXpos() {
        return xpos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

    public Player(Texture texture, int xpos, int ypos) {
        this.texture = texture;
        this.xpos = xpos;
        this.ypos = ypos;
        this.direction = 0;
    }

    public void loadCharacterAnimation() {
        Texture walkSheet = texture;

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> downWalkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            downWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
        }

        characterDownAnimation = new Animation<TextureRegion>(0.1f, downWalkFrames);

        Array<TextureRegion> rightWalkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < animationFrames; col++) {
            rightWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 32, frameWidth, frameHeight));
        }

        characterRightAnimation = new Animation<TextureRegion>(0.1f, rightWalkFrames);

        Array<TextureRegion> upWalkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < animationFrames; col++) {
            upWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 64, frameWidth, frameHeight));
        }

        characterUpAnimation = new Animation<TextureRegion>(0.1f, upWalkFrames);

        Array<TextureRegion> leftWalkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < animationFrames; col++) {
            leftWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 96, frameWidth, frameHeight));
        }

        characterLeftAnimation = new Animation<TextureRegion>(0.1f, leftWalkFrames);

    }

    public Animation<TextureRegion> getWalkAnimation(int direction){
        loadCharacterAnimation();
        switch(direction){
            case 0 -> { return characterDownAnimation; }
            case 1 -> { return characterRightAnimation; }
            case 2 -> { return characterUpAnimation; }
            case 3 -> { return characterLeftAnimation; }
            default -> {return characterUpAnimation; }
        }
    }




}
