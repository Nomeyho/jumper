package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;
import io.github.nomeyho.jumper.utils.AnimationWrapper;
import io.github.nomeyho.jumper.utils.ColorManager;
import io.github.nomeyho.jumper.utils.Utils;

public class Portal extends AbstractGameObject {
    public static final float WIDTH = 85;
    public static final float HEIGHT = 55;
    private static final float SPEED = 1;
    private static final int SCORE = 1;
    private TextureRegion frontTexture;
    private TextureRegion backTexture;
    private AnimationWrapper explosion;
    private Color color;

    public Portal(float x, float y) {
        TextureAtlas textureAtlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.frontTexture = textureAtlas.findRegion("portal_front");
        this.backTexture = textureAtlas.findRegion("portal_back");

        HitboxAtlas hitboxAtlas = Application.get().assetManager.get(Application.HITBOX_ATLAS);
        this.hitbox = hitboxAtlas.get("bell");

        // TODO CHANGE ATLAS
        this.explosion = new AnimationWrapper(0.33f, "bell_explosion", Application.TEXTURE_ATLAS);

        init(x, y);
    }

    @Override
    public void init(float x, float y){
        this.location.setLocation(x, y);
        this.speed.set(0, SPEED, 0);
        this.color = ColorManager.get().getColor(y);
    }

    @Override
    public int getScore() {
        return SCORE;
    }

    @Override
    public void update(float delta) {
        this.location.add(0, - SPEED * delta);
        this.updateHitbox(WIDTH, HEIGHT, this.location.getX(), this.location.getY());
        this.explosion.update(delta);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setColor(color);
       batch.draw(this.frontTexture, this.location.getX(), this.location.getY(), WIDTH, HEIGHT);
        batch.setColor(1,1,1,1);
      //  this.explosion.draw(batch, this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }

    @Override
    public void drawBackground(SpriteBatch batch) {
        batch.setColor(color);
        batch.draw(this.backTexture, this.location.getX(), this.location.getY(), WIDTH, HEIGHT);
        batch.setColor(1,1,1,1);
        //  this.explosion.draw(batch, this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }
}
