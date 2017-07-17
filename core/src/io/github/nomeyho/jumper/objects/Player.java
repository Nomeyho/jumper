package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.Application;

public class Player extends AbstractGameObject {
    public static final float WIDTH = 100;
    public static final float HEIGHT = 180;
    public static final float SPEED_MAX_X = 2000;
    public static final float ACCELX = 6000;
    public static float direction = 0;

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

    public void move(float delta){
        Float normWidth = MathUtils.clamp(this.location.getX() + ( direction * this.speed.x * delta), 0
                        , Application.worldWidth - Player.WIDTH);
        this.location.setLocation(normWidth, this.location.getY());
    }
}
