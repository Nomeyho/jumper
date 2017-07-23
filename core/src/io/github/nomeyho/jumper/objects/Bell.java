package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;
import io.github.nomeyho.jumper.utils.AnimationWrapper;
import io.github.nomeyho.jumper.utils.Utils;

public class Bell extends AbstractGameObject {
    public static final float WIDTH = 126;
    public static final float HEIGHT = 100;
    private static final float SPEED = 1;
    private TextureRegion bellTexture;
    private AnimationWrapper explosion;

    public Bell(float x, float y, int layer) {
        super(x, y, layer);

        TextureAtlas textureAtlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.bellTexture = textureAtlas.findRegion("bell", Utils.randomInt(1, 3));

        this.speed.set(0, SPEED, 0);

        HitboxAtlas hitboxAtlas = Application.get().assetManager.get(Application.HITBOX_ATLAS);
        this.hitbox = hitboxAtlas.get("bell");

        this.explosion = new AnimationWrapper(0.33f, textureAtlas.findRegions("bell_explosion"));
    }

    @Override
    public void update(float delta) {
        this.location.add(0, - SPEED * delta);
        this.updateHitbox(WIDTH, HEIGHT, this.location.getX(), this.location.getY());
        this.explosion.update(delta);
    }

    @Override
    public void draw(SpriteBatch batch) {
       // batch.draw(this.bellTexture, this.location.getX(), this.location.getY(), WIDTH, HEIGHT);
        this.explosion.draw(batch, this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }
}
