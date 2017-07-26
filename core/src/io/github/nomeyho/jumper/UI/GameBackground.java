package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.utils.AnimationWrapper;

public class GameBackground {

    private AnimationWrapper animation;
    private TextureRegion background;

    public GameBackground () {
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.background = atlas.findRegion("background");

        this.animation = new AnimationWrapper(0.33f, "background", Application.BACKGROUND_ANIM_ATLAS);
    }

    public void update (float delta) {
        this.animation.update(delta);
    }

    public void draw (SpriteBatch batch) {
        float w = Application.worldWidth;
        float h = this.background.getRegionHeight() * (w / this.background.getRegionWidth());

        // Animation
        /*
        TextureRegion frame = this.animation.getCurrentTexture();
        batch.draw(frame, 0, 0, h, w);
        */

        // Background
        batch.draw(this.background, 0, 0, w, h);
    }
}
