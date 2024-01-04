package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

public abstract class Character {
    protected Texture texture;
    protected SpriteBatch spriteBatch;

    protected int xpos;
    protected int ypos;
    protected int direction;

    protected int lives;
    protected int health;

    protected boolean walking;

    protected Animation<TextureRegion> characterIdleAnimation;
    protected Animation<TextureRegion> characterDownAnimation;
    protected Animation<TextureRegion> characterUpAnimation;
    protected Animation<TextureRegion> characterLeftAnimation;
    protected Animation<TextureRegion> characterRightAnimation;

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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }



    public Animation<TextureRegion> getCharacterIdleAnimation() {return characterIdleAnimation;}

    public Animation<TextureRegion> getCharacterDownAnimation() {return characterDownAnimation;}

    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

    public Character(int xpos, int ypos, int direction) {

        this.xpos = xpos;
        this.ypos = ypos;
        this.direction = 0;

        loadCharacterAnimation();
    }

    public void loadCharacterAnimation() {}

    public void moveUp(){
        setDirection(this.UP);
        this.ypos += 20;
    }

    public void moveDown(){
        setDirection(this.DOWN);
        this.ypos -= 20;
    }

    public void moveLeft(){
        setDirection(this.LEFT);
        this.xpos -= 20;
    }

    public void moveRight(){
        setDirection(this.RIGHT);
        this.xpos += 20;
    }

    }
