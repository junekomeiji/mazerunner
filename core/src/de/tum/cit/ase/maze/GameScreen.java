package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.ase.maze.Entities.Entity;
import de.tum.cit.ase.maze.Entities.Mobs.*;
import de.tum.cit.ase.maze.Entities.Things.*;
import java.util.ArrayList;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    private Viewport viewport;
    private Viewport hudViewport;


    public Maploader maploader;

    private SpriteBatch batch;
    private SpriteBatch hudBatch;

    private final BitmapFont font;

    private float sinusInput = 0f;

    private float timeCount;

    private float elapsedTime = 0f;

    private boolean paused;
    private boolean openingChest;

    private Player player;

    private boolean isInvulnerable = false;
    private int invulnerabilityTime = 0;

    // Necessary for handling walking sounds
    private boolean walkingSoundDelay = true;
    private int walkingSoundDelayTime = 0;
    private int walkingSoundDelayTimeTicks = 20;

    // All sound effects used in this class
    private Sound damageGhostSound;
    private Sound damageTrapSound;
    private Sound ghostSound;
    private Sound swingSound;
    private Sound walkingSound;
    private Sound keySound;
    private Sound birdSound;
    private Sound chestSound;


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

    private int translatedx = 0;
    private int translatedy = 0;

    private boolean once = false;


    // All the textures for the gameScreen are here
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

    // Full heart Texture
    Texture fullHearthTexture = new Texture(Gdx.files.internal("objects.png"));
    TextureRegion fullHearthTextureRegion = new TextureRegion(fullHearthTexture, 64, 0, 16, 16);

    // Empty heart Texture
    Texture emptyHearthTexture = new Texture(Gdx.files.internal("objects.png"));
    TextureRegion emptyHearthTextureRegion = new TextureRegion(emptyHearthTexture, 128, 0, 16, 16);


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, Maploader maploader) {

        this.maploader = maploader;

        this.paused = false;
        this.openingChest = false;

        timeCount = 0;

        this.game = game;
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        font = game.getSkin().getFont("font");
        enemies = new ArrayList<Mob>();
        things = new ArrayList<Thing>();

        // Loads all the sounds that can be played in the game screen and sets them to an appropriate volume
        walkingSound = Gdx.audio.newSound(Gdx.files.internal("soundeffects/walking.mp3"));
        swingSound = Gdx.audio.newSound(Gdx.files.internal("soundeffects/swing.mp3"));
        ghostSound = Gdx.audio.newSound(Gdx.files.internal("soundeffects/ghost.mp3"));
        damageGhostSound = Gdx.audio.newSound(Gdx.files.internal("soundeffects/damageGhost.mp3"));
        damageTrapSound = Gdx.audio.newSound(Gdx.files.internal("soundeffects/damageTrap.mp3"));
        keySound = Gdx.audio.newSound(Gdx.files.internal("soundeffects/ding.mp3"));
        chestSound = Gdx.audio.newSound(Gdx.files.internal("soundeffects/chest.mp3"));



        //Spawns in entities
        for (int x = 0; x < maploader.getMapWidth(); x++) {
            for (int y = 0; y < maploader.getMapHeight(); y++) {
                if (maploader.getMap()[x][y] == 1) { // 1 is entry point
                    player = new Player(x * 64 + 64, y * 64, 0);
                    player.setDirection(1);
                }
                if (maploader.getMap()[x][y] == 3) {
                    things.add(new Spike(x * 64, y * 64, 0));
                }
                if (maploader.getMap()[x][y] == 4) {
                    enemies.add(new Ghost(x * 64, y * 64, 0));
                }
                if (maploader.getMap()[x][y] == 5) {
                    things.add(new Chest(x * 64, y * 64, 0));
                }
            }
        }

        camera = new OrthographicCamera();
        camera.zoom = 1.5f;

        hudCamera = new OrthographicCamera();
        hudCamera.zoom = 1f;

        // For changing window size without stretching everything
        viewport = new ScreenViewport(camera);
        hudViewport = new ScreenViewport(camera);



        //hud = new HUD(game.getSpriteBatch(), player, game.height, game.width);

        humanoid = new Humanoid(0, 0, 0);
        slime = new Slime(0, 0, 0);
        man = new Man(0, 0, 0);
        ghost = new Ghost(0, 0, 0);


        door = new Door(0, 0, 0);

        torch = new Torch(0, 0, 0);
        lever = new Lever(0, 0, 0);
        spike = new Spike(0, 0, 0);
        fireplace = new Fireplace(0, 0, 0);
        vase = new Vase(0, 0, 0);
        chest = new Chest(0, 0, 0);


        collided = false;
        // Get the font from the game's skin
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {

        int mapX = (int) (player.getX() / 64);
        int mapY = (int) (player.getY() / 64);

        if (!once) {
            camera.position.x = player.getX();
            camera.position.y = player.getY();
            once = true;
            translatedx = 0;
            translatedy = 0;
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsedTime += Gdx.graphics.getDeltaTime();

        // If p is pressed, either pause or unpause the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (isPaused()) {
                resumeGame();
            } else {
                pauseGame();
            }
        }

        // If the game is not paused, handle inputs (aside from ESC) and update timer
        if (!isPaused() && !openingChest) {
            handleInput();
            timeCount += delta;
        }

        if (player.getAnimation().isAnimationFinished(elapsedTime) & (player.isPickingUp() | player.isSlashing())) {
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
                        game.getSpriteBatch().draw(wallTextureRegion, x * 64, y * 64, 64, 64);
                        break;
                    case 1:
                        // Render Entry
                        game.getSpriteBatch().draw(entryPointTextureRegion, x * 64, y * 64, 64, 64);
                        break;
                    case 2:
                        // Render Exit on top of plain Grass
                        game.getSpriteBatch().draw(plainGrassTextureRegion, x * 64, y * 64, 64, 64);
                        game.getSpriteBatch().draw(exitTextureRegion, x * 64, y * 64, 64, 64);
                        break;
                    case 3, 5:
                        // Render plain grass below all Traps and Chests
                        game.getSpriteBatch().draw(plainGrassTextureRegion, x * 64, y * 64, 64, 64);
                        break;
                    case 4, 6:
                        // Render grass below all Enemies and randomly on the floor
                        game.getSpriteBatch().draw(grassTextureRegion, x * 64, y * 64, 64, 64);
                        break;
                    case 7:
                        // Render Lush Grass randomly on the floor
                        game.getSpriteBatch().draw(lushGrassTextureRegion, x * 64, y * 64, 64, 64);
                        break;
                    case 8:
                        // Render shadowless Walls
                        game.getSpriteBatch().draw(wallTextureShadowlessRegion, x * 64, y * 64, 64, 64);
                        break;
                    case 9:
                        // Render bushes on top of plain Grass
                        game.getSpriteBatch().draw(plainGrassTextureRegion, x * 64, y * 64, 64, 64);
                        game.getSpriteBatch().draw(bushTextureRegion, x * 64, y * 64, 64, 64);
                        break;

                }
            }
            //Checks if we are out of lives
            if (player.getLives() < 1) {
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


        // Updates invulnerability and sets it to false after given amount of time
        if (isInvulnerable) {
            invulnerabilityTime--;
            if (invulnerabilityTime <= 0) {
                isInvulnerable = false;
            }
        }


        for (Entity e : enemies) {
            //e.moveUp(2)

            // Checks if player is colliding with a ghost + checks for invulnerability
            if (e.getMapX() == player.getMapX() && e.getMapY() == player.getMapY() && !isInvulnerable) {

                player.setLives(player.getLives() - 1);
                damageGhostSound.setVolume(damageGhostSound.play(), 0.3f);
                isInvulnerable = true; // Temporary invulnerability granted
                invulnerabilityTime = 50;
            }
            game.getSpriteBatch().draw(ghostFrame, e.getX(), e.getY(), 64, 64);
        }

        for (Thing t : things) {
            if (t instanceof Chest) game.getSpriteBatch().draw(chestFrame, t.getX(), t.getY(), 64, 64);
            else if (t instanceof Spike) {

                // Checks if player is colliding with a trap + checks for invulnerability
                if (t.getMapX() == player.getMapX() && t.getMapY() == player.getMapY() && !isInvulnerable) {

                    player.setLives(player.getLives() - 1);
                    damageTrapSound.setVolume(damageTrapSound.play(), 0.3f);
                    isInvulnerable = true; // Temporary invulnerability granted
                    invulnerabilityTime = 50;
                }
                game.getSpriteBatch().draw(spikeFrame, t.getX(), t.getY(), 64, 64);
            }
        }

        // Player gets drawn below where he "actually is" to make up for empty space at the bottom of the skin
        game.getSpriteBatch().draw(playerFrame, player.getX(), player.getY() - 16, 64, 128);

        // Removes and plays sound effect whenever a ghost reaches 0 lives
        // Adds 10 score for every ghots killed
        enemies.removeIf(e -> {
            if (e.getHealth() == 0) {
                ghostSound.setVolume(ghostSound.play(), 0.1f);
                player.setScore(player.getScore() + 10);


                // Killing ghosts gives you one lost life back
                if(player.getLives() < 3) {
                    player.setLives(player.getLives() + 1);
                }

                return true;
            }

            return false;
        });
        game.getSpriteBatch().setProjectionMatrix(this.camera.combined);
        game.getSpriteBatch().end();

        // Convert screen coordinates to world coordinates using the camera's projection matrix
        // Necessary when resizing the game
        int centerX = Gdx.graphics.getWidth() / 2;
        int centerY = Gdx.graphics.getHeight();
        Vector3 worldCoordinates = new Vector3(centerX, centerY / 8, 0);
        camera.unproject(worldCoordinates);

        // Renders the HUD for the game at the top of the screen, unless the game is paused
        // If game is paused, render a pause menu

        if (!isPaused()) {

            hudBatch.setProjectionMatrix(hudCamera.combined); // Set the projection matrix
            hudBatch.begin();

            font.draw(hudBatch, "Score: " + player.getScore(), centerX + 150, centerY - 100);
            font.draw(hudBatch, String.format("Time : %.1f", timeCount), centerX - 300, centerY - 100);
            font.draw(hudBatch, "Key obtained: " + player.hasKey(), centerX + 350, centerY - 100);

            hudBatch.end();

            /*
            // Debug stuff
            font.draw(hudBatch, player.getX() + ", " + player.getY(), 500, 800);
            font.draw(hudBatch, player.getUpperrightcorner().x + ", " + player.getUpperrightcorner().y, 500, 750);

            font.draw(hudBatch, mapX + 1+ ", " + mapY + ", " + type(maploader.getMap()[mapX + 1][mapY]), 500, 700);
            font.draw(hudBatch, ((mapX * 64) - 64) + ", " + ((mapY * 64) - 64), 500, 650);
            font.draw(hudBatch, invulnerabilityTime + ", " + player.hasKey(), 500, 600);
             */

            // Draw the hearts at the calculated world coordinates + updates for when a live is lost
            //TODO Draw Key if obtained
            game.getSpriteBatch().begin();

            if (player.getLives() == 3) {
                game.getSpriteBatch().draw(fullHearthTextureRegion, worldCoordinates.x - 90, worldCoordinates.y, 64, 64);
                game.getSpriteBatch().draw(fullHearthTextureRegion, worldCoordinates.x, worldCoordinates.y, 64, 64);
                game.getSpriteBatch().draw(fullHearthTextureRegion, worldCoordinates.x + 90, worldCoordinates.y, 64, 64);
            }
            if (player.getLives() == 2) {
                game.getSpriteBatch().draw(fullHearthTextureRegion, worldCoordinates.x - 90, worldCoordinates.y, 64, 64);
                game.getSpriteBatch().draw(fullHearthTextureRegion, worldCoordinates.x, worldCoordinates.y, 64, 64);
                game.getSpriteBatch().draw(emptyHearthTextureRegion, worldCoordinates.x + 90, worldCoordinates.y, 64, 64);
            }
            if (player.getLives() == 1) {
                game.getSpriteBatch().draw(fullHearthTextureRegion, worldCoordinates.x - 90, worldCoordinates.y, 64, 64);
                game.getSpriteBatch().draw(emptyHearthTextureRegion, worldCoordinates.x, worldCoordinates.y, 64, 64);
                game.getSpriteBatch().draw(emptyHearthTextureRegion, worldCoordinates.x + 90, worldCoordinates.y, 64, 64);
            }

            game.getSpriteBatch().end();
        } else {
            // Handles the pause menu

            hudBatch.begin();

            font.draw(hudBatch, "GAME PAUSED", centerX - 100, centerY - 150);
            font.draw(hudBatch, "Press ESC to resume game", centerX - 200, centerY - 250);
            font.draw(hudBatch, "Press M to access main menu", centerX - 200, centerY - 350);
            hudBatch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                game.goToMenu();
            }
        }

        camera.update(); // Update the camera
        hudCamera.update();
    }

    //debug function!
    private String type(int t) {
        switch (t) {
            case 0 -> {
                return "wall";
            }
            case 5 -> {
                return "chest";
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

    // Handles inputs of the player
    private void handleInput() {

        // Plays walking sound at a reasonable pace
        if (walkingSoundDelay) {
            walkingSoundDelayTime--;
            if (walkingSoundDelayTime <= 0) {
                walkingSoundDelay = false;
            }
        }

        // Handles WASD inputs
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (!isObstacleCollision(player.getX(), player.getY() + 64, 64, player) &&
                    !isObstacleCollision(player.getX() + 64 - movementSpeed, player.getY() + 64, 64, player)) {
                player.moveUp(movementSpeed);
                if (!walkingSoundDelay) {
                    walkingSound.setVolume(walkingSound.play(), 0.75f);
                    walkingSoundDelay = true;
                    walkingSoundDelayTime = walkingSoundDelayTimeTicks;
                }
            }
            if (player.hasKey() & (isExitCollision(player.getX(), player.getY() + 64, 64) ||
                    isExitCollision(player.getX() + 64 - movementSpeed, player.getY() + 64, 64))) {
                game.goToVictory();
            } else player.moveUp(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (!isObstacleCollision(player.getX() - 8, player.getY(), 64, player) &&
                    !isObstacleCollision(player.getX() - 8, player.getY() + 64 - movementSpeed, 64, player)) {
                player.moveLeft(movementSpeed);
                if (!walkingSoundDelay) {
                    walkingSound.setVolume(walkingSound.play(), 0.75f);
                    walkingSoundDelay = true;
                    walkingSoundDelayTime = walkingSoundDelayTimeTicks;
                }
            }
            if (player.hasKey() & (isExitCollision(player.getX() - 64, player.getY(), 64) ||
                    isExitCollision(player.getX() - 64, player.getY() + 64 - movementSpeed, 64))) {
                game.goToVictory();
            } else player.moveLeft(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (!isObstacleCollision(player.getX(), player.getY() - 8, 64, player) &&
                    !isObstacleCollision(player.getX() + 64 - movementSpeed, player.getY() - 8, 64, player)) {
                player.moveDown(movementSpeed);
                if (!walkingSoundDelay) {
                    walkingSound.setVolume(walkingSound.play(), 0.75f);
                    walkingSoundDelay = true;
                    walkingSoundDelayTime = walkingSoundDelayTimeTicks;
                }
            }
            if (player.hasKey() & (isExitCollision(player.getX(), player.getY() - 1, 64) ||
                    isExitCollision(player.getX() + 64 - movementSpeed, player.getY() - 1, 64))) {
                game.goToVictory();
            } else player.moveDown(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (!isObstacleCollision(player.getX() + 64, player.getY(), 64, player) &&
                    !isObstacleCollision(player.getX() + 64, player.getY() + 64 - movementSpeed, 64, player)) {
                player.moveRight(movementSpeed);
                if (!walkingSoundDelay) {
                    walkingSound.setVolume(walkingSound.play(), 0.75f);
                    walkingSoundDelay = true;
                    walkingSoundDelayTime = walkingSoundDelayTimeTicks;
                }
            }
            if (player.hasKey() & (isExitCollision(player.getX() + 64, player.getY(), 64) ||
                    isExitCollision(player.getX() + 64, player.getY() + 64 - movementSpeed, 64))) {
                game.goToVictory();
            } else player.moveRight(0);
        }

        // Press E to pick up a key from a chest (only works when player is not in pick-Up animation
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if(chestProximity(player.getX(), player.getY()) && !player.isPickedUp()){
                keyCollected();
            } else if (!player.isPickedUp()) {
                elapsedTime = 0;
                player.setPickingUp(true);
                player.setPickedUp(true);
            } else {
                elapsedTime = 0;
                player.setPickingUp(false);
                player.setPickedUp(false);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            elapsedTime = 0;
            player.setSlashing(true);
            swingSound.setVolume(swingSound.play(), 0.25f);
            switch (player.getDirection()) {
                //down
                case 0 -> {
                    for (Mob e : enemies) {
                        if (e.getY() < player.getY() && player.getY() - e.getY() < 64 && Math.abs(player.getX() - e.getX()) < 64) {
                            e.setHealth(e.getHealth() - 1);
                        }
                    }
                }
                //right
                case 1 -> {
                    for (Mob e : enemies) {
                        if (e.getX() > player.getX() && e.getX() - player.getX() < 64 && Math.abs(player.getY() - e.getY()) < 64) {
                            e.setHealth(e.getHealth() - 1);
                        }
                    }
                }
                //up
                case 2 -> {
                    for (Mob e : enemies) {
                        if (e.getY() > player.getY() && e.getY() - player.getY() < 64 && Math.abs(player.getX() - e.getX()) < 64) {
                            e.setHealth(e.getHealth() - 1);
                        }
                    }
                }
                //left
                case 3 -> {
                    for (Mob e : enemies) {
                        if (e.getX() < player.getX() && player.getX() - e.getX() < 64 && Math.abs(player.getY() - e.getY()) < 64) {
                            e.setHealth(e.getHealth() - 1);
                        }
                    }
                }
                default -> {
                    return;
                }
            }
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
                | maploader.getMap()[mapX][mapY] == 2
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


    public boolean isCollision(float nextX, float nextY, int tileSize, Entity entity) {
        boolean val = false;
        int mapX = (int) (nextX / tileSize);
        int mapY = (int) (nextY / tileSize);
        if (mapX >= 0 && mapX < maploader.getMapWidth() && mapY >= 0 && mapY < maploader.getMapHeight()) {

            if (entity.getUpperleftcorner().x - movementSpeed < (mapX * tileSize) - 64 & entity.getLowerleftcorner().x - movementSpeed < (mapX * tileSize) - 64) {
                val = true;
            } else if (entity.getUpperrightcorner().x + movementSpeed > (mapX * tileSize) - 64 & entity.getLowerrightcorner().x + movementSpeed > (mapX * tileSize) - 64) {
                val = true;
            } else if (entity.getUpperrightcorner().y + movementSpeed > (mapY * tileSize) + 64 & entity.getUpperleftcorner().y + movementSpeed > (mapY * tileSize) + 64) {
                val = true;
            } else if (entity.getLowerleftcorner().y - movementSpeed > (mapY * tileSize) - 64 & entity.getLowerrightcorner().y - movementSpeed > (mapY * tileSize) - 64) {
                val = true;
            }
        }
        return val;
    }

    // Assuming you have a method to get the player's coordinates and a list of chest coordinates
    private boolean chestProximity(float x, float y) {

        int mapX = (int) (x / 64);
        int mapY = (int) (y / 64);

        //as always, down, right, up, left
        if (mapX >= 0 && mapX < maploader.getMapWidth() && mapY >= 0 && mapY < maploader.getMapHeight()) {
            if (player.getDirection() == 0 & maploader.getMap()[player.getMapX()][player.getMapY() - 1] == 5) {
                return true;
            } else if (player.getDirection() == 1 & maploader.getMap()[player.getMapX() + 1][player.getMapY()] == 5) {
                return true;
            } else if (player.getDirection() == 2 & maploader.getMap()[player.getMapX()][player.getMapY() + 1] == 5) {
                return true;
            } else if (player.getDirection() == 3 & maploader.getMap()[player.getMapX() - 1][player.getMapY()] == 5) {
                return true;
            }
        }

        return false;
    }

    // Logic for everything that happens after a key is collected
    // Chest is opened, game is "paused". After 3 seconds game is resumed
    public void keyCollected() {
        setOpeningChest(true);
        player.setPickingUp(true);
        player.setPickedUp(true);
        chestSound.setVolume(chestSound.play(), 0.5f);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            // After 1 second, chestOpening is done and the key is collected
            keySound.play();
            player.setKey(true);
            setOpeningChest(false);
            player.setPickingUp(false);
            player.setPickedUp(false);
        }, 1, TimeUnit.SECONDS);
        executor.shutdown();
    }


    private void pauseGame() {
        paused = true;
    }

    private void resumeGame() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setOpeningChest(boolean openingChest) {
        this.openingChest = openingChest;
    }

    public boolean isOpeningChest() {
        return paused;
    }

    // Responsible for making sure the game does not get scaled down when resizing
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.setToOrtho(false, width, height); // Update the camera projection to match the new viewport size
        hudCamera.setToOrtho(false, width, height); // Update the camera projection to match the new viewport size
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
}
