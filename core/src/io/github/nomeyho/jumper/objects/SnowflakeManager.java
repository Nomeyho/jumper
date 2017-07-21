package io.github.nomeyho.jumper.objects;


import com.badlogic.gdx.utils.Array;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.utils.Utils;

import java.util.Random;

public class SnowflakeManager {

    private int snowflakesNumber = 75;
    private Array<Snowflake> snowflakes;
    public float counter = 0;

    public SnowflakeManager(){
        init();
    }

    public void init() {
        snowflakes  = new Array<Snowflake>();
        for(int i=1; i<= snowflakesNumber; i++) {
            float x = Utils.randomFloat(0, Application.worldWidth);
            // Ensure fall memory flakes positions.
            float y = Utils.randomFloat(- Application.worldHeight / 2 , Application.worldHeight);
            snowflakes.add(new Snowflake(x,y));
        }
    }

    public void update(float delta) {

        counter += delta;
        if(counter >= 2 * Application.PI * 100)
            counter = 0;

        for(int i=0; i < snowflakes.size; i++){
            Snowflake snowflake = snowflakes.get(i);
            snowflake.update(delta);
            float min_height = GameManager.get().camera.position.y
                                - Application.worldHeight - snowflake.getHeight() / 2;
            float max_height = GameManager.get().camera.position.y
                                + Application.worldHeight / 2 + snowflake.getHeight() / 2;
            // Reach bottom of screen repositionning at top.
            if(snowflake.location.getY() < min_height) {
                float x= Utils.randomFloat(0, Application.worldWidth);
                snowflake.location.setLocation(x, max_height);
                snowflake.init();
                snowflake.movementOriginX = x;
            }
            // Reach top of screen repositionning at bottom.
            else if(snowflake.location.getY() > max_height) {
                float x= Utils.randomFloat(0, Application.worldWidth);
                snowflake.location.setLocation(snowflake.location.getX(), min_height);
                snowflake.init();
                snowflake.movementOriginX = x;
            }
        }
    }

    public int getNumberOfSnowflakes(){
        return snowflakes.size;
    }

    public Snowflake getsnowflake(int index){
        return snowflakes.get(index);
    }

    public void resize(){
        init();
    }

}
