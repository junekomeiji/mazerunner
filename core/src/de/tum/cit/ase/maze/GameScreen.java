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

    private int translatedx = 0;
    private int translatedy = 0;

    private boolean once = false;

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

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 1.5f;



        humanoid = new Humanoid(0,0,0);
        slime = new Slime(0, 0, 0);
        man = new Man(0, 0, 0);
        ghost = new Ghost(0, 0, 0);

        door = new Door(0, 0, 0);

        torch = new Torch(0, 0, 0);
        lever = new Lever(0, 0, 0);
        spike = new Spike(0, 0, 0);
        chest = new Chest(0, 0, 0);
        fireplace = new Fireplace(0, 0, 0);
        vase = new Vase(0, 0, 0);

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");


    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {

        if(!once) {
            camera.position.x = player.getX();
            camera.position.y = player.getY();
            once = true;
        }

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

        //Draws the HUD for lives
        batch.begin();
        font.draw(batch, "Remaining Lives: " + player.getLives(), 1600, 1000);
        batch.end();


        game.getSpriteBatch().begin();

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
            //Checks if we are out of lives
            if(player.getLives() < 1) {
                game.goToVDefeat();
            }
        }

        /*
        System.out.println(Gdx.graphics.getWidth());

        int transspeed = 64;

        System.out.println(translatedx);

        if((player.getX()) - translatedx + 256 > (Gdx.graphics.getWidth() * 0.8)){

            camera.translate(transspeed, 0);
            translatedx += transspeed;

        }

        if((player.getX()) - translatedx - 256 < (Gdx.graphics.getWidth() * 0.2)){

            camera.translate(-transspeed, 0);
            translatedx -= transspeed;

        }

        if((player.getY()) - translatedy + 256 > (Gdx.graphics.getHeight() * 0.8)){

            camera.translate(0, transspeed);
            translatedy += transspeed;

        }

        if((player.getY()) - translatedy + 256 < (Gdx.graphics.getHeight() * 0.2)){

            camera.translate(0, -transspeed);
            translatedy -= transspeed;

        }

         */

        /*if((player.getX()) - translatedx > (Gdx.graphics.getWidth() * 0.9)){

            camera.translate(-transspeed, 0);
            translatedx -= transspeed;

        }*/

        /*if(Gdx.graphics.getWidth() + player.getX() - translatedx >= Gdx.graphics.getWidth() * 0.1){

            camera.translate(-transspeed, 0);
            translatedx -= transspeed;

        }

        if(Gdx.graphics.getHeight() - player.getY() + translatedy <= Gdx.graphics.getHeight() * 0.1){

            camera.translate(0, transspeed);
            translatedy += transspeed;

        }

        if(Gdx.graphics.getHeight() + player.getY() - translatedx >= Gdx.graphics.getHeight() * 0.1){

            camera.translate(0, -transspeed);
            translatedy -= transspeed;

        }*/


        //TODO: KEEP THIS, JUST COMMENTED OUT FOR TESTING
        /*




        if( - player.getY() < 0){
            System.out.println();
            camera.translate(0, 10);
        }

        if(Gdx.graphics.getHeight() - player.getY() > 0){
            camera.translate(0, -10);
        }
         */


        //System.out.println(player.getX() + ", " + player.getY());
        //System.out.println(Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight());

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
            if (!isWallCollision(player.getX(), player.getY() + movementSpeed, 64)) {
                player.moveUp(movementSpeed);
            }
            if (isExitCollision(player.getX(), player.getY() + movementSpeed, 64)) {
                game.goToVictory();
            }
            if (isHostileCollision(player.getX(), player.getY() + movementSpeed, 64)) {
                player.setLives(player.getLives() - 1);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (!isWallCollision(player.getX() - movementSpeed, player.getY(), 64)) {
                player.moveLeft(movementSpeed);
            }
            if (isExitCollision(player.getX(), player.getY() + movementSpeed, 64)) {
                game.goToVictory();
            }
            if (isHostileCollision(player.getX(), player.getY() + movementSpeed, 64)) {
                player.setLives(player.getLives() - 1);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (!isWallCollision(player.getX(), player.getY() - movementSpeed, 64)) {
                player.moveDown(movementSpeed);
            }
            if (isExitCollision(player.getX(), player.getY() - movementSpeed, 64)) {
                game.goToVictory();
            }
            if (isHostileCollision(player.getX(), player.getY() - movementSpeed, 64)) {
                player.setLives(player.getLives() - 1);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (!isWallCollision(player.getX() + movementSpeed, player.getY(), 64)) {
                player.moveRight(movementSpeed);
            }
            if (isExitCollision(player.getX(), player.getY() - movementSpeed, 64)) {
                game.goToVictory();
            }
            if (isHostileCollision(player.getX(), player.getY() - movementSpeed, 64)) {
                player.setLives(player.getLives() - 1);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
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
        if(Gdx.input.isKeyPressed(Input.Keys.L)) {
            camera.translate(50, 0);
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
    private boolean isWallCollision(float nextX, float nextY, int tileSize) {
        int mapX = (int) (nextX / tileSize);
        int mapY = (int) (nextY / tileSize);

        // Checks if we are still within the bounds of the map
        if (mapX >= 0 && mapX < maploader.getMapWidth() && mapY >= 0 && mapY < maploader.getMapHeight()) {
            // Check if the next position is a wall
            return maploader.getMap()[mapX][mapY] == 0;
        }

        // If we are out of bounds, consider it a wall collision
        return true;
    }

    // Checks if we are about to run into an exit
    private boolean isExitCollision(float nextX, float nextY, int tileSize) {
        int mapX = (int) (nextX / tileSize);
        int mapY = (int) (nextY / tileSize);

        // Checks if next position is an exit (case 2)
        return maploader.getMap()[mapX][mapY] == 2;
    }

    //Checks if we are about to run into a threat
    private boolean isHostileCollision(float nextX, float nextY, int tileSize) {
        int mapX = (int) (nextX / tileSize);
        int mapY = (int) (nextY / tileSize);

        int entityType = maploader.getMap()[mapX][mapY];

        // Check if the next position is a trap or ghost (case 3 for trap, case 4 for ghost),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
        return entityType == 3 || entityType == 4;
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
