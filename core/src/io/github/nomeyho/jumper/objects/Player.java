package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;
import io.github.nomeyho.jumper.utils.AnimationWrapper;
import io.github.nomeyho.jumper.utils.DirectionEnum;
import io.github.nomeyho.jumper.utils.PlayerEnum;
import io.github.nomeyho.jumper.utils.Utils;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import sun.security.krb5.internal.APOptions;

public class Player extends AbstractGameObject {
    // Const
    public static final float WIDTH = 100;
    public static final float HEIGHT = 160;
    public static final float MAX_SPEED_X = 1100;
    public static final float MAX_SPEED_Y = 2000;
    public static final float GRAVITY = 1000;
    public static final float MIN_Y = 60;
    // State
    private Vector2 touchedPos =  new Vector2();
    private DirectionEnum direction = DirectionEnum.RIGHT;
    public PlayerEnum state = PlayerEnum.WAITING;
    private float startingPosition, endPosition;
    private float maxReachSpeedY = 0;
    // Graphics
    private AnimationWrapper takeoffAnimation;
    private AnimationWrapper flyingAnimation;
    private AnimationWrapper fallAnimation;
    private TextureRegion playerTexture;
    private Vector2 deltaPos = new Vector2();


    public Player(float x, float y, int layer) {
        super(x, y, layer);
        // Ensure looking right
        this.touchedPos.x = x + WIDTH/2;
        this.touchedPos.y = y + HEIGHT/2;
        this.speed.set(0,0,0);

        HitboxAtlas hitboxAtlas = Application.get().assetManager.get(Application.HITBOX_ATLAS);
        this.hitbox = hitboxAtlas.get("player");


        this.takeoffAnimation = new AnimationWrapper(0.005f, "player_takeoff", Application.PLAYER_TAKEOFF_ATLAS, Animation.PlayMode.NORMAL);
        this.playerTexture = this.takeoffAnimation.getCurrentTexture();

        this.flyingAnimation = new AnimationWrapper(1f / 10, "player_flying", Application.PLAYER_FLYING_ATLAS);
        this.fallAnimation = new AnimationWrapper(1f / 15, "player_fall", Application.PLAYER_FALL_ATLAS, Animation.PlayMode.NORMAL);
    }


