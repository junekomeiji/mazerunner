package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;

    private final BitmapFont font;

    private float sinusInput = 0f;

    private Player player;
    private Animation<TextureRegion> playerAnimation;
    private float elapsedTime = 0f;


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {

        this.game = game;

        player = new Player(new Texture(Gdx.files.internal("character.png")), 0, 0);
        player.setDirection(player.UP);
        playerAnimation = player.getWalkAnimation();

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += Gdx.graphics.getDeltaTime();


        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player.setYpos(player.getYpos() + 20);
            player.setDirection(player.UP);
            playerAnimation = player.getWalkAnimation();

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            player.setXpos(player.getXpos() - 20);
            player.setDirection(player.LEFT);
            playerAnimation = player.getWalkAnimation();

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player.setYpos(player.getYpos() - 20);
            player.setDirection(player.DOWN);
            playerAnimation = player.getWalkAnimation();

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player.setXpos(player.getXpos() + 20);
            player.setDirection(player.RIGHT);
            playerAnimation = player.getWalkAnimation();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            if(!player.isPickedUp()) {
                elapsedTime = 0;
                player.setPickingUp(true);
                player.setPickedUp(true);
                playerAnimation = player.getPickingUpAnimation();

            } else {
                elapsedTime = 0;
                player.setPickingUp(false);
                player.setPickedUp(false);
                playerAnimation = player.getWalkAnimation();
            }

        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
            playerAnimation = player.getSlashingAnimation();
        }

        if(playerAnimation.isAnimationFinished(elapsedTime) && player.isPickingUp()){
            player.setPickingUp(false);
            elapsedTime = 0;
            playerAnimation = player.getWalkAnimation();
        }


        game.getSpriteBatch().begin();
        player.loadCharacterAnimation();

        font.draw(game.getSpriteBatch(), "test", 0, 0);
        TextureRegion currentFrame = playerAnimation.getKeyFrame(elapsedTime, true);

        game.getSpriteBatch().draw(currentFrame, player.getXpos(), player.getYpos(), 64, 128);

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        camera.update(); // Update the camera

        game.getSpriteBatch().end(); // Important to call this after drawing everything
    }



    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    // Additional methods and logic can be added as needed for the game screen
}
