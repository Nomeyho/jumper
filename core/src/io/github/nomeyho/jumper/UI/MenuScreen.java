package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.nomeyho.jumper.*;
import io.github.nomeyho.jumper.files.PlayerStats;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.sound.SoundEnum;
import io.github.nomeyho.jumper.sound.SoundManager;
import io.github.nomeyho.jumper.utils.Utils;


public class MenuScreen extends AbstractGameScreen implements ITranslatable {
    public static final float SIZE = Application.SIZE;
    private static final int ICON_SIZE = 30;
    private static float FADE_IN_DURATION = 0.05f;
    private static float FADE_OUT_DURATION = 0.15f;
    // View
    private ExtendViewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    // UI
    private Stage stage;
    private Table layout;
    private Label logo;
    private Label tooltip;
    private TextButton playBtn;
    private TextButton buyBtn;
    private TextButton settingsBtn;
    private SettingsMenu settingsMenu;
    public MenuBackground background;
    private float time = 0;

    private Image scoreIcon;
    private Label score;
    private Image livesIcon;
    private Label lives;
    private Label nextLives;
    private Label countdown;

    private I18NBundle bundle;

    public MenuScreen(AbstractGame game) {
        super(game);
        this.batch = new SpriteBatch();
        this.background = new MenuBackground();
        LanguageManager.get().register(this);
    }

    @Override
    public void resize(int width, int height) {
        // Viewport
        this.viewport.update(width, height);
        Application.worldHeight = this.viewport.getWorldHeight();
        Application.worldWidth = this.viewport.getWorldWidth();

        // Camera
        this.camera.position.set(this.camera.viewportWidth / 2,this.camera.viewportHeight /2,0);
        this.camera.update();

        // Shapes
        Application.get().shapeRenderer.setProjectionMatrix(this.camera.combined);
        Application.get().shapeRenderer.updateMatrices();

        this.settingsMenu.setSize(this.viewport.getWorldWidth(), this.viewport.getWorldHeight());
        this.scoreIcon.setPosition(20, Application.worldHeight - 40 - ICON_SIZE/2);
        this.score.setPosition(60, Application.worldHeight - 40);
        this.livesIcon.setPosition(20, Application.worldHeight - 80 - ICON_SIZE/2);
        this.lives.setPosition(60, Application.worldHeight - 80);

        this.background.resize();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this.stage;
    }

    @Override
    public void show() {
        LanguageManager.get().register(this);

        // View
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(SIZE, SIZE, this.camera);
        // Layout & stage
        this.stage = new Stage(this.viewport, this.batch);
        this.layout = new Table();
        this.stage.addActor(this.layout);
        this.layout.setFillParent(true);
        this.layout.align(Align.center);
        this.stage.setDebugAll(Application.DEBUG);
        // Fade in effect
        this.stage.getRoot().getColor().a = 0;
        this.stage.getRoot().addAction(Actions.fadeIn(FADE_IN_DURATION));

        Skin skin = Application.get().assetManager.get(Application.SKIN);
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);

        // Logo
        this.logo = new Label(Application.TAG, skin, "default");
        this.logo.setFontScale(2);
        this.layout.add(this.logo).padBottom(80);
        this.layout.row();

        // Next lives
        this.nextLives = new Label("", skin, "small");
        this.layout.add(this.nextLives);
        this.layout.row();
        this.countdown = new Label("", skin);
        this.layout.add(this.countdown).padBottom(50);
        this.layout.row();

        this.tooltip = new Label("", skin);
        this.layout.add(this.tooltip).padBottom(50);
        this.layout.row();

