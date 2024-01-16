package de.tum.cit.ase.maze;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.Entities.Entity;

public class Maploader {

    private final MazeRunnerGame game;

    int mapType; //Default Map type

    int mapWidth;  // max Width of the map
    int mapHeight; // max Height of the map ( both flexible)
    int[][] map;

    Properties properties = new Properties();

    //Constructor
    public Maploader(MazeRunnerGame game) {
        this.game = game;
        this.mapType = 1; //TODO: Gets called every time I render a map, therfore Filechooser useless
        loadMapSize();
        map = new int[mapHeight][mapWidth];
    }

    //Reads the .properties file
    public void reader() {
        try (InputStream input = Gdx.files.internal("maps/level-" + mapType + ".properties").read()) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Fills out the Array
    public void createMap() {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String[] coordinates = entry.getKey().toString().split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int entityType = Integer.parseInt(entry.getValue().toString());

            map[x][y] = entityType;
        }
    }

    private void loadMapSize() {
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
            e.printStackTrace();
            // Handle exceptions accordingly
            mapWidth = 99; // default values if stuff goes wrong
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