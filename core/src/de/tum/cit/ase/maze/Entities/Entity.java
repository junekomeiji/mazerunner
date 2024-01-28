package de.tum.cit.ase.maze.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.*;

public abstract class Entity {
    protected Texture texture;
    protected SpriteBatch spriteBatch;

    protected int x;
    protected int y;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getMapX(){
        return (int) (this.getX() / 64);
    }

    public int getMapY(){
        return (int) (this.getY() / 64);
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

    public Point getUpperleftcorner() {
        return new Point(this.getX(), this.getY() + 64);
    }

    public Point getUpperrightcorner() {
        return new Point(this.getX() + 64, this.getY() + 64);
    }

    public Point getLowerleftcorner() {
        return new Point(this.getX(), this.getY());
    }

    public Point getLowerrightcorner() {
        return new Point(this.getX() + 64, this.getY());
    }


    public Animation<TextureRegion> getCharacterIdleAnimation() {
        return characterIdleAnimation;
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

    public Entity(int x, int ypos, int direction) {

        this.x = x;
        this.y = ypos;
        this.direction = direction;
        this.lives = 4;

        loadAnimations();
    }

    public void loadAnimations(){}

    public void moveUp() {
        setDirection(this.UP);
        this.y += 20;
    }

    public void moveDown() {
        setDirection(this.DOWN);
        this.y -= 20;
    }

    public void moveLeft() {
        setDirection(this.LEFT);
        this.x -= 20;
    }

    public void moveRight() {
        setDirection(this.RIGHT);
        this.x += 20;
    }

    public void moveUp(int movementSpeed) {
        setDirection(this.UP);
        this.y += movementSpeed;
    }

    public void moveDown(int movementSpeed) {
        setDirection(this.DOWN);
        this.y -= movementSpeed;
    }

    public void moveLeft(int movementSpeed) {
        setDirection(this.LEFT);
        this.x -= movementSpeed;
    }

    public void moveRight(int movementSpeed) {
        setDirection(this.RIGHT);
        this.x += movementSpeed;
    }

    public Animation<TextureRegion> getAnimation() {

        switch (this.direction) {
            case 0 -> {
                return characterDownAnimation;
            }
            case 1 -> {
                return characterRightAnimation;
            }
            case 2 -> {
                return characterUpAnimation;
            }
            case 3 -> {
                return characterLeftAnimation;
            }
            default -> {
                return characterUpAnimation;
            }
        }

    }
}