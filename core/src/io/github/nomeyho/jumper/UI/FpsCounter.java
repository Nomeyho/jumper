package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import io.github.nomeyho.jumper.Application;

public class FpsCounter {

    private SpriteBatch batch;
    private BitmapFont font;

    public FpsCounter(SpriteBatch batch){
        this.batch = batch;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chocolate.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        this.font = generator.generateFont(parameter);
        generator.dispose();
    }


    public void draw(){
        this.font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps",5, Application.worldHeight-20);
    }
}
