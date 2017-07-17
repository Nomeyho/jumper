package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.nomeyho.jumper.Application;

public class Player extends AbstractGameObject {
    public static final float WIDTH = 100;
    public static final float HEIGHT = 180;
    private TextureRegion playerTexture;

    public Player(float x, float y, int layer) {
        super(x, y, layer);
        TextureAtlas atlas = Application.assetManager.get("assets.atlas");
        this.playerTexture =  atlas.findRegion("player");
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this.playerTexture,this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }
}
