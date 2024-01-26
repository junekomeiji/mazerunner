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
import java.util.ArrayList;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    public Maploader maploader;

    private SpriteBatch batch;
    private SpriteBatch hudBatch;

    private final BitmapFont font;

    private float sinusInput = 0f;

    private float elapsedTime = 0f;

    private Player player;

    int movementSpeed = 8;

    private Humanoid humanoid;
    private Slime slime;
    private Man man;
    private Ghost ghost;

    private ArrayList<Mob> enemies;
    private ArrayList<Thing> things;

    private Door door;

    private Lever lever;
    private Torch torch;
    private Spike spike;
    private Chest chest;
    private Fireplace fireplace;
    private Vase vase;

    //Maybe put this somewhere else later
    private Texture TextureRegion;

    private boolean collided;

    private HUD hud;

    private int translatedx = 0;
    private int translatedy = 0;

    private boolean once = false;

    private boolean paused = false;

    //Wall Texture
    Texture wallTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion wallTextureRegion = new TextureRegion(wallTexture, 32, 0, 16, 16);

    //Entry Point Texture
    Texture entryPointTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion entryPointTextureRegion = new TextureRegion(entryPointTexture, 16, 112, 16, 16);

    //Exit Texture (currently door)
    Texture exitTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion exitTextureRegion = new TextureRegion(exitTexture, 0, 96, 16, 16);

    //Grass Texture
    Texture grassTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion grassTextureRegion = new TextureRegion(grassTexture, 0, 128, 16, 16);

    //Lush Grass Texture
    Texture lushGrassTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion lushGrassTextureRegion = new TextureRegion(lushGrassTexture, 16, 128, 16, 16);

    // Plain Grass Texture
    Texture plainGrassTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion plainGrassTextureRegion = new TextureRegion(plainGrassTexture, 48, 16, 16, 16);

    // Wall Filler Texture
    Texture wallTextureShadowless = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion wallTextureShadowlessRegion = new TextureRegion(wallTextureShadowless, 16, 0, 16, 16);

    // Bush Texture
    Texture bushTexture = new Texture(Gdx.files.internal("basictiles.png"));
    TextureRegion bushTextureRegion = new TextureRegion(bushTexture, 64, 144, 16, 16);


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, Maploader maploader) {

        this.maploader = maploader;

        this.game = game;
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        font = game.getSkin().getFont("font");
        enemies = new ArrayList<Mob>();
        things = new ArrayList<Thing>();


        //Spawns in entities
        for (int x = 0; x < maploader.getMapWidth(); x++) {
            for (int y = 0; y < maploader.getMapHeight(); y++) {
                if (maploader.getMap()[x][y] == 1) { // 1 is entry point
                    player = new Player(x * 64 + 64, y * 64, 0);
                    player.setDirection(1);
                }
                if(maploader.getMap()[x][y] == 3){
                    things.add(new Spike(x * 64, y * 64, 0));
                }
                if (maploader.getMap()[x][y] == 4){
                    enemies.add(new Ghost(x * 64, y * 64, 0));
                }
                if(maploader.getMap()[x][y] == 5){
                    things.add(new Chest(x * 64, y * 64, 0));
                }
            }
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 1.5f;

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false);
        hudCamera.zoom = 1f;

        //hud = new HUD(game.getSpriteBatch(), player, game.height, game.width);

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

        player.setLives(1);
        collided = false;
        // Get the font from the game's skin


    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {

        if(!once) {
            camera.position.x = player.getX();
            camera.position.y = player.getY();
            once = true;
            translatedx = 0;
            translatedy = 0;
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsedTime += Gdx.graphics.getDeltaTime();

        //if(!paused) handleInput();
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

        //Renders the Map
        for (int x = 0; x < maploader.getMapWidth(); x++) {
            for (int y = 0; y < maploader.getMapHeight(); y++) {
                int entityType = maploader.getMap()[x][y]; // Retrieves the object type
                // Render Objects based on their type
                switch (entityType) {
                    case 0:
                        // Render Wall
                        game.getSpriteBatch().draw(wallTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 1:
                        // Render Entry
                        game.getSpriteBatch().draw(entryPointTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 2:
                        // Render Exit on top of plain Grass
                        game.getSpriteBatch().draw(plainGrassTextureRegion, x * 64 , y * 64, 64, 64);
                        game.getSpriteBatch().draw(exitTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 3, 5:
                        // Render plain grass below all Traps and Chests
                        game.getSpriteBatch().draw(plainGrassTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 4, 6:
                        // Render grass below all Enemies and randomly on the floor
                        game.getSpriteBatch().draw(grassTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 7:
                        // Render Lush Grass randomly on the floor
                        game.getSpriteBatch().draw(lushGrassTextureRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 8:
                        // Render shadowless Walls
                        game.getSpriteBatch().draw(wallTextureShadowlessRegion, x * 64 , y * 64, 64, 64);
                        break;
                    case 9:
                        // Render bushes on top of plain Grass
                        game.getSpriteBatch().draw(plainGrassTextureRegion, x * 64 , y * 64, 64, 64);
                        game.getSpriteBatch().draw(bushTextureRegion, x * 64 , y * 64, 64, 64);
                        break;

                }
            }
            //Checks if we are out of lives
            if(player.getLives() < 1) {
                game.goToVDefeat();
            }
        }

        //for debug purposes
        camera.position.x = player.getX();
        camera.position.y = player.getY();

        /*
        int transspeed = 10;

        if((player.getX()) - translatedx + 256 > (Gdx.graphics.getWidth() * 0.75)){

            camera.translate(transspeed, 0);
            translatedx += transspeed;
            //this.paused = true;

        } //else this.paused = false;

        if((player.getX()) - translatedx - 256 < (Gdx.graphics.getWidth() * 0.25)){

            camera.translate(-transspeed, 0);
            translatedx -= transspeed;
            //this.paused = true;

        } //else this.paused = false;

        if((player.getY()) - translatedy + 256 > (Gdx.graphics.getHeight() * 0.75)){

            camera.translate(0, transspeed);
            translatedy += transspeed;
            //this.paused = true;

        } //else this.paused = false;

        if((player.getY()) - translatedy - 256 < (Gdx.graphics.getHeight() * 0.25)){

            camera.translate(0, -transspeed);
            translatedy -= transspeed;
            //this.paused = true;

        } //else this.paused = false;
        */


        for(Entity e : enemies){
            //e.moveUp(2);
            if(e.getX() == player.getX() && e.getY() == player.getY()) player.setLives(player.getLives() - 1);
            game.getSpriteBatch().draw(ghostFrame, e.getX(), e.getY(), 64, 64);
        }

        for(Thing t : things){
            if(t instanceof Chest) game.getSpriteBatch().draw(chestFrame, t.getX(), t.getY(), 64, 64);
            else if(t instanceof Spike){
                if(t.getX() == player.getX() && t.getY() == player.getY()) player.setLives(player.getLives() - 1);
                game.getSpriteBatch().draw(spikeFrame, t.getX(), t.getY(), 64, 64);
            }
        }

        // Player gets drawn below where he "actually is" to make up for empty space at the bottom of the skin
        game.getSpriteBatch().draw(playerFrame, player.getX(), player.getY() - 16, 64, 128);

        enemies.removeIf(e -> e.getHealth() == 0);

        game.getSpriteBatch().setProjectionMatrix(this.camera.combined);

        game.getSpriteBatch().end();

        hudBatch.begin();

        font.draw(hudBatch, "Lives: " + player.getLives(), 100, 800);
        font.draw(hudBatch, "Score: " + player.getScore(), 300, 800);
        font.draw(hudBatch, player.getX() + ", " + player.getY(), 500, 800);
        font.draw(hudBatch, player.getUpperrightcorner().x + ", " + player.getUpperrightcorner().y, 500, 750);

        int mapX = (int) (player.getX() / 64);
        int mapY = (int) (player.getY() / 64);



        font.draw(hudBatch, mapX + ", " + mapY + ", " + type(maploader.getMap()[mapX][mapY]), 500, 700);
        font.draw(hudBatch, ((mapX*64) - 64) + ", " + ((mapY*64) - 64) , 500, 650);

        font.draw(hudBatch, collided ? "collided" : "not collided", 100, 600);

        hudBatch.end();

        camera.update(); // Update the camera
        hudCamera.update();
    }

    //debug function!
    private String type(int t){
        switch(t){
            case 0 -> {
                return "wall";
            }
            case 6 -> {
                return "grass";
            }
            case 7 -> {
                return "lush grass";
            }
            default -> {
                return "something else";
            }
        }
    }


    private void handleInput(){

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (!isObstacleCollision(player.getX(), player.getY() + 64, 64, player) &&
                    !isObstacleCollision(player.getX() + 64 - movementSpeed, player.getY() + 64, 64, player)) {
                player.moveUp(movementSpeed);
            }
            if (isExitCollision(player.getX(), player.getY() + 64, 64) ||
                    isExitCollision(player.getX() + 64 - movementSpeed, player.getY() + 64, 64)) {
                game.goToVictory();
            }
            else player.moveUp(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (!isObstacleCollision(player.getX() - 8, player.getY(), 64, player) &&
                    !isObstacleCollision(player.getX() - 8, player.getY() + 64 - movementSpeed, 64, player)) {
                player.moveLeft(movementSpeed);
            }
            if (isExitCollision(player.getX() - 64, player.getY(), 64) ||
                    isExitCollision(player.getX() - 64, player.getY() + 64 - movementSpeed, 64)) {
                game.goToVictory();
            }
            else player.moveLeft(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (!isObstacleCollision(player.getX(), player.getY() - 8, 64, player) &&
                    !isObstacleCollision(player.getX() + 64 - movementSpeed, player.getY() - 8, 64, player)) {
                player.moveDown(movementSpeed);
            }
            if (isExitCollision(player.getX(), player.getY() - 1, 64) ||
                    isExitCollision(player.getX() + 64 - movementSpeed, player.getY() - 1, 64)) {
                game.goToVictory();
            }
            else player.moveDown(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (!isObstacleCollision(player.getX() + 64, player.getY(), 64, player) &&
                    !isObstacleCollision(player.getX() + 64, player.getY() + 64 - movementSpeed, 64, player)) {
                player.moveRight(movementSpeed);
            }
            if (isExitCollision(player.getX() + 64, player.getY(), 64) ||
                    isExitCollision(player.getX() + 64, player.getY() + 64 - movementSpeed, 64)) {
                game.goToVictory();
            }
            else player.moveRight(0);
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
            switch(player.getDirection()){
                //down
                case 0 -> { for(Mob e: enemies){
                        if(e.getY() < player.getY() && player.getY() - e.getY() < 64 && Math.abs(player.getX() - e.getX()) < 64){
                            e.setHealth(e.getHealth() - 1);
                        }
                    }
                }
                //right
                case 1 -> {for(Mob e: enemies){
                        if(e.getX() > player.getX() && e.getX() - player.getX() < 64 && Math.abs(player.getY() - e.getY()) < 64){
                            e.setHealth(e.getHealth() - 1);
                        }
                    }
                }
                //up
                case 2 -> {for(Mob e: enemies){
                        if(e.getY() > player.getY() && e.getY() - player.getY() < 64 && Math.abs(player.getX() - e.getX()) < 64){
                            e.setHealth(e.getHealth() - 1);
                        }
                    }
                }
                //left
                case 3 -> {for(Mob e: enemies) {
                        if (e.getX() < player.getX() && player.getX() - e.getX() < 64 && Math.abs(player.getY() - e.getY()) < 64) {
                           e.setHealth(e.getHealth() - 1);
                        }
                    }
                }
                default -> {return ; }
            }
        }


        //camera debug controls
        if(Gdx.input.isKeyPressed(Input.Keys.L)) {
            player.moveRight(movementSpeed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.J)){
            player.moveLeft(movementSpeed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)){
            player.moveUp(movementSpeed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K)){
            player.moveDown(movementSpeed);
        }

        collided = isObstacleCollision(player.getX() + movementSpeed, player.getY(), 64, player);

    }

    // Checks if we are about to run into an obstacle (wall, entry point, chest)
    private boolean isObstacleCollision(float nextX, float nextY, int tileSize, Entity entity) {
        int mapX = (int) (nextX / tileSize);
        int mapY = (int) (nextY / tileSize);

        // Handles it so we cant just run outside of the map infinitly and bypass the Maze
        if (mapX < 0 || mapX >= maploader.getMapWidth() || mapY < 0 || mapY >= maploader.getMapHeight()) {
            return true;
        }


        // Checks if the next position is a tile with collision
        return (isCollision(nextX, nextY, tileSize, entity) & (
                maploader.getMap()[mapX][mapY] == 0
                | maploader.getMap()[mapX][mapY] == 1)
                | maploader.getMap()[mapX][mapY] == 5
                | maploader.getMap()[mapX][mapY] == 8
                | maploader.getMap()[mapX][mapY] == 9);
    }



    // Checks if we are about to run into an exit
    private boolean isExitCollision(float nextX, float nextY, int tileSize) {
        int mapX = (int) (nextX / tileSize);
        int mapY = (int) (nextY / tileSize);

        // Checks for out of bounds to prevent crashes
        if (mapX >= 0 && mapX < maploader.getMapWidth() && mapY >= 0 && mapY < maploader.getMapHeight()) {
            // Checks if next position is an exit (case 2)
            return isCollision(nextX, nextY, tileSize, player) && (maploader.getMap()[mapX][mapY] == 2);
        } else {
            // In case we are out of bounds of the map
            return false;
        }
    }



    public boolean isCollision(float nextX, float nextY, int tileSize, Entity entity){
        boolean val = false;
        int mapX = (int) (nextX / tileSize);
        int mapY = (int) (nextY / tileSize);
        if (mapX >= 0 && mapX < maploader.getMapWidth() && mapY >= 0 && mapY < maploader.getMapHeight()) {

                if (entity.getUpperleftcorner().x - movementSpeed < (mapX*tileSize) - 64 & entity.getLowerleftcorner().x - movementSpeed < (mapX*tileSize) - 64 ) {
                    val = true;
                } else if (entity.getUpperrightcorner().x + movementSpeed > (mapX*tileSize) - 64 & entity.getLowerrightcorner().x + movementSpeed > (mapX*tileSize) - 64) {
                    val =  true;
                } else if (entity.getUpperrightcorner().y + movementSpeed > (mapY*tileSize) + 64 & entity.getUpperleftcorner().y + movementSpeed > (mapY*tileSize) + 64) {
                    val =  true;
                } else if (entity.getLowerleftcorner().y - movementSpeed > (mapY*tileSize) - 64 & entity.getLowerrightcorner().y - movementSpeed > (mapY*tileSize) - 64 ) {
                    val = true;
                }
        }
        return val;
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
