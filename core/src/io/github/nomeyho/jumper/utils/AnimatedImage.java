package io.github.nomeyho.jumper.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image {
    private final AnimationWrapper animationWrapper;

    public AnimatedImage(AnimationWrapper animationWrapper) {
        super(animationWrapper.getCurrentTexture());
        this.animationWrapper = animationWrapper;
    }

    @Override
    public void act(float delta) {
        ((TextureRegionDrawable)getDrawable()).setRegion(this.animationWrapper.getCurrentTexture());
        this.animationWrapper.update(delta);
        super.act(delta);
    }
}
