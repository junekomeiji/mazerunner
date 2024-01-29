package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;


/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {
    MazeRunnerGame game;

    Table table;

    private final Stage stage;
    SpriteBatch batch;
    BitmapFont font;

    private Sound clickSound;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final Texture backgroundTexture;


    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        // Loading background texture
        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/menu.png"));

        table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Soundeffect for clicking a button
        clickSound = Gdx.audio.newSound(Gdx.files.internal("soundeffects/click.mp3"));

        // Add a label as a title
        table.add(new Label("MAZERUNNER", game.getSkin(), "title")).padBottom(80).row();

        // Buttons to enter Map selector / resume game / debug screen
        TextButton g1 = new TextButton("Map Selector", game.getSkin());
        table.add(g1).width(300).row();
        g1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                game.goToMapSelector();
            }
        });

        //TODO: REMOVE
        TextButton g2 = new TextButton("Debug Screen", game.getSkin());
        table.add(g2).width(300).row();
        g2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                game.goToDebug();
            }
        });

        batch = new SpriteBatch();
        font = game.getSkin().getFont("font");
    }

    // Method for rendering all the graphics of the  menuScreen
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        batch.begin();

        // Draw the background texture, scaled to fit the current window size
        batch.draw(backgroundTexture, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        System.out.println(Gdx.graphics.getWidth());

        batch.end();

        // Update and draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    // Responsible for making sure the game does not get scaled down when resizing
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        // Dispose of the stage / background texture / music when screen is disposed
        stage.dispose();
        backgroundTexture.dispose();
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