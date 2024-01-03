package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import org.w3c.dom.Text;

public class Player {

    private Texture texture;
    private SpriteBatch spriteBatch;

    private int xpos;
    private int ypos;
    private int direction;

    private boolean pickingUp;
    private boolean pickedUp;

    private int lives;
    private int health;

    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    private Animation<TextureRegion> characterRightAnimation;

    private Animation<TextureRegion> characterPickingUpDown;
    private Animation<TextureRegion> characterPickingUpRight;
    private Animation<TextureRegion> characterPickingUpUp;
    private Animation<TextureRegion> characterPickingUpLeft;

    private Animation<TextureRegion> characterPickedUpDown;
    private Animation<TextureRegion> characterPickedUpRight;
    private Animation<TextureRegion> characterPickedUpUp;
    private Animation<TextureRegion> characterPickedUpLeft;

    private Animation<TextureRegion> playerSlashDown;
    private Animation<TextureRegion> playerSlashUp;
    private Animation<TextureRegion> playerSlashRight;
    private Animation<TextureRegion> playerSlashLeft;

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

    public boolean isPickingUp() {
        return pickingUp;
    }

    public void setPickingUp(boolean pickingUp) {
        this.pickingUp = pickingUp;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
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

    public Animation<TextureRegion> getCharacterPickingUpDown() {
        return characterPickingUpDown;
    }

    public Animation<TextureRegion> getCharacterPickingUpRight() {
        return characterPickingUpRight;
    }

    public Animation<TextureRegion> getCharacterPickingUpUp() {
        return characterPickingUpUp;
    }

    public Animation<TextureRegion> getCharacterPickingUpLeft() {
        return characterPickingUpLeft;
    }

    public Animation<TextureRegion> getCharacterPickedUpDown() {
        return characterPickedUpDown;
    }

    public Animation<TextureRegion> getCharacterPickedUpRight() {
        return characterPickedUpRight;
    }

    public Animation<TextureRegion> getCharacterPickedUpUp() {
        return characterPickedUpUp;
    }

    public Animation<TextureRegion> getCharacterPickedUpLeft() {
        return characterPickedUpLeft;
    }

    public Player(Texture texture, int xpos, int ypos) {

        this.texture = texture;
        this.xpos = xpos;
        this.ypos = ypos;
        this.direction = 0;

        loadCharacterAnimation();
    }

    public void loadCharacterAnimation() {
        Texture walkSheet = texture;

        int frameWidth = 16;
        int frameHeight = 32;
        int walkAnimationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> downWalkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        //walk frames
        for (int col = 0; col < walkAnimationFrames; col++) {
            downWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
        }

        characterDownAnimation = new Animation<TextureRegion>(0.1f, downWalkFrames);

        Array<TextureRegion> rightWalkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            rightWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 32, frameWidth, frameHeight));
        }

        characterRightAnimation = new Animation<TextureRegion>(0.1f, rightWalkFrames);

        Array<TextureRegion> upWalkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            upWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 64, frameWidth, frameHeight));
        }

        characterUpAnimation = new Animation<TextureRegion>(0.1f, upWalkFrames);

        Array<TextureRegion> leftWalkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            leftWalkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 96, frameWidth, frameHeight));
        }

        characterLeftAnimation = new Animation<TextureRegion>(0.1f, leftWalkFrames);


        //load picking up frames
        int pickingUpAnimationFrames = 3;

        Array<TextureRegion> characterPickingUpDownFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < pickingUpAnimationFrames; col++){
            characterPickingUpDownFrames.add(new TextureRegion(walkSheet, (col * frameWidth) + 80, 0, frameWidth, frameHeight));
        }

        characterPickingUpDown = new Animation<TextureRegion>(0.1f, characterPickingUpDownFrames);


        Array<TextureRegion> characterPickingUpRightFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < pickingUpAnimationFrames; col++){
            characterPickingUpRightFrames.add(new TextureRegion(walkSheet, (col * frameWidth) + 80, 32, frameWidth, frameHeight));
        }

        characterPickingUpRight = new Animation<TextureRegion>(0.1f, characterPickingUpRightFrames);


        Array<TextureRegion> characterPickingUpUpFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < pickingUpAnimationFrames; col++){
            characterPickingUpUpFrames.add(new TextureRegion(walkSheet, (col * frameWidth) + 80, 64, frameWidth, frameHeight));
        }

        characterPickingUpUp = new Animation<TextureRegion>(0.1f, characterPickingUpUpFrames);


        Array<TextureRegion> characterPickingUpLeftFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < pickingUpAnimationFrames; col++){
            characterPickingUpLeftFrames.add(new TextureRegion(walkSheet, (col * frameWidth) + 80, 96, frameWidth, frameHeight));
        }

        characterPickingUpLeft = new Animation<TextureRegion>(0.1f, characterPickingUpLeftFrames);

        //load picked up frames
        Array<TextureRegion> characterPickedUpDownFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            characterPickingUpDownFrames.add(new TextureRegion(walkSheet, (col * frameWidth) + 144, 0, frameWidth, frameHeight));
        }

        characterPickedUpDown = new Animation<TextureRegion>(0.1f, characterPickingUpDownFrames);


        Array<TextureRegion> characterPickedUpRightFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            characterPickedUpRightFrames.add(new TextureRegion(walkSheet, (col * frameWidth) + 144, 32, frameWidth, frameHeight));
        }

        characterPickedUpRight = new Animation<TextureRegion>(0.1f, characterPickedUpRightFrames);


        Array<TextureRegion> characterPickedUpUpFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            characterPickedUpUpFrames.add(new TextureRegion(walkSheet, (col * frameWidth) + 144, 64, frameWidth, frameHeight));
        }

        characterPickedUpUp = new Animation<TextureRegion>(0.1f, characterPickedUpUpFrames);


        Array<TextureRegion> characterPickedUpLeftFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            characterPickedUpLeftFrames.add(new TextureRegion(walkSheet, (col * frameWidth) + 144, 96, frameWidth, frameHeight));
        }

        characterPickedUpLeft = new Animation<TextureRegion>(0.1f, characterPickedUpLeftFrames);


    }

    public Animation<TextureRegion> getWalkAnimation(){

        if(!pickedUp){
            switch(this.direction){
                case 0 -> { return characterDownAnimation; }
                case 1 -> { return characterRightAnimation; }
                case 2 -> { return characterUpAnimation; }
                case 3 -> { return characterLeftAnimation; }
                default -> {return characterUpAnimation; }
            }
        } else{
            switch(this.direction){
                case 0 -> { return characterPickedUpDown; }
                case 1 -> { return characterPickedUpRight; }
                case 2 -> { return characterPickedUpUp; }
                case 3 -> { return characterPickedUpLeft; }
                default -> {return characterUpAnimation; }
            }
        }

    }

    public Animation<TextureRegion> getPickingUpAnimation(){
        switch(this.direction){
            case 0 -> { return characterPickingUpDown; }
            case 1 -> { return characterPickingUpRight; }
            case 2 -> { return characterPickingUpUp; }
            case 3 -> { return characterPickingUpLeft; }
            default -> {return characterPickingUpDown; }
        }
    }






}