        // Play
        this.playBtn = new TextButton("", skin);
        this.playBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                // Prevent playing without lives
                if(PlayerStats.get().remainingLifes <= 0) {
                    tooltip.setText(bundle.get("no_more_lives"));
                    tooltip.addAction(Actions.sequence(
                            Actions.color(Color.RED),
                            Actions.fadeIn(1f),
                            Actions.fadeOut(1f)
                    ));
                    return;
                }
                SoundManager.get().playSound(SoundEnum.CLICK);
                // Fade out effect
                stage.addAction(Actions.sequence(
                        Actions.fadeOut(FADE_OUT_DURATION),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                Application.get().inputMultiplexer.clear();
                                game.setScreen(new GameScreen(game));
                            }
                        })
                ));
                // -1 live as soon as the game starts
                PlayerStats.get().decreaseLifes();
            }
        });
        this.playBtn.getLabelCell().padBottom(7).padTop(7);
        this.layout.add(this.playBtn).padBottom(100);
        this.layout.row();

        // Buy
        this.buyBtn = new TextButton("", skin);
        this.buyBtn.getLabelCell().padBottom(7).padTop(7);
        this.buyBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                // Add service might not exist (e.g. on desktop)
                if(game.service == null || game.service.isLoading())
                    return;
                // Open new pub
                game.service.openAdd();
            }
        });
        this.layout.add(this.buyBtn).padBottom(100);
        this.layout.row();

        // Settings
        this.settingsBtn = new TextButton("", skin);
        this.settingsBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                SoundManager.get().playSound(SoundEnum.CLICK);
                settingsMenu.show(stage);
            }
        });
        this.settingsBtn.getLabelCell().padBottom(7).padTop(7);
        this.layout.add(this.settingsBtn).padBottom(100);
        this.settingsMenu = new SettingsMenu("", skin, this.background);

        // Life & score
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

        // Lang
        updateLang();

        // Start taking input from the UI
        Application.get().inputMultiplexer.addProcessor(this.stage);

        this.layout.invalidate();
        this.layout.invalidateHierarchy();
    }

    @Override
    public void hide() {
        this.stage.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void render(float delta) {
        this.stage.act(delta);
        update(delta);
        clearScreen();
        draw();
    }

    private void update(float delta) {
        this.time += delta;

        // Update texts
        this.score.setText(PlayerStats.get().currentScore + "");
        this.lives.setText(PlayerStats.get().remainingLifes + "");
        this.countdown.setText(PlayerStats.get().getCountdown(this.bundle));

        // Update buttons
        this.playBtn.setDisabled(PlayerStats.get().remainingLifes <= 0);
        if(this.game.service == null || this.game.service.isLoading()) {
            this.buyBtn.setDisabled(true);
            this.buyBtn.setText("");
            this.buyBtn.setText(getLoadingText());
        } else {
            this.buyBtn.setDisabled(false);
            this.buyBtn.setText(this.bundle.get("buy"));
        }

        this.background.update(delta);
        this.stage.act(delta);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(27 / 250f, 33 / 255f, 40 / 255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT) ;
    }

    private void draw() {
        // Draw the grid
        if(Application.DEBUG)
            this.drawGrid();

        // Background
        this.batch.begin();
        this.background.draw(this.batch);
        this.batch.end();

        // Show the screen content
        this.stage.draw();
    }

    private void drawGrid () {
        ShapeRenderer shapeRenderer = Application.get().shapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < this.viewport.getWorldWidth(); x += Application.CELL) {
            for (int y = 0; y < this.viewport.getWorldHeight(); y += Application.CELL) {
                shapeRenderer.rect(x,y, Application.CELL, Application.CELL);
            }
        }
        shapeRenderer.end();
    }

    public void updateLang() {
        this.bundle = LanguageManager.get().getBundle();

        this.playBtn.setText(this.bundle.get("play"));
        this.buyBtn.setText(this.bundle.get("buy"));
        this.settingsBtn.setText(this.bundle.get("settings"));
        this.nextLives.setText(this.bundle.get("daily_lives"));

        // Set button width depending on the contained text
        Utils.setButtonWidth(this.layout);
        this.layout.invalidate();
    }

    public String getLoadingText () {
        String txt = this.bundle.get("loading");

        // 4 sec to display '...'
        int len = (int) this.time % 4;
        switch (len) {
            case 0:
                txt += "";
                break;
            case 1:
                txt += ".";
                break;
            case 2:
                txt += "..";
                break;
            case 3:
                txt += "...";
                break;
        }

        return txt;
    }
}
