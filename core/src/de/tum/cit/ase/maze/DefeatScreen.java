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
    MazeRunnerGame game;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final Stage stage;
    Table table;

    SpriteBatch batch;
    BitmapFont font;
    private final Texture backgroundTexture;


    public DefeatScreen(MazeRunnerGame game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view
        viewport = new ScreenViewport(camera); // Create a viewport with the camera

        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements


        // Table for Layout, filling stage, adding table to stage
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Add a label to inform the player they lost
        table.add(new Label("You Lost", game.getSkin(), "title")).padBottom(80).row();

        batch = new SpriteBatch();
        font = game.getSkin().getFont("font");

        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/defeat.png")); // Background texture
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        batch.begin();

        // Informative text for player
        batch.draw(backgroundTexture, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        font.draw(batch, "Press ESC to return to the main menu", 100, 100);

        batch.end();

        // Draw and update the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

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