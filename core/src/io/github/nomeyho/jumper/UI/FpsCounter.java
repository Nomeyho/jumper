package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import io.github.nomeyho.jumper.Application;

public class FpsCounter {

    private SpriteBatch batch;
    private BitmapFont font;

    public FpsCounter(SpriteBatch batch){
        this.batch = batch;

        /*
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/keep_calm.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        this.font = generator.generateFont(parameter);
        generator.dispose();
        */
        BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
        Application.assetManager.load("fonts/fps.fnt", BitmapFont.class);
    }


    public void draw(){
        // this.font = Application.assetManager.get("fonts/fps.fnt");
        // this.font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps",5, Application.worldHeight-20);
    }
}