    @Override
    public void update(float delta) {

        float x =0, y =0;
        // Direction
        if(this.touchedPos.x == this.location.getX() + WIDTH / 2f){}
        else if(this.touchedPos.x > this.location.getX() + WIDTH / 2f) {
            // Direction switch reset starting pos
            if(this.direction == DirectionEnum.LEFT) {
                this.startingPosition = this.location.getX();
                deltaPos.set(this.touchedPos.x - this.location.getX() - WIDTH / 2,
                        Application.worldHeight / 2 - HEIGHT / 2);
            }
            this.direction = DirectionEnum.RIGHT;
        }
        else{
            // Direction switch reset starting pos
            if(this.direction == DirectionEnum.RIGHT) {
                this.startingPosition = this.location.getX();
                deltaPos.set(this.touchedPos.x - this.location.getX() - WIDTH / 2,
                        Application.worldHeight / 2 - HEIGHT / 2);
            }
            this.direction = DirectionEnum.LEFT;
        }
        // Starting and end points
        if(this.speed.x <= 0.01)
            this.startingPosition = this.location.getX();
        this.endPosition = this.touchedPos.x - WIDTH/2;

        switch (state){
            case WAITING:
                break;
            case TAKEOFF:
                this.takeoffAnimation.update(delta);
                this.speed.set(0,0,0);
                if(this.takeoffAnimation.animation.isAnimationFinished(this.takeoffAnimation.stateTime)){
                    this.state = PlayerEnum.FLYING;
                    giveImpulse();
                }
                break;
            case FLYING:
                /*  X */
                // Speed interpolation
                float avancement;
                if(startingPosition == endPosition)
                    avancement = 1;
                else
                    avancement = MathUtils.clamp(1 - (Math.abs(this.location.getX() - endPosition)
                            / Math.abs(startingPosition - endPosition)), 0 ,1);
                this.speed.x = Interpolation.swingOut.apply(0, MAX_SPEED_X, 1 - avancement);

                /*  Y */

                // Check max speed et when to apply gravity
                if(this.speed.y > - MAX_SPEED_Y)
                    this.speed.y -= GRAVITY * delta;

                // Animation
                if(this.speed.y >= 0) {
                    this.flyingAnimation.update(delta);
                    this.fallAnimation.stateTime = 0;
                }
                else {
                    this.fallAnimation.update(delta);
                    this.flyingAnimation.stateTime = 0;
                }

                break;
        }

        /* X */
        // Do not overextend arrival position
        if(this.direction == DirectionEnum.RIGHT)
            x = MathUtils.clamp(this.location.getX() + this.direction.getSign() * this.speed.x * delta,
                    this.location.getX(), this.endPosition);
        else
            x = MathUtils.clamp(this.location.getX() + this.direction.getSign() * this.speed.x * delta,
                    this.endPosition, this.location.getX());
        //Stay inside world
        x = MathUtils.clamp(x,0,Application.worldWidth - WIDTH);

        /* Y */
        y =  this.location.getY() + this.speed.y * delta;
        // Hit ground
        if(y <= MIN_Y && this.speed.y < 0){
            y = MIN_Y;
            this.speed.y = 0;
            // Reset
            this.state = PlayerEnum.WAITING;
            GameManager.GAME_STARTING = false;
            this.takeoffAnimation = new AnimationWrapper(0.005f, "player_takeoff", Application.PLAYER_TAKEOFF_ATLAS,Animation.PlayMode.NORMAL);
        }

        /* Set location */
        this.location.setLocation(x, y);


        /* Reset state if player hit ground
        if(this.location.getY() == 0)
            this.state = PlayerEnum.GROUNDED;*/

        // Hitbox
        this.updateHitbox(WIDTH, HEIGHT, this.location.getX(), this.location.getY());
    }

    @Override
    public void draw(SpriteBatch batch) {
        boolean flip = (this.direction == DirectionEnum.LEFT);
        float x = this.location.getX();
        float y = this.location.getY();
        switch (state){
            case WAITING:
                batch.draw(playerTexture, flip ? x + WIDTH : x, y, flip ? -WIDTH : WIDTH, HEIGHT);
                break;
            case TAKEOFF:
                this.takeoffAnimation.draw(batch, flip ? x + WIDTH : x, y, flip ? -WIDTH : WIDTH, HEIGHT);
                break;
            case FLYING:
                float Ymax = ((this.speed.y * this.speed.y) / (2 * GRAVITY));
                deltaPos.set(this.touchedPos.x - this.location.getX() - WIDTH / 2,
                       Ymax + HEIGHT);
                float angle = MathUtils.clamp(deltaPos.angle(), 25, 155);
                TextureRegion frame;
                if(this.speed.y > 0) {
                    frame = this.flyingAnimation.animation.getKeyFrame(this.flyingAnimation.stateTime, true);
                }
                else {
                    System.out.println(this.fallAnimation.animation.isAnimationFinished(this.fallAnimation.stateTime));
                    if(this.fallAnimation.animation.isAnimationFinished(this.fallAnimation.stateTime))
                        frame = this.fallAnimation.animation.getKeyFrame(this.fallAnimation.animation.getAnimationDuration());
                    else
                        frame = this.fallAnimation.animation.getKeyFrame(this.fallAnimation.stateTime, true);
                }
                batch.draw(frame, flip ? x + WIDTH : x, y, WIDTH /2, HEIGHT /2, flip ? -WIDTH : WIDTH, HEIGHT, 1, 1, angle - 90);
                break;
        }
    }

    public void setTouchedPos(Vector3 pos) {
        this.touchedPos.x = pos.x;
        this.touchedPos.y = pos.y;
    }

    public void giveImpulse(){
        this.speed.y += 1000;
        this.maxReachSpeedY = this.speed.y;
    }

}
