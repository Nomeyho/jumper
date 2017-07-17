package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.Application;

public class FpsCounter {

    private SpriteBatch batch;
    private BitmapFont font;

    public FpsCounter(SpriteBatch batch){
        this.batch = batch;
        this.font = Application.assetManager.get("fonts/dejavu.fnt");
    }

    public void draw() {
        this.font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps",5, Application.worldHeight-20);
    }
}
