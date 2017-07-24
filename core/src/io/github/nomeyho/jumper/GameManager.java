package io.github.nomeyho.jumper;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.UI.FpsCounter;
import io.github.nomeyho.jumper.levels.AbstractLevel;
import io.github.nomeyho.jumper.levels.UsualLevel;
import io.github.nomeyho.jumper.objects.*;
import io.github.nomeyho.jumper.sound.SoundEnum;
import io.github.nomeyho.jumper.sound.SoundManager;

public class GameManager {
    private static GameManager INSTANCE = new GameManager();

    public Player player;
    public AbstractLevel level;
    public Camera camera;
    public Camera guiCamera;
    private FpsCounter fpscounter;
    public InputController inputController;
    public static boolean GAME_STARTING = false;
    public StarManager starManager;

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
        this.starManager = new StarManager();
    }

    public void update (float delta) {
        this.player.update(delta);
        this.level.update(delta, this.player.location.getX(), this.player.location.getY());
        this.starManager.update(delta);
        checkForCollision();
    }

    public void draw (SpriteBatch batch) {
        // Draw layer per layer
        for(int layer = Application.MIN_LAYER; layer <= Application.MAX_LAYER; ++layer) {
            // Draw player
            if (this.player.location.getLayer() == layer)
                this.player.draw(batch);
            // Draw objects
            this.level.draw(batch, layer);
            this.starManager.draw(batch, layer);
        }
    }

    public void drawUI (SpriteBatch batch) {
        this.fpscounter.draw(batch);
    }

    private void checkForCollision(){
        AbstractGameObject go;
        for(int i=0, end=this.level.objects.size; i<end; ++i) {
            go = this.level.objects.get(i);
            if(this.player.hitbox.overlap(go.hitbox)) {
                SoundManager.get().playSound(SoundEnum.TINK);
                //  this.player.speed.y = 2500;
            }
        }
    }
}
