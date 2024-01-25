package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;
    SpriteBatch batch;
    BitmapFont font;

    MazeRunnerGame game;

    Table table;

    public Maploader maploader;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game, Maploader maploader) {

        this.game = game;
        this.maploader = maploader;

        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Hello World from the Menu!", game.getSkin(), "title")).padBottom(80).row();

        // Create and add buttons to load maps and go to the game
        // Needs to be its own menu later
        TextButton g1 = new TextButton("Enter Map 1", game.getSkin());
        table.add(g1).width(300).row();
        g1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                maploader.setMapType(1);
                maploader.createMap();
                game.goToGame(); // Change to the game screen when button is pressed
            }
        });


        TextButton g2 = new TextButton("Enter Map 2", game.getSkin());
        table.add(g2).width(300).row();
        g2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                maploader.setMapType(2);
                maploader.createMap();
                game.goToGame();
            }
        });

        TextButton g3 = new TextButton("Enter Map 3", game.getSkin());
        table.add(g3).width(300).row();
        g3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                maploader.setMapType(2);
                maploader.createMap();
                game.goToGame();
            }
        });

        TextButton g4 = new TextButton("Enter Map 4", game.getSkin());
        table.add(g4).width(300).row();
        g4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                maploader.setMapType(2);
                maploader.createMap();
                game.goToGame();
            }
        });

        TextButton g5 = new TextButton("Enter Map 5", game.getSkin());
        table.add(g5).width(300).row();
        g5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                maploader.setMapType(2);
                maploader.createMap();
                game.goToGame();
            }
        });


        TextButton g6 = new TextButton("Go to Debug Screen", game.getSkin());
        table.add(g6).width(300).row();
        g6.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToDebug();
            }
        });

        batch = new SpriteBatch();
        font = game.getSkin().getFont("font");
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
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
