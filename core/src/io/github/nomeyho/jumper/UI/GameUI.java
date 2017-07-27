package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;

public class GameUI {
    private static final int ICON_SIZE = 30;
    private Stage stage;
    private Label fps;
    private Image scoreIcon;
    private Label score;
    private Image livesIcon;
    private Label lives;
    private ImageButton pauseIcon;
    private PauseMenu pauseMenu;

    public GameUI () {
        Skin skin = Application.get().assetManager.get(Application.SKIN);
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);

        this.stage = new Stage();
        this.stage.setViewport(GameManager.get().viewport);

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

        this.pauseIcon = new ImageButton(new TextureRegionDrawable(atlas.findRegion("pause")));
        this.pauseIcon.setSize(2 * ICON_SIZE, 2 * ICON_SIZE);
        this.pauseIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                pauseMenu.setVisible(true);
            }
        });
        this.stage.addActor(this.pauseIcon);

        this.pauseMenu = new PauseMenu("", skin);
        this.stage.addActor(this.pauseMenu);

        Application.get().inputMultiplexer.addProcessor(this.stage);

        resize();
    }

    public void update (float delta) {
        this.stage.act(delta);
    }

    public void draw (SpriteBatch batch) {
        // Update texts
        this.fps.setText("Fps: " + Gdx.graphics.getFramesPerSecond());
        this.score.setText("123");
        this.lives.setText("8");

        this.stage.draw();
    }

    public void resize () {
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
}
