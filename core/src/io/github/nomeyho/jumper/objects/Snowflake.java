package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.math.Location;
import io.github.nomeyho.jumper.utils.Utils;

import java.util.Random;

public class Snowflake {

    private float width = 15, height = 15;
    public Location location = new Location(0, 0, 1);
    public Vector3 speed = new Vector3(0,-50,0);
    private TextureRegion snowflakeTexture;
    public float movementOriginX = 0;
    public float movementAmplitudeX = 0;
    public float movementDistortionX = 0;
    public float movementPhaseX = 0;

    private Random rand = new Random();

    public Snowflake(float x, float y) {
        this.location.setLocation(x,y);
        init();
        this.movementOriginX = this.location.getX();

        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.snowflakeTexture =  atlas.findRegion("snowflake");
    }

    public void init() {
        this.location.setLayer(Utils.getRandom(new Integer[]{-1,1}));
        this.speed.y = Utils.randomFloat(-30,-60);
        this.width = this.height = Utils.randomFloat(12, 20);
        this.movementAmplitudeX = Utils.randomFloat(0, Application.worldWidth / 20);
        this.movementDistortionX = Utils.randomFloat(0.1f, 0.6f);
        this.movementPhaseX = Utils.randomFloat(0, Application.PI);
    }


    public void update(float delta) {
        float x = movementAmplitudeX * MathUtils.sin(
                movementDistortionX*GameManager.get().snowflakeManager.counter + movementPhaseX);
        this.location.setLocation(movementOriginX + x , this.location.getY() + this.speed.y * +delta);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this.snowflakeTexture,this.location.getX(),this.location.getY(), width, height);
    }

    public float getHeight(){
        return this.height;
    }

}
