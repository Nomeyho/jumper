package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.utils.Utils;

public class StarManager {
    // Keep a constant number of stars on memory
    // But a varying number of them are actually visible (location randomness)
    private static final int NUMBER_STARS = 110;
    private static final int NUMBER_SHOOTING_STARS = 2;
    private Array<Star> stars;

    public StarManager () {
        init();
    }

    public void init () {
        // Stars
        this.stars = new Array<Star>();
        Star star;
        for(int i=1; i<= NUMBER_STARS; ++i) {
            float x = Utils.randomFloat(0, Application.worldWidth);
            // Ensure stars are still visible when player falls
            float y = Utils.randomFloat(- Application.worldHeight, Application.worldHeight);
            star = new Star(x,y);
            star.init();
            this.stars.add(star);
        }

        // Shooting stars
        for(int i=1; i<= NUMBER_SHOOTING_STARS; ++i) {
            float y = Utils.randomFloat(- Application.worldHeight, Application.worldHeight);
            ShootingStar shootingStar = new ShootingStar(y);
            shootingStar.init();
            this.stars.add(shootingStar);
        }
    }

    public void update (float delta) {
        Star star;
        for(int i=0, end = this.stars.size; i < end; ++ i) {
            // Update each star
            star = this.stars.get(i);
            star.update(delta);
            // Compute limits
            float minHeight = GameManager.get().player.location.getY() - Application.worldHeight;
            float maxHeight = GameManager.get().player.location.getY() + Application.worldHeight;
            // Below or above
            if(star.location.getY() < minHeight) {
                float x = Utils.randomFloat(0, Application.worldWidth);
                float y = GameManager.get().player.location.getY() + Application.worldHeight;
                star.location.setLocation(x, y);
                star.init();
            }
            else if( star.location.getY() > maxHeight) {
                float x = Utils.randomFloat(0, Application.worldWidth);
                float y = GameManager.get().player.location.getY() - Application.worldHeight;
                star.location.setLocation(x, y);
                star.init();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for(int i=0, end = this.stars.size; i<end; ++i) {
            this.stars.get(i).draw(batch);
        }
    }

    public void resize () {
        // Yeah we loose and recreate everything
        init();
    }
}
