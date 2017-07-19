package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;

public class FpsCounter {

    private BitmapFont font;

    public FpsCounter() {
        this.font = Application.get().assetManager.get("fonts/dejavu.fnt");
    }

    public void draw(SpriteBatch batch) {
        this.font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps",5, Application.worldHeight - 20);
    }
}
