package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.utils.Utils;

public class StarManager {
    // Keep a constant number of stars on memory
    // But a varying number of them are actually visible (location randomness)
    private static final int NUMBER = 75;
    private Array<Star> stars;

    public StarManager () {
        init();
    }

    public void init () {
        this.stars = new Array<Star>();
        for(int i=1; i<= NUMBER; i++) {
            float x = Utils.randomFloat(0, Application.worldWidth);
            // Ensure stars are still visible when player falls
            float y = Utils.randomFloat(- Application.worldHeight / 2 , Application.worldHeight);
            this.stars.add(new Star(x,y));
        }
    }

    public void update (float delta) {
        Star star;
        for(int i=0, end = this.stars.size; i < end; ++ i) {
            // Update each star
            star = this.stars.get(i);
            star.update(delta);
            // Compute limits
            float minHeight = GameManager.get().camera.position.y
                    - Application.worldHeight - star.getHeight() / 2;
            float maxHeight = GameManager.get().camera.position.y
                    + Application.worldHeight / 2 + star.getHeight() / 2;
            // Below or above
            if(star.location.getY() < minHeight || star.location.getY() > maxHeight) {
                float x = Utils.randomFloat(0, Application.worldWidth);
                float y = Utils.randomFloat(- Application.worldHeight / 2 , Application.worldHeight);
                star.location.setLocation(x, y);
                star.init();
            }
        }
    }

    public void draw(SpriteBatch batch, int layer) {
        Star star;
        for(int i=0; i<this.stars.size; ++i) {
            star = this.stars.get(i);
            if(star.location.getLayer() == layer)
                star.draw(batch);
        }
    }

    public void resize () {
        // Yeah we loose and recreate everything
        init();
    }
}
