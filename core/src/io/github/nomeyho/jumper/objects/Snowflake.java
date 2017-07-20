package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.math.Location;

public class Snowflake {

    public static final float WIDTH = 50;
    public static final float HEIGHT = 50;
    public Location location = new Location(0, 0, 0);
    public Vector3 speed = new Vector3(0,0,0);
    private TextureRegion snowflakeTexture;

    public Snowflake(float x, float y, int layer) {
        this.location.setLocation(x,y);
        this.location.setLayer(layer);
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.snowflakeTexture =  atlas.findRegion("snowflake");
    }

    public void update(float delta) {
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this.snowflakeTexture,this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }
}
