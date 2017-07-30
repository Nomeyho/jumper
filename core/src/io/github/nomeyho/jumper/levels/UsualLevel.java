package io.github.nomeyho.jumper.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.objects.AbstractGameObject;
import io.github.nomeyho.jumper.objects.Portal;
import io.github.nomeyho.jumper.utils.Utils;

import java.util.Iterator;

public class UsualLevel extends AbstractLevel {
    private static final float LEFT_LIMIT = Application.worldWidth - 2*Portal.WIDTH;
    private static final float RIGHT_LIMIT = 2*Portal.WIDTH;
    private float currentBellHeight = MIN_HEIGHT;
    private float deltaY;
    private float deltaX;
    private Pool<Portal> pool;

    public UsualLevel () { init(); }

    public void init () {
        super.init();
        this.deltaX = Application.CELL * 5;
        this.deltaY = Application.CELL * 3.5f;
        this.pool = new Pool<Portal>(10, 40) {
            @Override
            protected Portal newObject() {
                return new Portal(0, 0);
            }
        };
    }

    /**
     * Update the list of game objects based on the player location
     * @param delta
     * @param playerX world X
     * @param playerY world Y
     */
    public void update (float delta, float playerX, float playerY) {
        /* Update each game objects */
        for(int i=0, end=this.objects.size; i<end; ++i)
            this.objects.get(i).update(delta);

        /* Generate new portals */
        float minY = Math.max(playerY - Application.worldHeight, this.currentBellHeight);
        float maxY = playerY + Application.worldHeight;
        float y = minY;
        float x;
        int len;
        Portal portal;

        // Generate portals
        while (y < maxY) {
            // Get next Y
            y = getNextY(y);
            // Get next X
            len = this.objects.size;
            if(len > 0) {
                x = this.objects.get(len - 1).location.getX();
                x = getNextX(x);
            } else {
                // Don't start on the side
                x = Utils.randomFloat(Portal.WIDTH, Application.worldWidth - 2*Portal.WIDTH);
            }
            // Add new portals
            portal = this.pool.obtain();
            portal.init(x, y);
            this.objects.add(portal);
        }

        this.currentBellHeight = y;

        // Remove the bells for the low levels
        Iterator<AbstractGameObject> it = this.objects.iterator(); // controlled
        while(it.hasNext()) {
            AbstractGameObject go = it.next();
            if(go.location.getY() < (playerY - 1.5f*Application.worldHeight)) {
                this.pool.free((Portal)go);
                it.remove();
            }
        }
    }

    private float getNextX (float x) {
        /*
        // Left
        float left = 0;
        if(x > (deltaX + LEFT_LIMIT))
            left = LEFT_LIMIT;
        else
            left = deltaX + LEFT_LIMIT;

        // Right
        float right = 0;
        if(x < (RIGHT_LIMIT - deltaX))
            right = RIGHT_LIMIT;
        else
            right = RIGHT_LIMIT - deltaX;

        x = Utils.randomFloat(left, right);
        */

        float delta = Utils.randomFloat(-this.deltaX, this.deltaX);

        float tmp = x + delta;
        while(tmp < LEFT_LIMIT || tmp > RIGHT_LIMIT || Math.abs(x - tmp) < Application.CELL) {
            delta = Utils.randomFloat(-this.deltaX, this.deltaX);
            tmp = x + delta;
        }

        x = tmp;
        return x;
    }

    private float getNextY (float y) {
        return y + this.deltaY + Utils.randomFloat(-0.2f * this.deltaY, 0.2f * this.deltaY);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Draw each game object
        for(int i=0, end=this.objects.size; i<end; ++i)
            this.objects.get(i).drawBackground(batch);

        GameManager.get().player.draw(batch);

        for(int i=0, end=this.objects.size; i<end; ++i)
            this.objects.get(i).draw(batch);
    }

    @Override
    public void remove(AbstractGameObject go) {
        // TODO effect
        this.objects.removeValue(go, true);
    }
}
