package io.github.nomeyho.jumper.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.objects.AbstractGameObject;
import io.github.nomeyho.jumper.objects.Portal;
import io.github.nomeyho.jumper.objects.Player;
import io.github.nomeyho.jumper.utils.Utils;

import java.util.Iterator;

public class UsualLevel extends AbstractLevel {
    private float currentBellHeight = MIN_HEIGHT;


    public UsualLevel () {
        super();
    }

    /**
     * Update the list of game objects based on the player location
     * @param delta
     * @param playerX world X
     * @param playerY world Y
     */
    public void update (float delta, float playerX, float playerY) {
        // Update each game objects
        for(int i=0, end=this.objects.size; i<end; ++i)
            this.objects.get(i).update(delta);

        // Generate new bells
        int i = (int) currentBellHeight;
        while(i<playerY + Application.worldHeight) {
            float x = Utils.randomFloat(1, Application.worldWidth - Portal.WIDTH);
            this.objects.add(new Portal(x, i));
            i += Portal.HEIGHT * 2;
        }
        this.currentBellHeight = i;

        // Remove the bells for the low levels
        /*
        Iterator<AbstractGameObject> it = this.objects.iterator(); // TODO
        while(it.hasNext()) {
            AbstractGameObject go = it.next();
            if(go instanceof Portal && go.location.getY() < playerY)
                it.remove();
        }
        */
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



        this.objects.removeValue(go, true);
    }

}
