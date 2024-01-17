package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.Entities.Mobs.Player;
import de.tum.cit.ase.maze.MazeRunnerGame;

public class TiledTest implements Screen {

    private final Stage stage;

    private final OrthographicCamera camera;

    SpriteBatch batch;
    BitmapFont font;
    private float elapsedTime = 0;

    MazeRunnerGame game;

    Player p;

    public TiledTest(MazeRunnerGame game) {
        this.game = game;
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        p = new Player(0,0,0);

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        font = game.getSkin().getFont("font");

    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        elapsedTime += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        game.getSpriteBatch().begin();

        font.draw(game.getSpriteBatch(), "tiledtest screen!", 400, 400);

        game.getSpriteBatch().draw(p.getAnimation().getKeyFrame(elapsedTime, true), p.getX(), p.getY(), 64, 128);



        camera.update(); // Update the camera


        game.getSpriteBatch().end(); // Important to call this after drawing everything

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
