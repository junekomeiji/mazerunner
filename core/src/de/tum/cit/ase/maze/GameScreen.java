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
import de.tum.cit.ase.maze.Entities.Entity;
import de.tum.cit.ase.maze.Entities.Mobs.*;
import de.tum.cit.ase.maze.Entities.Things.*;


/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private OrthographicCamera camera;

    public Maploader maploader;

    private SpriteBatch batch;

    private final BitmapFont font;

    private float sinusInput = 0f;

    private float elapsedTime = 0f;

    private Player player;
    private Humanoid humanoid;
    private Slime slime;
    private Man man;
    private Ghost ghost;

    private Door door;

    private Lever lever;
    private Torch torch;
    private Spike spike;
    private Chest chest;
    private Fireplace fireplace;
    private Vase vase;

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

    //Placeholder Texture (not currently used)
    Texture placeholderTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion placeholderTextureRegion = new TextureRegion(placeholderTexture, 64, 0, 16, 16);

    //Grass Texture
    Texture grassTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion grassTextureRegion = new TextureRegion(grassTexture, 0, 128, 16, 16);

    //Lush Grass Texture
    Texture lushGrassTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion lushGrassTextureRegion = new TextureRegion(lushGrassTexture, 16, 128, 16, 16);

    // Plain Grass Texture
    Texture plainGrassTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion plainGrassTextureRegion = new TextureRegion(plainGrassTexture, 48, 16, 16, 16);


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, Maploader maploader) {

        this.maploader = maploader;

        this.game = game;
        batch = new SpriteBatch();

        // Sets player spawn point to the coordinates of the entry point (case 1)
        for (int x = 0; x < maploader.getMapWidth(); x++) {
            for (int y = 0; y < maploader.getMapHeight(); y++) {
                if (maploader.getMap()[x][y] == 1) { // 1 is entry point
                    player = new Player(x * 64, y * 64, 0);
                    break;
                }
            }
        }

        humanoid = new Humanoid(200,200,0);
        slime = new Slime(100, 100, 0);
        man = new Man(200, 100, 0);
        ghost = new Ghost(0, 0, 0);

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
        camera.zoom = 1.5f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");


    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsedTime += Gdx.graphics.getDeltaTime();

        handleInput();

        if(player.getAnimation().isAnimationFinished(elapsedTime) & (player.isPickingUp() | player.isSlashing())){
            player.setPickingUp(false);
            player.setSlashing(false);
            elapsedTime = 0;
        }

        TextureRegion playerFrame = player.getAnimation().getKeyFrame(elapsedTime, true);
        TextureRegion ghostFrame = ghost.getAnimation().getKeyFrame(elapsedTime, true);

        TextureRegion spikeFrame = spike.getAnimation().getKeyFrame(elapsedTime, true);
        TextureRegion chestFrame = chest.getAnimation().getKeyFrame(elapsedTime, true);

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        game.getSpriteBatch().begin();

        font.draw(game.getSpriteBatch(), "Lives: " + player.getLives(), 0, 400);

        //Renders the Map
        for (int x = 0; x < maploader.getMapWidth(); x++) {
            for (int y = 0; y < maploader.getMapHeight(); y++) {
                int entityType = maploader.getMap()[x][y]; // Retrieves the object type
                // Render Objects based on their type
                switch (entityType) {
                    case 0:
                        // Render Wall at position (x, y)
                        game.getSpriteBatch().draw(wallTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 1:
                        // Render Entry point at position (x, y)
                        game.getSpriteBatch().draw(entryPointTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 2:
                        // Render Exit at position (x, y)
                        game.getSpriteBatch().draw(exitTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 3:
                        // Render Trap at position (x, y) on top of plain Grass
                        game.getSpriteBatch().draw(plainGrassTextureRegion, x * 64 , y * 64, 64, 64);
                        game.getSpriteBatch().draw(spikeFrame, x * 64 , y * 64, 64, 64);
                        break;
                    case 4:
                        // Render Enemy (currently static Ghost) at position (x, y)
                        // Grass temporary, ideally also random later
                        game.getSpriteBatch().draw(grassTextureRegion, x * 64 , y * 64, 64, 64);
                        game.getSpriteBatch().draw(ghostFrame, x * 64, y * 64, 64, 64);
                        break;
                    case 5:
                        // Render Chest (for obtaining key) at position (x, y) on top of plain Grass
                        game.getSpriteBatch().draw(plainGrassTextureRegion, x * 64 , y * 64, 64, 64);
                        game.getSpriteBatch().draw(chestFrame, x * 64 , y * 64, 64, 64);
                        break;
                    case 6:
                        // Render Grass at position (x, y) (filler)
                        game.getSpriteBatch().draw(grassTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 7:
                        // Render Lush Grass at position (x, y) (filler)
                        game.getSpriteBatch().draw(lushGrassTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                }
            }
        }

        //TODO: KEEP THIS, JUST COMMENTED OUT FOR TESTING
        /*
        if(Gdx.graphics.getWidth() - player.getX() < 0){
            camera.translate(10, 0);
        }

        if(Gdx.graphics.getWidth() - player.getX() > 0){
            camera.translate(-10, 0);
        }

        if( - player.getY() < 0){
            System.out.println();
            camera.translate(0, 10);
        }

        if(Gdx.graphics.getHeight() - player.getY() > 0){
            camera.translate(0, -10);
        }
         */


        System.out.println(player.getX() + ", " + player.getY());
        System.out.println(Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight());

        game.getSpriteBatch().draw(playerFrame, player.getX(), player.getY(), 64, 128);
        game.getSpriteBatch().setProjectionMatrix(this.camera.combined);

        camera.update(); // Update the camera
        game.getSpriteBatch().end(); // Important to call this after drawing everything
    }

    private void handleInput(){

        // Redone for wall collision check
        int nextX = player.getX();
        int nextY = player.getY();

        //TODO: Currently messes up Wall check if not 64
        int movementSpeed = 64; // Speed at which the player moves

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if(!isWallCollision(player.getX(), player.getY() + movementSpeed)) player.moveUp(movementSpeed);
            player.moveUp(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if(!isWallCollision(player.getX() - movementSpeed, player.getY())) player.moveLeft(movementSpeed);
            player.moveLeft(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if(!isWallCollision(player.getX(), player.getY() - movementSpeed)) player.moveDown(movementSpeed);
            player.moveDown(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if(!isWallCollision(player.getX() + movementSpeed, player.getY())) player.moveRight(movementSpeed);
            player.moveRight(0);
        }

        // Check for collisions with walls
        /*if (!isWallCollision(nextX, nextY)) {
            // Convert so we can set position
            player.setX((int)nextX);
            player.setY((int)nextY);
        }*/

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
            elapsedTime = 0;
            player.setSlashing(true);
        }

        //camera debug controls
        if(Gdx.input.isKeyPressed(Input.Keys.L)){
            camera.translate(50,0);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.J)){
            camera.translate(-50,0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)){
            camera.translate(0,50);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K)){
            camera.translate(0,-50);
        }
    }

    // Checks if we are about to run into a  wall
    private boolean isWallCollision(float nextX, float nextY) {
        int mapX = (int) (nextX / 64); // Convert to map coordinates
        int mapY = (int) (nextY / 64);

        // Check if the next position is a wall
        return maploader.getMap()[mapX][mapY] == 0; // 0 represents a wall
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
