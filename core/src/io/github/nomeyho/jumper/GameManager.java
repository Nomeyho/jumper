package io.github.nomeyho.jumper;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.UI.FpsCounter;
import io.github.nomeyho.jumper.levels.AbstractLevel;
import io.github.nomeyho.jumper.levels.UsualLevel;
import io.github.nomeyho.jumper.objects.AbstractGameObject;
import io.github.nomeyho.jumper.objects.Player;
import io.github.nomeyho.jumper.objects.Snowflake;

public class GameManager {
    private static GameManager INSTANCE = new GameManager();

    public Player player;
    public AbstractLevel level;
    public Camera camera;
    public Camera guiCamera;
    private FpsCounter fpscounter;
    private InputController inputController;
    public static boolean GAME_STARTING = false;
    public Snowflake snowflake;

    private GameManager() {}

    public static GameManager get() {
        return INSTANCE;
    }

    /**
     * MUST BE CALLED at GameScreen CREATION !
     * @param camera
     * @param guiCamera
     */
    public void init (Camera camera, Camera guiCamera) {
        this.player = new Player(Application.worldWidth / 2, 0, 0);
        this.level = new UsualLevel();
        this.camera = camera;
        this.guiCamera = guiCamera;
        this.fpscounter = new FpsCounter();
        this.inputController = new InputController();
        this.snowflake = new Snowflake(Application.worldWidth / 2, Application.worldHeight / 2, 0);
    }

    public void update (float delta) {
        this.player.update(delta);
        this.level.update(this.player.location.getX(), this.player.location.getY());
        for(AbstractGameObject go: this.level.objects)
            go.update(delta);
        checkForCollision();
    }

    public void draw (SpriteBatch batch) {
        this.snowflake.draw(batch);
        // Draw layer per layer
        for(int layer = Application.MIN_LAYER; layer < Application.MAX_LAYER; ++layer) {
            // Draw player
            if (this.player.location.getLayer() == layer)
                this.player.draw(batch);
            // Draw objects
            for(AbstractGameObject go: this.level.objects) {
                if(go.location.getLayer() == layer)
                    go.draw(batch);
            }
        }
    }

    public void drawUI (SpriteBatch batch) {
        this.fpscounter.draw(batch);
    }

    public void checkForCollision(){
        for(AbstractGameObject go: this.level.objects) {
           // if(this.player.hitbox.overlap(go.hitbox))
            //    this.player.speed.y += 2000;
        }
    }
}
