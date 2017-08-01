package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.nomeyho.jumper.Application;

public class MenuBackground {
    private static final float FADE_IN = 0.14f;
    public TextureRegion background;
    private float width;
    private float height;
    public float time = 1;
    public boolean fadeIn = false;

    public MenuBackground() {
        TextureAtlas textureAtlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.background = textureAtlas.findRegion("background");
    }

    public void update (float delta) {
        this.time += delta;
    }

    public void draw (Batch batch) {
        float alpha;
        if(this.time <= FADE_IN) {
            alpha = (this.time / FADE_IN) % 1;
        } else {
            alpha = 1;
            this.fadeIn = false;
        }

        batch.setColor(1, 1, 1, alpha);
        batch.draw(this.background, 0, 0, width, this.height);
        batch.setColor(1, 1, 1, 1);
    }

    public void resize() {
        this.width = Application.worldWidth;
        this.height = this.background.getRegionHeight() * (Application.worldWidth / this.background.getRegionWidth());
    }

    public void fadeIn () {
        this.time = 0;
        this.fadeIn = true;
    }
}
