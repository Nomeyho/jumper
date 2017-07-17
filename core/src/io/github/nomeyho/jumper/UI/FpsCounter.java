package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameScreen;

public class FpsCounter {

    private SpriteBatch batch;
    private BitmapFont font;

    public FpsCounter(SpriteBatch batch){
        this.batch = batch;
        font =  new BitmapFont();
        font.getData().setScale(2.5f);
    }


    public void draw(){
        this.font.draw(batch, (int) Gdx.graphics.getFramesPerSecond() + " fps",5, Application.worldHeight-20);
    }
}
