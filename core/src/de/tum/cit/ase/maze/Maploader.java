package de.tum.cit.ase.maze;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

    public class Maploader {

        private final MazeRunnerGame game;

        int mapWidth = 15;  //Width of the map
        int mapHeight = 15; //Height of the map (needs to be flexible in the future)

        int[][] map = new int[mapWidth][mapHeight];

        Properties properties = new Properties();

        //Constructor
        public Maploader(MazeRunnerGame game) {
            this.game = game;
        }

        //Reads the .properties file
        //TODO: Map will only load on my PC, needs fixing
        public void reader() {
            try (InputStream input = new FileInputStream("/Users/maxhu/IdeaProjects/mazerunnerMaploader/maps/level-1.properties")) {
                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Creates a map out of the data of the .properties file
        public void createMap() {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String[] coordinates = entry.getKey().toString().split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                int entityType = Integer.parseInt(entry.getValue().toString());

                map[x][y] = entityType;
            }
        }



        /* old, may be useful later
        public void render() {

            elapsedTime += Gdx.graphics.getDeltaTime();
            TextureRegion slimeFrame = slime.getAnimation().getKeyFrame(elapsedTime, true);

            ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
            game.getSpriteBatch().begin();

            game.getSpriteBatch().draw(slimeFrame, slime.getX(), slime.getY(), 64, 64);
            font.draw(game.getSpriteBatch(), "RENDER: " , 700, 700);

            game.getSpriteBatch().end();


            Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

         */







    //LEGACY, DONT KNOW IF I NEED THIS LATER
    /*
    //Loads the Map
    public static void loader() {
        // Path to my properties (problematic, needs fixing)
        String filePath = "/Users/maxhu/IdeaProjects/mazerunner/maps/level-1.properties";

        // ArrayLists für verschiedene Z-Werte
        ArrayList<String> zWallList = new ArrayList<>();
        ArrayList<String> zEntryList = new ArrayList<>();
        ArrayList<String> zExitList = new ArrayList<>();
        ArrayList<String> zTrapList = new ArrayList<>();
        ArrayList<String> zEnemyList = new ArrayList<>();
        ArrayList<String> zKeyList = new ArrayList<>();

        Properties properties = new Properties();

        try (FileInputStream fileInput = new FileInputStream(filePath)) {
            properties.load(fileInput);

            // Iteriere über alle Schlüssel-Wert-Paare in der Properties-Datei
            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);

                // Trenne den Schlüssel in x, y und z
                String[] parts = key.split(",");
                String x = parts[0];
                String y = parts[1];
                int z = Integer.parseInt(value);

                // Use switch case to handle different Z values
                switch (z) {
                    case 0:
                        zWallList.add(x + "," + y);
                        break;
                    case 1:
                        zEntryList.add(x + "," + y);
                        break;
                    case 2:
                        zExitList.add(x + "," + y);
                        break;
                    case 3:
                        zTrapList.add(x + "," + y);
                        break;
                    case 4:
                        zEnemyList.add(x + "," + y);
                        break;
                    case 5:
                        zKeyList.add(x + "," + y);
                        break;
                }
            }

            // Output the ArrayLists (optional)
            System.out.println("Z=0 List:");
            printList(zWallList);

            System.out.println("Z=1 List:");
            printList(zEntryList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to print an ArrayList (optional)
    private static void printList(ArrayList<String> list) {
        for (String element : list) {
            System.out.println(element);
        }
        System.out.println();
    }
     */
}