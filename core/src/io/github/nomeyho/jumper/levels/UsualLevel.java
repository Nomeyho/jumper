package io.github.nomeyho.jumper.levels;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import io.github.nomeyho.jumper.AbstractGame;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.objects.AbstractGameObject;
import io.github.nomeyho.jumper.objects.Portal;
import io.github.nomeyho.jumper.objects.RainbowPortal;
import io.github.nomeyho.jumper.utils.PlayerEnum;
import io.github.nomeyho.jumper.utils.Utils;

import java.util.Iterator;

public class UsualLevel extends AbstractLevel {
    private static final float RIGHT_LIMIT = Application.worldWidth - 1*Portal.WIDTH;
    private static final float LEFT_LIMIT = 1*Portal.WIDTH;
    private float currentBellHeight = MIN_HEIGHT;
    private float deltaY;
    private float deltaXMax, deltaXMin;
    private Pool<Portal> pool;
    private static final float rainbowProbability = 0.02f;

    public UsualLevel () { init(); }

    public void init () {
        super.init();
        this.deltaXMax = Application.CELL * 3;
        this.deltaXMin = Application.CELL/3f;
        this.deltaY = Application.CELL * 1.5f;
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
        clearDisappeared();

        /* Update each game objects */
        for(int i=0, end=this.objects.size; i<end; ++i)
            this.objects.get(i).update(delta);

        /* Generate new portals */
        float minY = Math.max(playerY - Application.worldHeight, this.currentBellHeight);
        float maxY = playerY + Application.worldHeight;
        float y = minY;
        float x;
        float random;
        Portal portal;
        Portal lastPortal;

        // Generate portals
        while (y < maxY) {
            // Get next Y
            y = getNextY(y);
            // Get next X
            lastPortal = getLastPortal();
            if(lastPortal == null){
                // Don't start on the side
                x = Utils.randomFloat(Portal.WIDTH, Application.worldWidth - 2*Portal.WIDTH);
            }
            else{
                x = lastPortal.location.getX();
                x = getNextX(x, this.deltaXMin, this.deltaXMax);
            }
            // Add new portals
            portal = this.pool.obtain();
            portal.init(x, y);
            this.objects.add(portal);
            // Rainbow
            random = Utils.randomFloat(0,1);

            if(random < rainbowProbability && y > Application.worldHeight){
                x = getNextX(x, 4*Application.CELL, 7*Application.CELL);
                this.objects.add(new RainbowPortal(x,y + Utils.randomFloat(-Application.CELL/2, Application.CELL/2)));
            }
        }

        this.currentBellHeight = y;

        // Remove the bells for the low levels
        Iterator<AbstractGameObject> it = this.objects.iterator(); // controlled
        while(it.hasNext()) {
            AbstractGameObject go = it.next();
            if(go.location.getY() < (playerY - 0.7f*Application.worldHeight)) {
                if(go.isPooled)
                    this.pool.free((Portal)go);
                it.remove();
            }
            //
            if(go.location.getY() < MIN_HEIGHT && GameManager.get().player.state == PlayerEnum.WAITING) {
                go.location.setLocation(Utils.randomFloat(Portal.WIDTH, Application.worldWidth - 2*Portal.WIDTH), maxY);
                go.init(go.location.getX(), go.location.getY());
            }
        }

        if(y > 1){
            deltaXMax = 6*Application.CELL;
            deltaY = 2f*Application.CELL;
        }
        /*if(y > 2000 && y < 4000){
            deltaXMax = 4*Application.CELL;
            deltaY = 1.65f*Application.CELL;
        }*/
    }

    private float getNextX (float x, float min, float max) {
        float delta = Utils.randomFloat(-max, max);
        float tmp = x + delta;


        while(tmp < LEFT_LIMIT || tmp > RIGHT_LIMIT || Math.abs(delta) < min) {
            delta = Utils.randomFloat(-max, max);
            tmp = x + delta;
        }
        return tmp;
        /*
        // Left
        float left;
        if(x > (deltaX + LEFT_LIMIT))
            left = LEFT_LIMIT;
        else
            left = deltaX + LEFT_LIMIT;

        // Right
        float right;
        if(x < (RIGHT_LIMIT - deltaX))
            right = RIGHT_LIMIT;
        else
            right = RIGHT_LIMIT - deltaX;

        System.out.println("limits" + left + " - " + right);

       return Utils.randomFloat(left, right);
       */
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
    public void disappear(AbstractGameObject go) {
        go.disappear();
    }

    private void clearDisappeared () {
        Portal portal;
        for(AbstractGameObject go: this.objects) {
            if(go instanceof Portal) {
                portal = (Portal) go;
                if(portal.toRemove) {
                    this.objects.removeValue(go, true);
                    if(portal.isPooled)
                        this.pool.free(portal);
                }
            }
        }
    }

    private Portal getLastPortal(){
        for(int i=this.objects.size-1; i >= 0; --i) {
            if(!(this.objects.get(i) instanceof RainbowPortal))
                return (Portal) this.objects.get(i);
        }
        return null;
    }
}
