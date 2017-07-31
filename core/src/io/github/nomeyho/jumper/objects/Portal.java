package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Pool;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;
import io.github.nomeyho.jumper.particles.ParticleEnum;
import io.github.nomeyho.jumper.particles.ParticleManager;
import io.github.nomeyho.jumper.utils.ColorManager;

public class Portal extends AbstractGameObject implements Pool.Poolable {
    public static final float WIDTH = 60;
    public static final float HEIGHT = 45;
    private static final float SPEED = -30;
    private static final int SCORE = 1;
    private TextureRegion frontTexture;
    private TextureRegion backTexture;
    private Color color;
    private ParticleEffect dustEffect;
    public float scale;
    public boolean disappear;
    public boolean toRemove;
    private float time;

    public Portal(float x, float y) {
        TextureAtlas textureAtlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.frontTexture = textureAtlas.findRegion("portal_front");
        this.backTexture = textureAtlas.findRegion("portal_back");

        HitboxAtlas hitboxAtlas = Application.get().assetManager.get(Application.HITBOX_ATLAS);
        this.hitbox = hitboxAtlas.get("portal");

        this.dustEffect = ParticleManager.get().getEffect(ParticleEnum.DUST);
        this.dustEffect.start();
        init(x, y);
    }

    @Override
    public void init(float x, float y){
        this.location.setLocation(x, y);
        this.speed.set(0, SPEED, 0);
        this.color = ColorManager.get().getColor(y);
        this.scale = 1;
        this.disappear = false;
        this.toRemove = false;
        this.time = 0;
    }

    @Override
    public int getScore() {
        return SCORE;
    }

    @Override
    public float getWidth() {
        return WIDTH;
    }

    @Override
    public float getHeight() {
        return HEIGHT;
    }

    @Override
    public void update(float delta) {
        if(this.toRemove) return;

        if(this.disappear) {
            this.time += delta;
            // this.scale = Interpolation.pow5in.apply((1 - (time) % 1));
            this.scale = Interpolation.swingIn.apply((1 - (time) % 1));
            if(this.scale < 0.001)
                this.toRemove = true;
        }

        this.location.add(0, + SPEED * delta);
        this.updateHitbox(this.disappear ? 0 : WIDTH, this.disappear ? 0 : HEIGHT, this.location.getX(), this.location.getY(), 0);
        this.dustEffect.update(delta);
        this.dustEffect.setPosition(this.location.getX()+20, this.location.getY()+20);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setColor(color);
        batch.draw(
                this.frontTexture,
                this.location.getX(),
                this.location.getY(),
                WIDTH/2,
                HEIGHT/2,
                WIDTH,
                HEIGHT,
                this.scale,
                this.scale,
                0
        );
        batch.setColor(1,1,1,1);
    }

    @Override
    public void drawBackground(SpriteBatch batch) {
        batch.setColor(color);
        // TODO this.dustEffect.draw(batch);
        batch.draw(
                this.backTexture,
                this.location.getX(),
                this.location.getY(),
                WIDTH/2,
                HEIGHT/2,
                WIDTH,
                HEIGHT,
                this.scale,
                this.scale,
                0
        );
        batch.setColor(1,1,1,1);
    }

    @Override
    public void reset() {
    }

    @Override
    public void disappear () {
        this.disappear = true;
        this.scale = 1;
    }
}
