package io.github.nomeyho.jumper.objects;


import com.badlogic.gdx.utils.Array;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.utils.Utils;

public class SnowflakeManager {

    private int snowflakesNumber = 75;
    private Array<Snowflake> snowflakes;
    public float counter = 0;

    public SnowflakeManager(){
        init();
    }

    public void init() {
        this.snowflakes  = new Array<Snowflake>();
        for(int i=1; i<= this.snowflakesNumber; i++) {
            float x = Utils.randomFloat(0, Application.worldWidth);
            // Ensure flakes are still visible when player falls
            float y = Utils.randomFloat(- Application.worldHeight / 2 , Application.worldHeight);
            this.snowflakes.add(new Snowflake(x,y));
        }
    }

    public void update(float delta) {

        this.counter += delta;
        if(this.counter >= 2 * Application.PI * 100)
            this.counter = 0;

        for(int i=0; i < this.snowflakes.size; i++){
            Snowflake snowflake = this.snowflakes.get(i);
            snowflake.update(delta);
            float min_height = GameManager.get().camera.position.y
                                - Application.worldHeight - snowflake.getHeight() / 2;
            float max_height = GameManager.get().camera.position.y
                                + Application.worldHeight / 2 + snowflake.getHeight() / 2;
            // Reach bottom of screen repositioning at top.
            if(snowflake.location.getY() < min_height) {
                float x= Utils.randomFloat(0, Application.worldWidth);
                snowflake.location.setLocation(x, max_height);
                snowflake.init();
                snowflake.movementOriginX = x;
            }
            // Reach top of screen repositioning at bottom.
            else if(snowflake.location.getY() > max_height) {
                float x= Utils.randomFloat(0, Application.worldWidth);
                snowflake.location.setLocation(snowflake.location.getX(), min_height);
                snowflake.init();
                snowflake.movementOriginX = x;
            }
        }
    }

    public int getNumberOfSnowflakes(){
        return this.snowflakes.size;
    }

    public Snowflake getSnowflake(int index){
        return this.snowflakes.get(index);
    }

    public void resize(){
        init();
    }

}
