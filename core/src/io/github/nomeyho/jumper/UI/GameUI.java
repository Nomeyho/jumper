package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.StringBuilder;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.files.PlayerStats;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.objects.Player;
import io.github.nomeyho.jumper.sound.SoundEnum;
import io.github.nomeyho.jumper.sound.SoundManager;
import io.github.nomeyho.jumper.utils.GameState;

public class GameUI implements ITranslatable {
    private static final int ICON_SIZE = 30;
    private Stage stage;
    private Label start;
    private Label fps;
    private Image scoreIcon;
    private Label score;
    private Image livesIcon;
    private Label lives;
    private ImageButton pauseIcon;
    private PauseMenu pauseMenu;
    private GameoverMenu gameoverMenu;
    private I18NBundle bundle = LanguageManager.get().getBundle();

    public GameUI () {
        LanguageManager.get().register(this);

        Skin skin = Application.get().assetManager.get(Application.SKIN);
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);

        this.stage = new Stage(GameManager.get().viewport, GameManager.get().batch);
        this.stage.setDebugAll(Application.DEBUG);

        this.start = new Label("", skin);
        this.start.setAlignment(Align.center);
        this.start.addAction(Actions.repeat(
                50,
                Actions.sequence(
                        Actions.fadeOut( 1.5f),
                        Actions.fadeIn(1.5f)
                )
        ));
        this.stage.addActor(this.start);

        this.fps = new Label("", skin, "small");
        this.stage.addActor(this.fps);

        this.scoreIcon = new Image(atlas.findRegion("score"));
        this.scoreIcon.setSize(ICON_SIZE, ICON_SIZE);
        this.stage.addActor(this.scoreIcon);

        this.score = new Label("", skin, "small");
        this.stage.addActor(this.score);

        this.livesIcon = new Image(atlas.findRegion("heart"));
        this.livesIcon.setSize(ICON_SIZE, ICON_SIZE);
        this.stage.addActor(this.livesIcon);

        this.lives = new Label("", skin, "small");
        this.stage.addActor(this.lives);

        this.pauseMenu = new PauseMenu("", skin);
        this.stage.addActor(this.pauseMenu);

        this.pauseIcon = new ImageButton(new TextureRegionDrawable(atlas.findRegion("pause")));
        this.pauseIcon.setSize(2 * ICON_SIZE, 2 * ICON_SIZE);
        this.pauseIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                SoundManager.get().playSound(SoundEnum.CLICK);
                GameManager.get().pause();
                pauseMenu.setVisible(true);
            }
        });
        this.stage.addActor(this.pauseIcon);

        this.gameoverMenu = new GameoverMenu("", skin);
        this.stage.addActor(this.gameoverMenu);

        Application.get().inputMultiplexer.addProcessor(this.stage);

        resize();
    }

    public void update (float delta) {
        this.stage.act(delta);

        // Update texts
        this.fps.setText("Fps: " + Gdx.graphics.getFramesPerSecond());
        this.score.setText(PlayerStats.get().currentScore + "");
        this.lives.setText(PlayerStats.get().remainingLifes + "");

        if(GameManager.get().state == GameState.READY)
            this.start.setText(this.bundle.get("touch_to_start"));
        else
            this.start.setText(null);
    }

    public void draw () {
        // Show game over
        if(GameManager.get().state == GameState.ENDED)
            this.gameoverMenu.show();

        this.stage.draw();
    }

    public void resize () {
        this.start.setPosition(Application.worldWidth / 2, 0.7f * Application.worldHeight);
        this.fps.setPosition(20, Application.worldHeight - 40);
        this.scoreIcon.setPosition(20, Application.worldHeight - 80 - ICON_SIZE/2);
        this.score.setPosition(60, Application.worldHeight - 80);
        this.livesIcon.setPosition(20, Application.worldHeight - 120 - ICON_SIZE/2);
        this.lives.setPosition(60, Application.worldHeight - 120);
        this.pauseIcon.setPosition(
                Application.worldWidth - 50 - ICON_SIZE,
                Application.worldHeight - 50 - ICON_SIZE
        );

        this.pauseMenu.setWidth(Application.worldWidth);
        this.pauseMenu.setHeight(Application.worldHeight);
    }

    @Override
    public void updateLang() {}
}
