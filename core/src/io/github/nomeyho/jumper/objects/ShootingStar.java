package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.utils.Utils;

public class ShootingStar extends Star {

    private static final float MIN_SPEED = 1000; // in absolute value
    private static final float MAX_SPEED = 2000;
    private Interpolation interpolator = Interpolation.pow3In;
    private float finalSpeed;
    private float elapsed;
    private boolean visible;

    public ShootingStar(float y) {
        super(Application.worldWidth, y);
    }

    @Override
    public void init() {
        // Force X position
        this.location.setLocation(Application.worldWidth, this.location.getY());
        // Random size and opacity
        super.width = Utils.randomFloat(50, 300);
        super.height = Utils.randomFloat(2, 5);
        super.opacity = Utils.randomFloat(0, 1);
        // Random speed
        this.finalSpeed = - Utils.randomFloat(MIN_SPEED, MAX_SPEED);
        this.elapsed = 0;
        this.visible = Utils.randomBool();

        // Texture
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        super.starTexture = atlas.findRegion("shooting_star");
    }

    @Override
    public void update(float delta) {
        this.elapsed += delta;
        // progress = second elapse / seconds to travel
        float progress = -this.elapsed / (Application.worldWidth / this.finalSpeed);
        progress = Math.min(progress, 1);
        // base_speed + delta_speed * time
        this.speed.x = this.speed.y = -MIN_SPEED + interpolator.apply(progress) * (this.finalSpeed + MIN_SPEED);
        this.location.add(this.speed.x * delta, this.speed.y * delta);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(!this.visible) return;

        batch.draw(
                this.starTexture,
                this.location.getX(),
                this.location.getY(),
                0,
                0,
                width,
                height,
                1,
                1,
                45 // ROTATION (degree)
        );
    }
}
