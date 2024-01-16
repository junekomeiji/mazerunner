package de.tum.cit.ase.maze;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.*;
import de.tum.cit.ase.maze.Entities.Mobs.Player;

import javax.swing.text.View;


public class HUD implements Disposable {

    //adapted from https://gamedev.stackexchange.com/questions/31379/how-do-i-make-an-on-screen-hud-in-libgdx

    public Stage stage;
    private Viewport vp;

    private Label testLabel;

    private int lives;
    private int key;
    private int score;
    public HUD(SpriteBatch sb, Player p){
        Viewport vp = new ScreenViewport();
        stage = new Stage(vp, sb);

        testLabel = new Label("Test", new Label.LabelStyle(new BitmapFont(), Color.GOLD));

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(testLabel).expandX().padTop(10);

        stage.addActor(table);
        int lives = p.getLives();
        int key = p.getScore();
    }

    @Override
    public void dispose(){stage.dispose();}
}
