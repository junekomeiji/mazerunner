package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Entities.Mobs.Player;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import com.badlogic.gdx.audio.Music;


/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private TiledTest tiledTest;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;
    private Music menuMusic;
    private Music victoryMusic1;
    private Music victoryMusicLooped;
    private Music defeatMusic;



    // UI Skin
    private Skin skin;

    private Maploader maploader;

    public int height;
    public int width;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser, int height, int width) {
        super();
        this.height = height;
        this.width = width;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin

        // Required for fog of war
        maploader = new Maploader(this, new Player(1, 1, 1));

        // Sets up all music for the game
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.wav"));
        menuMusic.setLooping(true);

        victoryMusic1 = Gdx.audio.newMusic(Gdx.files.internal("music/victory1.wav"));

        victoryMusicLooped = Gdx.audio.newMusic(Gdx.files.internal("music/victorylooped.wav"));
        victoryMusicLooped.setLooping(true);

        defeatMusic = Gdx.audio.newMusic(Gdx.files.internal("music/defeat.ogg"));
        defeatMusic.setLooping(true);

        // On completion of victoryMusic1, victoryMusicLooped is playd
        victoryMusic1.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                victoryMusicLooped.play();
            }
        });

        setScreen(new MenuScreen(this)); // Set the initial screen
        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        // Stops old Music
        victoryMusic1.stop();
        victoryMusicLooped.stop();
        defeatMusic.stop();

        menuMusic.setVolume(0.75f); // Sets volume of the music for menu (and by extension map selector)
        menuMusic.play();
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        menuMusic.setVolume(0.25f); // Sets volume of the music for gameplay
        this.setScreen(new GameScreen(this, maploader)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    public void goToMapSelector() {
        this.setScreen(new MapSelectorScreen(this, maploader)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    public void goToDebug(){
        this.setScreen(new DebugScreen(this));
        if(menuScreen != null){
            menuScreen.dispose();
            menuScreen = null;
        }
    }

    public void goToTiledTest(){
        this.setScreen(new TiledTest(this));
        if(menuScreen != null){
            menuScreen.dispose();
            menuScreen = null;
        }
    }

    public void goToVictory(){
        menuMusic.stop();
        victoryMusic1.setVolume(0.75f);
        victoryMusicLooped.setVolume(0.75f);
        victoryMusic1.play();
        this.setScreen(new VictoryScreen(this));
        if(menuScreen != null){
            menuScreen.dispose();
            menuScreen = null;
        }
    }

    public void goToVDefeat(){
        menuMusic.stop();
        defeatMusic.play();
        this.setScreen(new DefeatScreen(this));
        if(menuScreen != null){
            menuScreen.dispose();
            menuScreen = null;
        }
    }

    /**
     * Loads the character animation from the character.png file.
     */

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void setMusicVolume(float volume) {
        menuMusic.setVolume(volume);
    }

}
