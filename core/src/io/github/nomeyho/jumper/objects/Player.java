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
    public static final float SPEED_MAX_Y = 50;
    public static final float ACCELX = 4000;
    public static final float ACCELY = -10;
    public static final float ACCEL_SMOOTH_LEVEL = 1;

    private float direction = 0;
    private TextureRegion playerTexture;
    private Vector3 touchedPos = new Vector3();

    public Player(float x, float y, int layer) {
        super(x, y, layer);
        TextureAtlas atlas = Application.assetManager.get("assets.atlas");
        this.playerTexture =  atlas.findRegion("player");
    }

    @Override
    public void update(float delta) {
        // Set direction
        if((this.location.getX()+ Player.WIDTH/2) <= this.touchedPos.x)
            this.direction = 1;
        else
            this.direction = -1;

        // Acceleration
        for(int n=1; n<=ACCEL_SMOOTH_LEVEL; n++)
        {
            if(this.speed.x < SPEED_MAX_X )
                this.speed.x += ACCELX * delta / ACCEL_SMOOTH_LEVEL;
            if(this.speed.y < SPEED_MAX_Y)
                this.speed.y += ACCELY * delta / ACCEL_SMOOTH_LEVEL;
            // Move along X
            if( Math.abs(this.touchedPos.x - this.location.getX() - WIDTH/2) > Math.abs(this.speed.x * delta / (2*ACCEL_SMOOTH_LEVEL))) {
                Float normWidth = MathUtils.clamp(
                        this.location.getX() + ( direction * this.speed.x * delta / ACCEL_SMOOTH_LEVEL),
                        0,
                        Application.worldWidth - Player.WIDTH
                );
                this.location.setLocation(normWidth, this.location.getY());
            }
            else {
                this.direction = 0;
                this.speed.x = 0;
            }
            // Move along Y
            this.location.add(0, - this.speed.y * delta / ACCEL_SMOOTH_LEVEL);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this.playerTexture,this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }

    public void setTouchedPos(Vector3 pos) {
        this.touchedPos.set(pos);
    }
}
