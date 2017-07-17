package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.nomeyho.jumper.Application;

public class Bell extends AbstractGameObject {

    public static final float WIDTH = 100;
    public static final float HEIGHT = 100;
    private static final float SPEED = 75;
    private TextureRegion bellTexture;

    public Bell(float x, float y, int layer) {
        super(x, y, layer);
        TextureAtlas atlas = Application.assetManager.get("assets.atlas");
        this.bellTexture =  atlas.findRegion("bell");
        this.speed.set(0, SPEED, 0);
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
