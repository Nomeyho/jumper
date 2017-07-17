package io.github.nomeyho.jumper.levels;

import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.objects.AbstractGameObject;
import io.github.nomeyho.jumper.objects.Bell;
import io.github.nomeyho.jumper.objects.Player;

import java.util.Iterator;
import java.util.Random;

public class UsualLevel extends AbstractLevel {
    private float currentBellHeight = Player.HEIGHT;
    private Random rand = new Random();


    public UsualLevel () {
        super();
        this.objects.add( new Bell(200, 500, 0) );
        this.objects.add( new Bell(900, 1200, 0) );
    }

    /**
     * Update the list of game objects based on the player location
     * @param playerX world X
     * @param playerY world Y
     */
    public void update (float playerX, float playerY) {
        // Generate new bells
        int i = (int) currentBellHeight;
        while(i<Application.worldHeight) {
            float x = this.randomFloat(1, Application.worldWidth - Bell.WIDTH);
            this.objects.add(new Bell(x, i, 0));
            i += Bell.HEIGHT * 2;
        }
        this.currentBellHeight = i;

        // Remove the bells for the low levels
        Iterator<AbstractGameObject> it = this.objects.iterator();
        while(it.hasNext()) {
            AbstractGameObject go = it.next();
            if(go instanceof Bell && go.location.getY() < playerY)
                it.remove();
        }
    }

    private float randomFloat (float min, float max) {
        return this.rand.nextFloat() * max + min;
    }

    private int randomInt(int min, int max) {
        return this.rand.nextInt(max) + min;
    }
}
