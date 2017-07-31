package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;
import io.github.nomeyho.jumper.particles.ParticleEnum;
import io.github.nomeyho.jumper.particles.ParticleManager;
import io.github.nomeyho.jumper.utils.*;

public class Player extends AbstractGameObject {
    // Const
    public static final float WIDTH = 70;
    public static final float HEIGHT = 110;
    public static final float MAX_SPEED_X = 750;
    public static final float MAX_SPEED_Y = 2000;
    public static final float GRAVITY = 1800;
    public static final float MIN_Y = 60;
    // State
    private Vector2 touchedPos =  new Vector2();
    private DirectionEnum direction = DirectionEnum.RIGHT;
    public PlayerEnum state = PlayerEnum.WAITING;
    private float startingPosition, endPosition;
    private Vector2 deltaPos = new Vector2();
    private float angle =0;
    private float maxHeight =0;
    // Graphics
    private AnimationWrapper takeoffAnimation;
    private AnimationWrapper flyingAnimation;
    private AnimationWrapper fallAnimation;
    private TextureRegion playerTexture;
    // Particle
    private ParticleEffect smokeEffect;
    private ParticleEffect fireEffect;


    public Player(float x, float y) {

        HitboxAtlas hitboxAtlas = Application.get().assetManager.get(Application.HITBOX_ATLAS);
        this.hitbox = hitboxAtlas.get("player");

        this.takeoffAnimation = new AnimationWrapper(0.005f, "player_takeoff", Application.PLAYER_TAKEOFF_ATLAS, Animation.PlayMode.NORMAL);
        this.playerTexture = this.takeoffAnimation.getCurrentTexture();

        this.flyingAnimation = new AnimationWrapper(1f / 10, "player_flying", Application.PLAYER_FLYING_ATLAS);
        this.fallAnimation = new AnimationWrapper(1f / 50, "player_fall", Application.PLAYER_FALL_ATLAS, Animation.PlayMode.NORMAL);

        this.smokeEffect = ParticleManager.get().getEffect(ParticleEnum.SMOKE);
        this.fireEffect = ParticleManager.get().getEffect(ParticleEnum.FIRE);
        init(x, y);
    }

    @Override
    public void init(float x, float y){
        this.state = PlayerEnum.WAITING;
        this.speed.set(0,0,0);
        this.location.setLocation(x, y);
        this.touchedPos.x = x + WIDTH/2;
        this.touchedPos.y = y + HEIGHT/2;

    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public float getWidth() {
        return WIDTH;
    }

    @Override
    public float getHeight() {
        return HEIGHT;
    }

    @Override
    public void update(float delta) {

        setDirection();
        setReferencePosition();

        switch (state){
            case WAITING:
                // Animation
                this.takeoffAnimation.stateTime =0;
                break;
            case TAKEOFF:
                this.speed.set(0,0,0);
                // Particles
                if(!this.smokeEffect.isComplete()) {
                    for (int i = 0; i < this.smokeEffect.getEmitters().size; i++)
                        this.smokeEffect.getEmitters().get(i).durationTimer = 0;
                }
                playParticle(this.smokeEffect);
                // Animations
                if(this.takeoffAnimation.animation.isAnimationFinished(this.takeoffAnimation.stateTime)){
                    this.state = PlayerEnum.FLYING;
                    setSpeed(1500);
                    this.fireEffect.start();
                }
                // Animation
                this.takeoffAnimation.update(delta);
                break;
            case FLYING:
                updateSpeedX(delta);
                updateSpeedY(delta);
                if(this.speed.y >= 0) {
                    // Animation
                    this.flyingAnimation.update(delta);
                    this.fallAnimation.stateTime = 0;
                    // Particle
                    if(this.fireEffect.getEmitters().first().durationTimer > 0.8* this.fireEffect.getEmitters().first().duration)
                        this.fireEffect.getEmitters().first().durationTimer = 0.2f * this.fireEffect.getEmitters().first().duration;
                }
                else {
                    this.fallAnimation.update(delta);
                    this.flyingAnimation.stateTime = 0;
                    if(this.speed.y > - 50)
                        this.fireEffect.getEmitters().first().durationTimer = this.fireEffect.getEmitters().first().duration;
                }
                break;
        }

        setPosition(delta);
        setAngle();
        hasHitGround();
        updateEffect(delta);

        if(this.location.getY() > this.maxHeight)
             this.maxHeight = this.location.getY();
        if(this.location.getY() < this.maxHeight - Application.worldHeight*2f) {
            this.maxHeight = 0;
            this.location.setLocation(this.location.getX(), Application.worldHeight);
        }
        this.updateHitbox(WIDTH, HEIGHT, this.location.getX(), this.location.getY(), this.angle);
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
                //Particle
                this.fireEffect.draw(batch);
                // Animation
                TextureRegion frame = getFlyingframe();
                if(flip && !frame.isFlipX())
                    frame.flip(true, false);
                else if (!flip && frame.isFlipX())
                    frame.flip(true, false);
                batch.draw(frame, x, y, WIDTH /2, HEIGHT /2, WIDTH, HEIGHT, 1, 1, this.angle);
                break;
        }

        this.smokeEffect.draw(batch);
    }

