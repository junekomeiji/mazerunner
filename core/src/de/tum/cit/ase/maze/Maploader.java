package de.tum.cit.ase.maze;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import com.badlogic.gdx.Gdx;
import de.tum.cit.ase.maze.Entities.Mobs.Player;

public class Maploader {

    private Player player;
    private final MazeRunnerGame game;

    Properties properties = new Properties();

    int mapType; // Number of the map we want to load (1,2,3...)

    int mapWidth;
    int mapHeight;
    int[][] map;

    public Maploader(MazeRunnerGame game, Player player) {
        this.game = game;
        this.mapType = 1;
        map = new int[mapHeight][mapWidth];
        this.player = player;
    }

    // Creates and changes the map to make it look better
    public void createMap() {
        properties.clear(); // Clears out the old map
        setMapType(mapType);

        // Randomly fills out empty tiles with Grass or Lush Grass (Case 6 or 7)
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                int randomValue = Math.random() < 0.6 ? 6 : 7; // For every 6 grass there is 4 lush grass
                map[x][y] = randomValue;
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

        // Replaces Walls that are flagged by isAboveWall with a shadowless Wall (case 8)
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (wallReplacement(x, y)) {
                    map[x][y] = 8;
                }
            }
        }

        // Replaces Walls that are flagged by isInnerWall with a bush (case 9)
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (isInnerWall(x, y)) {
                    map[x][y] = 9;
                }
            }
        }
    }

    public void loadMap() {
        mapHeight = 0; // Reset mapHeight
        mapWidth = 0; // Reset mapWidth

        // Loads up the map from the .properties file by extracting coordinates and type
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

    // Helper method to flag all walls to be replaced by shadowless wall
    private boolean wallReplacement(int x, int y) {
        if (map[x][y] == 0) {
            // Check if the wall is at the left or right edge of the map
            if (x == 0 || x == mapWidth - 1) {
                // Check if the wall is not at the bottom
                if (y != 0) {
                    // Check if the wall is at the top
                    if (y == mapHeight - 1) {
                        return true;
                    }
                    // Check if the wall is not at the far bottom
                    else if (y != mapHeight - 1) {
                        return true;
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
                    return true;
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
        loadMap(); // Updates map size when Map changes
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