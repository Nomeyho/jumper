package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.nomeyho.jumper.Application;

public class MenuBackground {
    private TextureRegion background;
    private float width;
    private float height;

    public MenuBackground() {
        TextureAtlas textureAtlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.background = textureAtlas.findRegion("background");
    }

    public void update (float delta) {}

    public void draw (Batch batch) {
        batch.draw(this.background, 0, 0, width, this.height);
    }

    public void resize() {
        this.width = Application.get().worldWidth;
        this.height = this.background.getRegionHeight() * (Application.worldWidth / this.background.getRegionWidth());
    }
}
