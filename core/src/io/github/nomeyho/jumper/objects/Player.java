package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.utils.DirectionEnum;
import io.github.nomeyho.jumper.utils.Utils;

public class Player extends AbstractGameObject {
    public static final float WIDTH = 100;
    public static final float HEIGHT = 180;
    public static final float SPEED_MAX_X = 2000;
    public static final float SPEEDX =  1000;
    public static final float SPEED_MAX_Y_DOWN = -2000;
    public static final float ACCELX = 2000;
    public static final float ACCELY = -2000;

    private TextureRegion playerTexture;
    private Vector3 touchedPos = new Vector3();
    private DirectionEnum direction = DirectionEnum.RIGHT;



    public Player(float x, float y, int layer) {
        super(x, y, layer);
        TextureAtlas atlas = Application.get().assetManager.get("assets.atlas");
        this.playerTexture =  atlas.findRegion("player");
    }

    private float previous_static_position;
    private double final_speed;
    private double previous_distance = 0;
    @Override
    public void update(float delta) {
        // Handle x
        if( Math.abs(this.touchedPos.x - this.location.getX() - WIDTH * 0.5) > 0.001 && GameManager.GAME_STARTING){
           // Sign change -> instant redirection
           DirectionEnum newDirection = DirectionEnum.toDirection(this.touchedPos.x - this.location.getX() - WIDTH * 0.5 );
           if(this.direction != newDirection) { this.speed.x = 0; }
           this.direction = newDirection;

           if(this.speed.x == 0){
             this.previous_static_position = this.location.getX();
           }
           double distance =  Math.abs(this.touchedPos.x - this.previous_static_position - WIDTH * 0.5);
            if(Math.abs(this.touchedPos.x - this.location.getX() - WIDTH * 0.5) < 0.45 * distance &&
                    previous_distance >= distance){
                System.out.println("friction start : "+this.speed.x);
                if(Math.abs(this.speed.x) > Math.abs(direction.getSign() * ((-0.5*final_speed*final_speed)/(0.5 * distance)) * delta))
                    this.speed.x += direction.getSign() * ((-0.5*final_speed*final_speed)/(0.45 * distance)) * delta;
            }
            else {
                this.speed.x = this.direction.getSign() * SPEEDX;
                final_speed = this.speed.x;
            }
            previous_distance= distance;
           /* Max speed check
           if(Math.abs(this.speed.x) < SPEED_MAX_X)
                this.speed.x += this.direction.getSign() * ACCELX * delta;*/
           // Ensure that the new position do not overextend the arrival point (delta)
           float x;
           if(this.direction == DirectionEnum.RIGHT)
             x = MathUtils.clamp(this.location.getX() + this.speed.x * delta, this.location.getX(), touchedPos.x - WIDTH * 0.5f);
           else
             x = MathUtils.clamp(this.location.getX() + this.speed.x * delta, touchedPos.x - WIDTH * 0.5f, this.location.getX());
           // Ensure that the new position remains inside de viewport limits
           x = MathUtils.clamp(x, 0, Application.worldWidth - WIDTH);
           this.location.setLocation(x,this.location.getY());
        }
        else{
            this.speed.x =0;
            previous_distance =0;
        }

        // Handle y
        if(this.speed.y >= SPEED_MAX_Y_DOWN)
            this.speed.y += ACCELY * delta;
        float y = location.getY() + speed.y * delta;
        if(y<=0){ y = 0; }
        this.location.setLocation(location.getX(), y);
        if(this.location.getY() == 0)
            this.speed.y =0;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this.playerTexture,this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }

    public void setTouchedPos(Vector3 pos) {
        this.touchedPos.set(pos);
    }

    public void jump(){
        this.speed.y += 1000;
    }

}
