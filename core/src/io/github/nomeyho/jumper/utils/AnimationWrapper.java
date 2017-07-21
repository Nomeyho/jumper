package io.github.nomeyho.jumper.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationWrapper {
    public Animation<TextureRegion> animation;
    public float stateTime = 0;

    public AnimationWrapper(float frameDuration, Array<TextureAtlas.AtlasRegion> textures) {
        this.animation = new Animation<TextureRegion>(frameDuration, textures);
        this.animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        this.stateTime = (this.stateTime + delta) % Float.MAX_VALUE;
    }

    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        TextureRegion frame = this.animation.getKeyFrame(this.stateTime, true);
        batch.draw(frame, x, y, width, height);
    }
}
