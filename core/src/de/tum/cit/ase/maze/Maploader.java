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

    int mapType = 1; //Default Map type

    int mapWidth;  // max Width of the map
    int mapHeight; // max Height of the map
    int[][] map;

    Properties properties = new Properties();

    //Constructor
    public Maploader(MazeRunnerGame game) {
        this.game = game;
    }

    //Reads the .properties file
    public void reader() {
        try (InputStream input = Gdx.files.internal("maps/level-" + mapType + ".properties").read()) {
            properties.load(input);

            //Flexible Map Size
            mapWidth = Integer.parseInt(properties.getProperty("Width", "0"));
            mapHeight = Integer.parseInt(properties.getProperty("Height", "0"));

            map = new int[mapWidth][mapHeight];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Fills out the Array
    public void createMap() {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            if (!entry.getKey().equals("Width") && !entry.getKey().equals("Height")) {
                String[] coordinates = entry.getKey().toString().split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                int entityType = Integer.parseInt(entry.getValue().toString());

                map[x][y] = entityType;
            }
        }
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
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