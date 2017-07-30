package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Pool;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;
import io.github.nomeyho.jumper.particles.ParticleEnum;
import io.github.nomeyho.jumper.particles.ParticleManager;
import io.github.nomeyho.jumper.utils.AnimationWrapper;
import io.github.nomeyho.jumper.utils.ColorManager;

public class Portal extends AbstractGameObject implements Pool.Poolable {
    public static final float WIDTH = 85;
    public static final float HEIGHT = 55;
    private static final float SPEED = -10;
    private static final int SCORE = 1;
    private TextureRegion frontTexture;
    private TextureRegion backTexture;
    private AnimationWrapper explosion;
    private Color color;
    private ParticleEffect dustEffect;

    public Portal(float x, float y) {
        TextureAtlas textureAtlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.frontTexture = textureAtlas.findRegion("portal_front");
        this.backTexture = textureAtlas.findRegion("portal_back");

        HitboxAtlas hitboxAtlas = Application.get().assetManager.get(Application.HITBOX_ATLAS);
        this.hitbox = hitboxAtlas.get("bell");

        this.dustEffect = ParticleManager.get().getEffect(ParticleEnum.DUST);
        this.dustEffect.start();
        init(x, y);
    }

    @Override
    public void init(float x, float y){
        this.location.setLocation(x, y);
        this.speed.set(0, SPEED, 0);
        this.color = ColorManager.get().getColor(y);
    }

    @Override
    public int getScore() {
        return SCORE;
    }

    @Override
    public void update(float delta) {
        this.location.add(0, + SPEED * delta);
        this.updateHitbox(WIDTH, HEIGHT, this.location.getX(), this.location.getY());
        this.dustEffect.update(delta);
        this.dustEffect.setPosition(this.location.getX()+20, this.location.getY()+20);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setColor(color);
       batch.draw(this.frontTexture, this.location.getX(), this.location.getY(), WIDTH, HEIGHT);
        batch.setColor(1,1,1,1);
    }

    @Override
    public void drawBackground(SpriteBatch batch) {
        batch.setColor(color);
        this.dustEffect.draw(batch);
        batch.draw(this.backTexture, this.location.getX(), this.location.getY(), WIDTH, HEIGHT);
        batch.setColor(1,1,1,1);
    }

    @Override
    public void reset() {
    }
}
