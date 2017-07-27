package io.github.nomeyho.jumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.nomeyho.jumper.UI.GameUI;
import io.github.nomeyho.jumper.UI.GameBackground;
import io.github.nomeyho.jumper.levels.AbstractLevel;
import io.github.nomeyho.jumper.levels.UsualLevel;
import io.github.nomeyho.jumper.objects.*;
import io.github.nomeyho.jumper.sound.SoundEnum;
import io.github.nomeyho.jumper.sound.SoundManager;

public class GameManager {
    private static GameManager INSTANCE = new GameManager();

    public Player player;
    public AbstractLevel level;
    public Game game;
    public Viewport viewport;
    public Camera camera;
    public Camera guiCamera;
    public GameUI gameUI;
    public InputController inputController;
    public static boolean GAME_STARTING = false;
    public StarManager starManager;
    public GameBackground background;

    private GameManager() {}

    public static GameManager get() {
        return INSTANCE;
    }

    /**
     * MUST BE CALLED at GameScreen CREATION !
     */
    public void init (Game game, Viewport viewport, Camera camera, Camera guiCamera) {
        this.player = new Player(Application.worldWidth / 2 - Player.WIDTH/2, Player.MIN_Y, 0);
        this.level = new UsualLevel();
        this.game = game;
        this.viewport = viewport;
        this.camera = camera;
        this.guiCamera = guiCamera;
        this.gameUI = new GameUI();

        Application.get().inputMultiplexer.removeProcessor(this.inputController);
        this.inputController = new InputController();

        this.starManager = new StarManager();
        this.background = new GameBackground();
    }

    public void update (float delta) {
        this.player.update(delta);
        this.level.update(delta, this.player.location.getX(), this.player.location.getY());
        this.starManager.update(delta);
        this.background.update(delta);
        this.gameUI.update(delta);
        checkForCollision();
    }

    public void draw (SpriteBatch batch) {
        this.starManager.draw(batch);
        this.background.draw(batch);

        // Draw layer per layer
        for(int layer = Application.MIN_LAYER; layer <= Application.MAX_LAYER; ++layer) {
            // Draw objects
            this.level.draw(batch, layer);
            // Draw player
            if (this.player.location.getLayer() == layer)
                this.player.draw(batch);
        }
    }

    /**
     * Use a separated draw method because the batch is bound to the UI viewport
     * this time (even if both have the same size, the camera is different).
     */
    public void drawUI (SpriteBatch batch) {
        this.gameUI.draw(batch);
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

    public void resume () {
        // TODO
    }

    public void restart () {
        // TODO
    }

    public void pause () {
        // TODO
    }
}
