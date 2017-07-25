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
    public static final float WIDTH = 100;
    public static final float HEIGHT = 160;
    public static final float MAX_SPEED_X = 1200;
    private Vector2 touchedPos =  new Vector2();
    private AnimationWrapper groundAnimation;
    private AnimationWrapper flyingAnimation;
    private TextureRegion playerTexture;
    private DirectionEnum direction = DirectionEnum.RIGHT;
    public PlayerEnum state = PlayerEnum.WAITING;
    private float startingPosition, endPosition;


    public Player(float x, float y, int layer) {
        super(x, y, layer);
        // Ensure looking right
        this.touchedPos.x = x + WIDTH/2;
        this.touchedPos.y = y + HEIGHT/2;
        this.speed.set(0,0,0);

        HitboxAtlas hitboxAtlas = Application.get().assetManager.get(Application.HITBOX_ATLAS);
        this.hitbox = hitboxAtlas.get("player");


        this.groundAnimation = new AnimationWrapper(1f / 70, "player_takeoff", Application.PLAYER_TAKEOFF_ATLAS,Animation.PlayMode.NORMAL);
        this.playerTexture = this.groundAnimation.getCurrentTexture();

        this.flyingAnimation = new AnimationWrapper(1f / 10, "player_flying", Application.PLAYER_FLYING_ATLAS, Animation.PlayMode.NORMAL);
    }


    @Override
    public void update(float delta) {

        flyingAnimation.update(delta);
        // Direction
        if(this.touchedPos.x >= this.location.getX() + WIDTH / 2f) {
            // Direction switch reset starting pos
            if(this.direction == DirectionEnum.LEFT)
                this.startingPosition = this.location.getX();
            this.direction = DirectionEnum.RIGHT;
        }
        else{
            // Direction switch reset starting pos
            if(this.direction == DirectionEnum.RIGHT)
                this.startingPosition = this.location.getX();
            this.direction = DirectionEnum.LEFT;
        }
        // Starting and end points
        if(this.speed.x <= 0.01)
            this.startingPosition = this.location.getX();
        this.endPosition = this.touchedPos.x - WIDTH/2;

        switch (state){
            case WAITING:
                break;
            case GROUNDED:
                this.groundAnimation.update(delta);
                if(this.groundAnimation.animation.isAnimationFinished(this.groundAnimation.stateTime))
                    this.state = PlayerEnum.TAKEOFF;
                break;
            case TAKEOFF:
                break;
            case FLYING:
                /* Handle X movement */

                // Speed interpolation
                float avancement;
                if(startingPosition == endPosition)
                    avancement = 1;
                else
                    avancement = MathUtils.clamp(1 - (Math.abs(this.location.getX() - endPosition)
                            / Math.abs(startingPosition - endPosition)), 0 ,1);
                this.speed.x = Interpolation.pow3Out.apply(0, MAX_SPEED_X, 1 - avancement);
                // Do not overextend arrival position
                float posX;
                if(this.direction == DirectionEnum.RIGHT)
                    posX = MathUtils.clamp(this.location.getX() + this.direction.getSign() * this.speed.x * delta,
                            this.location.getX(), this.endPosition);
                else
                    posX = MathUtils.clamp(this.location.getX() + this.direction.getSign() * this.speed.x * delta,
                            this.endPosition, this.location.getX());
                //Stay inside world
                posX = MathUtils.clamp(posX,0,Application.worldWidth - WIDTH);
                // Set location
                this.location.setLocation(posX, 0);

                /* Handle Y movement */


                break;
        }

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
                this.flyingAnimation.draw(batch, flip ? x + WIDTH : x, y, flip ? -WIDTH : WIDTH, HEIGHT);
                break;
            case GROUNDED:
                this.groundAnimation.draw(batch, flip ? x + WIDTH : x, y, flip ? -WIDTH : WIDTH, HEIGHT);
                break;
            case TAKEOFF:
                this.flyingAnimation.draw(batch, flip ? x + WIDTH : x, y, flip ? -WIDTH : WIDTH, HEIGHT);
                break;
            case FLYING:

                break;
        }
    }

    public void setTouchedPos(Vector3 pos) {
        this.touchedPos.x = pos.x;
        this.touchedPos.y = pos.y;
        this.speed.x = 1000;
    }

}
