package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.audio.Sound;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



/**
 * The MapSelectorScreen class is responsible for displaying the map selector of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the map selector.
 */
public class MapSelectorScreen implements Screen {

    private final Stage stage;
    SpriteBatch batch;
    BitmapFont font;

    MazeRunnerGame game;
    Maploader maploader;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Sound clickSound;

    Table table;

    private final Texture backgroundTexture;


    /**
     * Constructor for MapSelectorScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     */
    public MapSelectorScreen(MazeRunnerGame game, Maploader maploader) {

        this.game = game;
        this.maploader = maploader;

        camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/menu.png")); // Background texture

        // Soundeffect for clicking a button
        clickSound = Gdx.audio.newSound(Gdx.files.internal("soundeffects/click.mp3"));

        // Lable as basic instructions
        table.add(new Label("Please select the map you want to play", game.getSkin(), "title")).padBottom(80).row();


        // Buttons to select and load maps
        TextButton g1 = new TextButton("Enter Map 1", game.getSkin());
        table.add(g1).width(300).row();
        g1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
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
                clickSound.play();
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
                clickSound.play();
                maploader.setMapType(3);
                maploader.createMap();
                game.goToGame();
            }
        });

        TextButton g4 = new TextButton("Enter Map 4", game.getSkin());
        table.add(g4).width(300).row();
        g4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                maploader.setMapType(4);
                maploader.createMap();
                game.goToGame();
            }
        });

        TextButton g5 = new TextButton("Enter Map 5", game.getSkin());
        table.add(g5).width(300).row();
        g5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                maploader.setMapType(5);
                maploader.createMap();
                game.goToGame();
            }
        });

        // Opens a Filechooser so you can select a map file
        TextButton g6 = new TextButton("Filechooser", game.getSkin());
        table.add(g6).width(300).row();
        g6.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();

                JFileChooser fileChooser = new JFileChooser();

                // Show the file chooser
                int returnValue = fileChooser.showOpenDialog(null);

                // Check if the user selected a file
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();

                    // Extract the level number using regular expression
                    String regex = "level-(\\d+)\\.properties";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(fileName);

                    // Check if the file name matches the pattern
                    if (matcher.matches()) {
                        // Extract the level number from the file name
                        int level = Integer.parseInt(matcher.group(1));

                        // Set the map type and create the map
                        maploader.setMapType(level);
                        maploader.createMap();
                        game.goToGame();
                    } else {
                        System.out.println("File could not be loaded");
                    }
                }
            }
        });


        //Back to menu button
        TextButton g7 = new TextButton("Back to Menu", game.getSkin());
        table.add(g7).width(300).row();
        g7.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                game.goToMenu();
            }
        });

        batch = new SpriteBatch();
        font = game.getSkin().getFont("font");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

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