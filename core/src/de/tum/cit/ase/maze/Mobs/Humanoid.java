package de.tum.cit.ase.maze.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Character;

public class Humanoid extends Character {
    public Humanoid(int xpos, int ypos, int direction){
        super(xpos, ypos, direction);
        loadCharacterAnimation();
    }



    @Override
    public void loadCharacterAnimation() {
        int frameWidth = 16;
        int frameHeight = 16;
        int walkAnimationFrames = 3;

        this.texture = new Texture(Gdx.files.internal("mobs.png"));

        Array<TextureRegion> characterDownAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterDownAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth), 0, frameWidth, frameHeight));
        }

        characterDownAnimation = new Animation<TextureRegion>(0.1f, characterDownAnimationFrames);


        Array<TextureRegion> characterLeftAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterLeftAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth), 32, frameWidth, frameHeight));
        }

        characterLeftAnimation = new Animation<TextureRegion>(0.1f, characterLeftAnimationFrames);

        Array<TextureRegion> characterRightAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterRightAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth), 64, frameWidth, frameHeight));
        }

        characterRightAnimation = new Animation<TextureRegion>(0.1f, characterRightAnimationFrames);

        Array<TextureRegion> characterUpAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterUpAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth), 96, frameWidth, frameHeight));
        }

        characterUpAnimation = new Animation<TextureRegion>(0.1f, characterUpAnimationFrames);



    }

    @Override
    public Animation<TextureRegion> getWalkAnimation() {

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
