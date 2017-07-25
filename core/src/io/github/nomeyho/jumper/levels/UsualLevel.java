package io.github.nomeyho.jumper.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.objects.AbstractGameObject;
import io.github.nomeyho.jumper.objects.Bell;
import io.github.nomeyho.jumper.objects.Player;
import io.github.nomeyho.jumper.utils.Utils;

import java.util.Iterator;
import java.util.Random;

public class UsualLevel extends AbstractLevel {
    private float currentBellHeight = Player.HEIGHT;


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
            float x = Utils.randomFloat(1, Application.worldWidth - Bell.WIDTH);
            this.objects.add(new Bell(x, i, 0));
            i += Bell.HEIGHT * 2;
        }
        this.currentBellHeight = i;

        // Remove the bells for the low levels
        Iterator<AbstractGameObject> it = this.objects.iterator(); // TODO
        while(it.hasNext()) {
            AbstractGameObject go = it.next();
            if(go instanceof Bell && go.location.getY() < playerY)
                it.remove();
        }
    }

    @Override
    public void draw(SpriteBatch batch, int layer) {
        // Draw each game object
        AbstractGameObject go;
        for(int i=0, end=this.objects.size; i<end; ++i) {
            go = this.objects.get(i);
            if(go.location.getLayer() == layer)
                go.draw(batch);
        }
    }

}
