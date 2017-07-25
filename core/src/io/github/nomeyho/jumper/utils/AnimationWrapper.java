package io.github.nomeyho.jumper.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import io.github.nomeyho.jumper.Application;

public class AnimationWrapper {
    public Animation<TextureRegion> animation;
    public float stateTime = 0;

    public AnimationWrapper(float frameDuration, String region, String atlasName) {
        this(frameDuration, region, atlasName, Animation.PlayMode.LOOP);
    }

    public AnimationWrapper(float frameDuration, String region, String atlasName, Animation.PlayMode playmode) {
        TextureAtlas atlas = Application.get().assetManager.get(atlasName);
        this.animation = new Animation<TextureRegion>(frameDuration, atlas.findRegions(region));
        this.animation.setPlayMode(playmode);
    }

    public void update(float delta) {
        this.stateTime = (this.stateTime + delta) % Float.MAX_VALUE;
    }

    public TextureRegion getCurrentTexture () {
        return this.animation.getKeyFrame(this.stateTime);
    }

    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        TextureRegion frame = this.animation.getKeyFrame(this.stateTime, true);
        batch.draw(frame, x, y, width, height);
    }
}
