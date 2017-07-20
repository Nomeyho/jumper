package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;

public class Bell extends AbstractGameObject {
    public static final float WIDTH = 100;
    public static final float HEIGHT = 100;
    private static final float SPEED = 25;
    private TextureRegion bellTexture;

    public Bell(float x, float y, int layer) {
        super(x, y, layer);

        TextureAtlas textureAtlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.bellTexture =  textureAtlas.findRegion("bell");

        this.speed.set(0, SPEED, 0);

        HitboxAtlas hitboxAtlas = Application.get().assetManager.get(Application.HITBOX_ATLAS);
        this.hitbox = hitboxAtlas.get("bell");
    }

    @Override
    public void update(float delta) {
        this.location.add(0, - SPEED * delta);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this.bellTexture,this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }
}
