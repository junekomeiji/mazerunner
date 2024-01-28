package de.tum.cit.ase.maze.Entities.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Entities.Entity;

public class Player extends Entity {

    private int score;
    private boolean key;

    private boolean pickingUp;
    private boolean pickedUp;
    private boolean slashing;

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

    public boolean isSlashing() {
        return slashing;
    }

    public void setSlashing(boolean slashing) {
        this.slashing = slashing;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean hasKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Player(int xpos, int ypos, int direction) {
        super(xpos, ypos, direction);
        this.lives = 3;
    }

    @Override
    public void loadAnimations() {
        int frameWidth = 16;
        int frameHeight = 32;
        int walkAnimationFrames = 4;

        this.texture = new Texture(Gdx.files.internal("character.png"));

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> downWalkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        //walk frames
        for (int col = 0; col < walkAnimationFrames; col++) {
            downWalkFrames.add(new TextureRegion(this.texture, col * frameWidth, 0, frameWidth, frameHeight));
        }

        characterDownAnimation = new Animation<TextureRegion>(0.1f, downWalkFrames);

        Array<TextureRegion> rightWalkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            rightWalkFrames.add(new TextureRegion(this.texture, col * frameWidth, 32, frameWidth, frameHeight));
        }

        characterRightAnimation = new Animation<TextureRegion>(0.1f, rightWalkFrames);

        Array<TextureRegion> upWalkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            upWalkFrames.add(new TextureRegion(this.texture, col * frameWidth, 64, frameWidth, frameHeight));
        }

        characterUpAnimation = new Animation<TextureRegion>(0.1f, upWalkFrames);

        Array<TextureRegion> leftWalkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            leftWalkFrames.add(new TextureRegion(this.texture, col * frameWidth, 96, frameWidth, frameHeight));
        }

        characterLeftAnimation = new Animation<TextureRegion>(0.1f, leftWalkFrames);


        //load picking up frames
        int pickingUpAnimationFrames = 3;

        Array<TextureRegion> characterPickingUpDownFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < pickingUpAnimationFrames; col++){
            characterPickingUpDownFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 80, 0, frameWidth, frameHeight));
        }

        characterPickingUpDown = new Animation<TextureRegion>(0.1f, characterPickingUpDownFrames);


        Array<TextureRegion> characterPickingUpRightFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < pickingUpAnimationFrames; col++){
            characterPickingUpRightFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 80, 32, frameWidth, frameHeight));
        }

        characterPickingUpRight = new Animation<TextureRegion>(0.1f, characterPickingUpRightFrames);


        Array<TextureRegion> characterPickingUpUpFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < pickingUpAnimationFrames; col++){
            characterPickingUpUpFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 80, 64, frameWidth, frameHeight));
        }

        characterPickingUpUp = new Animation<TextureRegion>(0.1f, characterPickingUpUpFrames);


        Array<TextureRegion> characterPickingUpLeftFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < pickingUpAnimationFrames; col++){
            characterPickingUpLeftFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 80, 96, frameWidth, frameHeight));
        }

        characterPickingUpLeft = new Animation<TextureRegion>(0.1f, characterPickingUpLeftFrames);

        //load picked up frames
        Array<TextureRegion> characterPickedUpDownFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            characterPickingUpDownFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 144, 0, frameWidth, frameHeight));
        }

        characterPickedUpDown = new Animation<TextureRegion>(0.1f, characterPickingUpDownFrames);


        Array<TextureRegion> characterPickedUpRightFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            characterPickedUpRightFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 144, 32, frameWidth, frameHeight));
        }

        characterPickedUpRight = new Animation<TextureRegion>(0.1f, characterPickedUpRightFrames);


        Array<TextureRegion> characterPickedUpUpFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            characterPickedUpUpFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 144, 64, frameWidth, frameHeight));
        }

        characterPickedUpUp = new Animation<TextureRegion>(0.1f, characterPickedUpUpFrames);


        Array<TextureRegion> characterPickedUpLeftFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            characterPickedUpLeftFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 144, 96, frameWidth, frameHeight));
        }

        characterPickedUpLeft = new Animation<TextureRegion>(0.1f, characterPickedUpLeftFrames);

        //slashing animations
        Array<TextureRegion> playerSlashDownFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            playerSlashDownFrames.add(new TextureRegion(this.texture, 2 * (col * frameWidth) + 8, 128, frameWidth, frameHeight));
        }

        playerSlashDown = new Animation<TextureRegion>(0.1f,playerSlashDownFrames);

        Array<TextureRegion> playerSlashUpFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            playerSlashUpFrames.add(new TextureRegion(this.texture, 2 * (col * frameWidth) + 8, 160, frameWidth, frameHeight));
        }

        playerSlashUp = new Animation<TextureRegion>(0.1f,playerSlashUpFrames);

        Array<TextureRegion> playerSlashRightFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            playerSlashRightFrames.add(new TextureRegion(this.texture, 2 * (col * frameWidth) + 8, 192, frameWidth, frameHeight));
        }

        playerSlashRight = new Animation<TextureRegion>(0.1f,playerSlashRightFrames);

        Array<TextureRegion> playerSlashLeftFrames = new Array<>(TextureRegion.class);
        for(int col = 0; col < walkAnimationFrames; col++){
            playerSlashLeftFrames.add(new TextureRegion(this.texture, 2 * (col * frameWidth) + 4, 224, frameWidth, frameHeight));
        }

        playerSlashLeft = new Animation<TextureRegion>(0.1f,playerSlashLeftFrames);


    }

    @Override
    public Animation<TextureRegion> getAnimation(){
        if(pickingUp){
            switch(this.direction){
                case 0 -> { return characterPickingUpDown; }
                case 1 -> { return characterPickingUpRight; }
                case 2 -> { return characterPickingUpUp; }
                case 3 -> { return characterPickingUpLeft; }
                default -> {return characterPickingUpDown; }
            }
        }

        if(slashing){
            switch(this.direction){
                case DOWN -> { return playerSlashDown; }
                case UP -> { return playerSlashUp; }
                case RIGHT -> { return playerSlashRight; }
                case LEFT -> { return playerSlashLeft; }
                default -> {return playerSlashUp; }
            }
        }

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

    public Animation<TextureRegion> getSlashingAnimation(){
            switch(this.direction){
                case DOWN -> { return playerSlashDown; }
                case UP -> { return playerSlashUp; }
                case RIGHT -> { return playerSlashRight; }
                case LEFT -> { return playerSlashLeft; }
                default -> {return playerSlashUp; }
            }
    }
}
