package io.github.nomeyho.jumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.nomeyho.jumper.UI.GameUI;
import io.github.nomeyho.jumper.UI.GameBackground;
import io.github.nomeyho.jumper.files.PlayerStats;
import io.github.nomeyho.jumper.levels.AbstractLevel;
import io.github.nomeyho.jumper.levels.UsualLevel;
import io.github.nomeyho.jumper.objects.*;
import io.github.nomeyho.jumper.sound.SoundEnum;
import io.github.nomeyho.jumper.sound.SoundManager;
import io.github.nomeyho.jumper.utils.ColorManager;
import io.github.nomeyho.jumper.utils.GameState;

public class GameManager {
    private static GameManager INSTANCE = new GameManager();

    public GameState state = GameState.READY;
    public Player player;
    public AbstractLevel level;
    public AbstractGame game;
    public Viewport viewport;
    public Camera camera;
    public Camera guiCamera;
    public GameUI gameUI;
    public InputController inputController;
    public StarManager starManager;
    public GameBackground background;
    public SpriteBatch batch;

    private GameState previousState = null;
    private boolean savedStats;

    private GameManager() {}

    public static GameManager get() {
        return INSTANCE;
    }

    /**
     * MUST BE CALLED at GameScreen CREATION !
     */
    public void init (AbstractGame game, Viewport viewport, Camera camera, Camera guiCamera, SpriteBatch batch) {
        this.player = new Player(Application.worldWidth / 2 - Player.WIDTH/2, Player.MIN_Y);
        this.level = new UsualLevel();
        this.game = game;
        this.viewport = viewport;
        this.camera = camera;
        this.guiCamera = guiCamera;
        this.batch = batch;
        this.gameUI = new GameUI();
        this.inputController = new InputController();
        this.starManager = new StarManager();
        this.background = new GameBackground();
        this.state = GameState.READY;
        this.savedStats = false;
        ColorManager.get().shuffle();
    }

    public void update (float delta) {
        if(this.state == GameState.PAUSED)
            return;

        this.player.update(delta);
        this.level.update(delta, this.player.location.getX(), this.player.location.getY());
        this.starManager.update(delta);
        this.background.update(delta);

        if(this.state == GameState.ENDED && !this.savedStats) {
            // Save stats (but only once)
            PlayerStats stats = PlayerStats.get();
            stats.bestScore = Math.max(stats.bestScore, stats.currentScore);
            stats.decreaseLifes();
            stats.save();
            this.savedStats = true;
        }

        this.gameUI.update(delta);
        checkForCollision();
    }

    public void draw () {
        this.viewport.setCamera(this.camera);
        this.batch.setProjectionMatrix(this.camera.projection);
        this.batch.setTransformMatrix(this.camera.view);
        this.viewport.apply(true);

        this.starManager.draw(batch);
        this.background.draw(batch);

        this.level.draw(batch);
    }

    /**
     * Use a separated draw method because the batch is bound to the UI viewport
     * this time (even if both have the same size, the camera is different).
     */
    public void drawUI () {
        this.viewport.setCamera(this.guiCamera);
        this.batch.setProjectionMatrix(this.guiCamera.projection);
        this.batch.setTransformMatrix(this.guiCamera.view);
        this.viewport.apply(true);

        this.gameUI.draw();
    }

    private void checkForCollision(){
        AbstractGameObject go;
        for(int i=0, end=this.level.objects.size; i<end; ++i) {
            go = this.level.objects.get(i);
            if(this.player.hitbox.overlap(go.hitbox)) {
                SoundManager.get().playSound(SoundEnum.TINK);
                //  this.player.speed.y = 2500;
                PlayerStats.get().currentScore += 1; // TODO generalize with a valuator
            }
        }
    }

    public void pause () {
        this.previousState = this.state;
        this.state = GameState.PAUSED;
    }

    public void resume () {
        this.state = this.previousState;
    }

    public void restart (boolean removeLive) {
        this.previousState = null;
        this.savedStats = false;
        this.state = GameState.READY;

        if(removeLive) {
            PlayerStats.get().decreaseLifes();
            PlayerStats.get().save();
        }

        ColorManager.get().shuffle();
        this.player.init(Application.worldWidth / 2 - Player.WIDTH/2, Player.MIN_Y);
        // TODO
        this.level = new UsualLevel();
    }
}
