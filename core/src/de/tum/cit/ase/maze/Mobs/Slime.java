package de.tum.cit.ase.maze.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Character;

public class Slime extends Character{
    public Slime(int xpos, int ypos, int direction){
        super(xpos, ypos, direction);
    }

    @Override
    public void loadCharacterAnimation() {
        int frameWidth = 16;
        int frameHeight = 32;
        int walkAnimationFrames = 3;

        this.texture = new Texture(Gdx.files.internal("mobs.png"));

        Array<TextureRegion> characterDownAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterDownAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 4, 128, frameWidth, frameHeight));
        }

        characterDownAnimation = new Animation<TextureRegion>(0.1f, characterDownAnimationFrames);


        Array<TextureRegion> characterLeftAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterLeftAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 4, 160, frameWidth, frameHeight));
        }

        characterLeftAnimation = new Animation<TextureRegion>(0.1f, characterLeftAnimationFrames);

        Array<TextureRegion> characterRightAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterRightAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 4, 192, frameWidth, frameHeight));
        }

        characterRightAnimation = new Animation<TextureRegion>(0.1f, characterRightAnimationFrames);

        Array<TextureRegion> characterUpAnimationFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < walkAnimationFrames; col++) {
            characterUpAnimationFrames.add(new TextureRegion(this.texture, (col * frameWidth) + 4, 224, frameWidth, frameHeight));
        }

        characterUpAnimation = new Animation<TextureRegion>(0.1f, characterUpAnimationFrames);



    }
}
