package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The DefeatScreen class is responsible for displaying the defeat screen of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the defeat screen.
 */
public class DefeatScreen implements Screen {

    private final Stage stage;

    private Viewport textViewport;

    private OrthographicCamera camera;
    private Viewport viewport;

    SpriteBatch batch;
    BitmapFont font;

    private final Texture backgroundTexture;

    MazeRunnerGame game;

    Table table;

    public DefeatScreen(MazeRunnerGame game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/defeat.png")); // Background texture

        table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label to inform the player they lost
        table.add(new Label("You Lost", game.getSkin(), "title")).padBottom(80).row();

        batch = new SpriteBatch();
        font = game.getSkin().getFont("font");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        batch.begin();

        batch.draw(backgroundTexture, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        font.draw(batch, "Press ESC to return to the main menu", 100, 100);

        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}