    @Override
    public void drawBackground(SpriteBatch batch) {}

    public void setTouchedPos(Vector3 pos) {
        this.touchedPos.x = pos.x;
        this.touchedPos.y = pos.y;
    }

    public void setSpeed(float intensity){
            if(intensity < -MAX_SPEED_Y)
                intensity = -MAX_SPEED_Y;
            else if(intensity > MAX_SPEED_Y)
                intensity = MAX_SPEED_Y;
            this.speed.y = intensity;
    }

    private void setAngle(){
        deltaPos.set(this.touchedPos.x - this.location.getX() - WIDTH / 2,
                (this.speed.y * this.speed.y) / (2 * GRAVITY) + HEIGHT);
        this.angle = (MathUtils.clamp(deltaPos.angle(), 25, 155)-90);
    }

    private TextureRegion getFlyingframe(){
        TextureRegion frame;
        if(this.speed.y > 0) {
            frame = this.flyingAnimation.animation.getKeyFrame(this.flyingAnimation.stateTime, true);
        }
        else {
            if(this.fallAnimation.animation.isAnimationFinished(this.fallAnimation.stateTime))
                frame = this.fallAnimation.animation.getKeyFrame(this.fallAnimation.animation.getAnimationDuration());
            else
                frame = this.fallAnimation.animation.getKeyFrame(this.fallAnimation.stateTime, true);
        }
        return frame;
    }

    private void setDirection(){
        if(this.touchedPos.x == this.location.getX() + WIDTH / 2f){}
        else if(this.touchedPos.x > this.location.getX() + WIDTH / 2f) {
            if(this.direction == DirectionEnum.LEFT)
                this.startingPosition = this.location.getX();
            this.direction = DirectionEnum.RIGHT;
        }
        else{
            if(this.direction == DirectionEnum.RIGHT)
                this.startingPosition = this.location.getX();
            this.direction = DirectionEnum.LEFT;
        }
    }

    private void setReferencePosition(){
        if(this.speed.x <= 0.01)
            this.startingPosition = this.location.getX();
        this.endPosition = this.touchedPos.x - WIDTH/2;
    }


    private void updateSpeedX(float delta){
        float avancement;
        if(startingPosition == endPosition)
            avancement = 1;
        else
            avancement = MathUtils.clamp(1 - (Math.abs(this.location.getX() - endPosition)
                    / Math.abs(startingPosition - endPosition)), 0 ,1);
        this.speed.x = Interpolation.swingOut.apply(0, MAX_SPEED_X, 1 - avancement);
    }

    private void updateSpeedY(float delta){
        if(this.speed.y > - MAX_SPEED_Y)
            this.speed.y -= GRAVITY * delta;
    }

    private void setPosition(float delta){
        float x, y;
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
        this.location.setLocation(x, y);
    }

    private void hasHitGround(){
        // System.out.println(this.speed.y);
        if(this.location.getY() <= MIN_Y && this.speed.y < 0){
            this.location.setLocation(this.location.getX(), MIN_Y);
            this.speed.y = 0;
            this.state = PlayerEnum.WAITING;
            GameManager.get().state = GameState.ENDED;
            playParticle(this.smokeEffect);
            this.fireEffect.reset();
        }
    }

    private void updateEffect(float delta){
        this.smokeEffect.update(delta);
        this.smokeEffect.setPosition(location.getX() + WIDTH/2, location.getY());
        this.fireEffect.update(delta);
        float radius = WIDTH / 2.55f;
        if(this.direction == DirectionEnum.LEFT)
            this.fireEffect.setPosition(this.location.getX() + WIDTH/2f + radius*MathUtils.cosDeg(320 + this.angle),
                    this.location.getY() + HEIGHT/2f + radius*MathUtils.sinDeg(320 + this.angle));
        else
            this.fireEffect.setPosition(this.location.getX() + WIDTH/2f + radius*MathUtils.cosDeg(230 + this.angle),
                    this.location.getY() + HEIGHT/2f + radius*MathUtils.sinDeg(230 + this.angle));
        this.fireEffect.getEmitters().first().getRotation().setLow( this.angle, this.angle);
        this.fireEffect.getEmitters().first().getRotation().setHigh( this.angle, this.angle);
        this.fireEffect.getEmitters().first().getAngle().setLow(90 + this.angle,90 + this.angle);
        this.fireEffect.getEmitters().first().getAngle().setHigh(45 + this.angle, 135 + this.angle);
    }

    private void playParticle(ParticleEffect particleEffect){
        if(particleEffect.isComplete())
            particleEffect.start();
    }

    @Override
    public void disappear() {}
}
