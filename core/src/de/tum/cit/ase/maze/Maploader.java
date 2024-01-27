package de.tum.cit.ase.maze;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import com.badlogic.gdx.Gdx;
import de.tum.cit.ase.maze.Entities.Mobs.Player;

public class Maploader {

    private final MazeRunnerGame game;

    int mapType; // Map type (1-5 for each of the Maps in /assets)

    int mapWidth;  // max Width of the map
    int mapHeight; // max Height of the map
    int[][] map;

    private Player player;

    Properties properties = new Properties();

    public Maploader(MazeRunnerGame game, Player player) {
        this.game = game;
        this.mapType = 1;
        map = new int[mapHeight][mapWidth];
        this.player = player;
    }


    // Fills out the Array from the file and adds a bit of extras to make it look good
    public void createMap() {
        properties.clear(); // Necessary to delete old Map
        reader();
        setMapType(mapType);

        // Randomly fills out the Map with Grass or Lush Grass (Case 6 or 7)
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                // Generate a random value (0 or 1) to decide between grass and lush grass
                int randomValue = Math.random() < 0.6 ? 6 : 7; // For every 6 grass there is 4 lush grass

                map[x][y] = randomValue; // Assigns the random Values to our Map
            }
        }

        // Fills out the Array
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String[] coordinates = entry.getKey().toString().split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int entityType = Integer.parseInt(entry.getValue().toString());

            map[x][y] = entityType;
        }

        // Replaces Walls that are flagged by isAboveWall with a clean Wall (case 8)
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (isOuterWall(x, y)) {
                    map[x][y] = 8; // 8 is the filler wall (shadowless)
                }
            }
        }

        // Replaces Walls that are flagged by isInnerWall with a bush (case 9)
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (isInnerWall(x, y)) {
                    map[x][y] = 9; // 9 is the bush
                }
            }
        }
    }

    // Reads the .properties file
    public void reader() {
        try (InputStream input = Gdx.files.internal("maps/level-" + mapType + ".properties").read()) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadMapSize() {
        mapHeight = 0; // Reset mapHeight
        mapWidth = 0; // Reset mapHeight

        try (InputStream input = Gdx.files.internal("maps/level-" + mapType + ".properties").read()) {
            properties.load(input);
            for (Object key : properties.keySet()) {
                String[] coordinates = key.toString().split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);

                // Updates mapWidth and mapHeight based on the highest coordinates
                mapWidth = Math.max(mapWidth, x + 1);
                mapHeight = Math.max(mapHeight, y + 1);
            }
        } catch (IOException e) {
            // default values if stuff goes wrong
            e.printStackTrace();
            mapWidth = 99;
            mapHeight = 99;
        }
    }

    // Helper method to flag all coordinates that are on the far left/right edges of the Map, except for the most bottom one
    //TODO: Maybe use sth like this later for a fog of war?
    private boolean isOuterWall(int x, int y) {
        if (map[x][y] == 0) {
            // Check if the wall is at the left or right edge of the map
            if (x == 0 || x == mapWidth - 1) {
                // Check if the wall is not at the bottom
                if (y != 0) {
                    // Check if the wall is at the top
                    if (y == mapHeight - 1) {
                        return true; // Wall flagged for replacement
                    }
                    // Check if the wall is not at the far bottom
                    else if (y != mapHeight - 1) {
                        return true; // Wall flagged for replacement
                    }
                }
            }
        }

        return false; // Wall can stay
    }

    // Helper method to flag all coordinates that are inner Walls
    private boolean isInnerWall(int x, int y) {
        if (map[x][y] == 0) {
            // Check if the wall is not at the left or right edge of the map
            if (x > 0 && x < mapWidth - 1) {
                // Check if the wall is not at the top or bottom edge of the map
                if (y > 0 && y < mapHeight - 1) {
                    return true; // Wall flagged for replacement
                }
            }
        }
        return false;
    }

    //TODO: FINISH
    public boolean fogOfWar(int radius) {
        // Iterate through all tiles in the map
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                // Calculate the distance between the current tile and the player
                int distance = Math.abs(player.getX() - x) + Math.abs(player.getY() - y);
                // Check if the distance is greater than the radius
                if (distance > radius) {
                    // Replace the tile with plain grass (case 10)
                    return true; // Set the flag to true since a tile was updated
                }
            }
        }
        return false;
    }




    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
        loadMapSize(); // Updates map size when Map changes
        map = new int[mapHeight][mapWidth];
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }
}