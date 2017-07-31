package io.github.nomeyho.jumper.objects;


import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Pool;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;
import io.github.nomeyho.jumper.particles.ParticleEnum;
import io.github.nomeyho.jumper.particles.ParticleManager;
import io.github.nomeyho.jumper.utils.ColorManager;

public class RainbowPortal extends Portal{

        private static final int IMPULSE = 2600;
        private static final int SCORE = 100;

        public RainbowPortal(float x, float y){
            super(x,y,"portal_rainbow");
            super.isPooled = false;
        }


        @Override
        public int getScore() {
            return SCORE;
        }

        @Override
        public void draw(SpriteBatch batch) {
            batch.draw(
                    this.frontTexture,
                    this.location.getX(),
                    this.location.getY(),
                    WIDTH/2,
                    HEIGHT/2,
                    WIDTH,
                    HEIGHT,
                    this.scale,
                    this.scale,
                    0
            );
        }

        @Override
        public void drawBackground(SpriteBatch batch) {
            // TODO this.dustEffect.draw(batch);
            batch.draw(
                    this.backTexture,
                    this.location.getX(),
                    this.location.getY(),
                    WIDTH/2,
                    HEIGHT/2,
                    WIDTH,
                    HEIGHT,
                    this.scale,
                    this.scale,
                    0
            );
        }

        @Override
        public void disappear () {
            this.disappear = true;
            this.scale = 1;
        }

        @Override
        public float getImpulse(){
            return IMPULSE;
        }
}
