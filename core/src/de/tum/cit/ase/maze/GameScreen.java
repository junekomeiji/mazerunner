package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.ase.maze.Entities.Mobs.*;
import de.tum.cit.ase.maze.Entities.Things.*;


/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;

    private SpriteBatch batch;

    private final BitmapFont font;

    private float sinusInput = 0f;

    private float elapsedTime = 0f;

    private Player player;
    private Humanoid humanoid;
    private Slime slime;
    private Man man;

    private Door door;

    private Lever lever;
    private Torch torch;
    private Spike spike;
    private Chest chest;
    private Fireplace fireplace;
    private Vase vase;

    public Maploader mapLoader;

    //Maybe put this somewhere else later
    private Texture TextureRegion;

    //Wall Texture
    Texture wallTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion wallTextureRegion = new TextureRegion(wallTexture, 0, 0, 16, 16);

    //Entry Point Texture
    Texture entryPointTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion entryPointTextureRegion = new TextureRegion(entryPointTexture, 64, 16, 16, 16);

    //Exit Texture (currently door)
    Texture exitTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion exitTextureRegion = new TextureRegion(exitTexture, 0, 96, 16, 16);

    //Placeholder Texture (dynamic enemy later, currently red Wall)
    Texture placeholderTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion placeholderTextureRegion = new TextureRegion(placeholderTexture, 64, 0, 16, 16);

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {

        this.game = game;
        batch = new SpriteBatch();

        player = new Player(0, 0, 0);

        humanoid = new Humanoid(200,200,0);
        slime = new Slime(100, 100, 0);
        man = new Man(200, 100, 0);

        door = new Door(100, 200, 0);

        torch = new Torch(0, 0, 0);
        lever = new Lever(0, 0, 0);
        spike = new Spike(0, 0, 0);
        chest = new Chest(0, 0, 0);
        fireplace = new Fireplace(0, 0, 0);
        vase = new Vase(0, 0, 0);


        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");

        mapLoader = new Maploader(game);
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {

        //TODO: Hinders performance, need to fix later (same problem as map selector not working)
        mapLoader.reader();
        mapLoader.createMap();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += Gdx.graphics.getDeltaTime();

        //key press statements
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.moveUp();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.moveDown();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            if(!player.isPickedUp()) {
                elapsedTime = 0;
                player.setPickingUp(true);
                player.setPickedUp(true);
            } else {
                elapsedTime = 0;
                player.setPickingUp(false);
                player.setPickedUp(false);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
            elapsedTime = 0;
            player.setSlashing(true);
        }

        //may refactor later ^^

        if(player.getAnimation().isAnimationFinished(elapsedTime) & (player.isPickingUp() | player.isSlashing())){
            player.setPickingUp(false);
            player.setSlashing(false);
            elapsedTime = 0;
        }

        TextureRegion playerFrame = player.getAnimation().getKeyFrame(elapsedTime, true);
        TextureRegion humanoidFrame = humanoid.getAnimation().getKeyFrame(elapsedTime, true);
        TextureRegion slimeFrame = slime.getAnimation().getKeyFrame(elapsedTime, true);

        //For Maploader
        TextureRegion spikeFrame = spike.getAnimation().getKeyFrame(elapsedTime, true);
        TextureRegion chestFrame = chest.getAnimation().getKeyFrame(elapsedTime, true);

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        game.getSpriteBatch().begin();

        font.draw(game.getSpriteBatch(), "Lives: " + player.getLives(), 0, 400);

        game.getSpriteBatch().draw(playerFrame, player.getX(), player.getY(), 64, 128);
        game.getSpriteBatch().draw(humanoidFrame, humanoid.getX(), humanoid.getY(), 64, 64);
        game.getSpriteBatch().draw(slimeFrame, slime.getX(), slime.getY(), 64, 64);
        game.getSpriteBatch().draw(man.getAnimation().getKeyFrame(elapsedTime, true), man.getX(), man.getY(), 64, 64);
        game.getSpriteBatch().draw(door.getAnimation().getKeyFrame(elapsedTime, true), door.getX(), door.getY(), 64, 64);




        //Renders the Map
        System.out.println(mapLoader.getMapType());
        for (int x = 0; x < mapLoader.getMapWidth(); x++) {
            for (int y = 0; y < mapLoader.getMapHeight(); y++) {
                int entityType = mapLoader.getMap()[x][y]; // Retrieves the object type

                // Render Objects based on their type
                switch (entityType) {
                    case 0:
                        // Render Wall at position (x, y)
                        game.getSpriteBatch().draw(wallTextureRegion, x * 64 + 400, y * 64, 64, 64);
                        break;
                    case 1:
                        // Render Entry point at position (x, y)
                        game.getSpriteBatch().draw(entryPointTextureRegion, x * 64 + 400, y * 64, 64, 64);
                        break;
                    case 2:
                        // Render Exit at position (x, y)
                        game.getSpriteBatch().draw(exitTextureRegion, x * 64 + 400, y * 64, 64, 64);
                        break;
                    case 3:
                        // Render Trap at position (x, y)
                        game.getSpriteBatch().draw(spikeFrame, x * 64 + 400, y * 64, 64, 64);
                        break;
                    case 4:
                        // Render Enemy (currently Placeholder) at position (x, y)
                        game.getSpriteBatch().draw(placeholderTextureRegion, x * 64 + 400, y * 64, 64, 64);
                        break;
                    case 5:
                        // Render Chest (for obtaining key) at position (x, y)
                        game.getSpriteBatch().draw(chestFrame, x * 64 + 400, y * 64, 64, 64);
                        break;
                }
            }
        }
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
