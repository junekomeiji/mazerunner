package de.tum.cit.ase.maze;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import com.badlogic.gdx.Gdx;

public class Maploader {

    private final MazeRunnerGame game;

    int mapType; // Map type (1-5 for each of the Maps in /assets)

    int mapWidth;  // max Width of the map
    int mapHeight; // max Height of the map
    int[][] map;

    Properties properties = new Properties();

    public Maploader(MazeRunnerGame game) {
        this.game = game;
        this.mapType = 1;
        map = new int[mapHeight][mapWidth];
    }


    //Fills out the Array
    public void createMap() {
        properties.clear(); // Necessary to delete old Map
        reader();
        setMapType(mapType);

        //Randomly fills out the Map with Grass or Lush Grass (Case 6 or 7)
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                // Generate a random value (0 or 1) to decide between grass and lush grass
                int randomValue = Math.random() < 0.6 ? 6 : 7; // For every 6 grass there is 4 lush grass

                map[x][y] = randomValue; // Assigns the random Values to our Map
            }
        }

        //Fills out the Array
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String[] coordinates = entry.getKey().toString().split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int entityType = Integer.parseInt(entry.getValue().toString());

            map[x][y] = entityType;
        }
    }

    //Reads the .properties file
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

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
        loadMapSize(); //Updates map size when Map changes
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