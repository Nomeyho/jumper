package io.github.nomeyho.jumper;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.nomeyho.jumper.UI.GameUI;
import io.github.nomeyho.jumper.UI.GameBackground;
import io.github.nomeyho.jumper.UI.ScoreLabel;
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
    private Pool<ScoreLabel> labelPool;
    private Array<ScoreLabel> labels = new Array<ScoreLabel>();

    private GameState previousState = null;
    private boolean savedStats;

    private GameManager() {
        final Skin skin = Application.get().assetManager.get(Application.SKIN);
        this.labelPool = new Pool<ScoreLabel>(2, 10) {
            @Override
            protected ScoreLabel newObject() {
                return new ScoreLabel("", skin);
            }
        };
    }

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

        for(int i=0; i<this.labels.size; ++i)
            this.labelPool.free(this.labels.get(i));
        this.labels.clear();

        ColorManager.get().shuffle();
    }

    public void update (float delta) {
        if(this.state == GameState.PAUSED)
            return;

        for(ScoreLabel label: this.labels) { // yeah, but controlled
            if(label.toRemove)
                this.labels.removeValue(label, true);
            else
                label.act(delta);
        }

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

        for(int i=0, end=this.labels.size; i<end; ++i)
            this.labels.get(i).draw(this.batch, 1);
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

    private void checkForCollision() {
        for(AbstractGameObject go: this.level.objects) { // yeah warning, but controlled
            if(this.player.hitbox.overlap(go.hitbox)) {
                SoundManager.get().playSound(SoundEnum.TINK);
                this.player.setSpeed(1000);
                PlayerStats.get().currentScore += go.getScore();
                this.level.disappear(go);
                showScore(
                        go.location.getX() + go.getWidth()/2,
                        go.location.getY() + go.getHeight(),
                        "" + go.getScore()
                );
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

        starManager.init();
        ColorManager.get().shuffle();
        this.player.init(Application.worldWidth / 2 - Player.WIDTH/2, Player.MIN_Y);
        // TODO
        this.level = new UsualLevel();
    }

    public void showScore (float x, float y, String score) {
        ScoreLabel label = this.labelPool.obtain();
        Vector3 pos = this.camera.project(new Vector3(x, y, 0));
        // label.init(pos.x, pos.y, score);
        // label.init(pos.x, pos.y, score);
        label.init(x, y, "1");
        this.labels.add(label);
    }
}
