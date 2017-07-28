package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.nomeyho.jumper.*;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.sound.SoundEnum;
import io.github.nomeyho.jumper.sound.SoundManager;
import io.github.nomeyho.jumper.utils.AnimatedImage;
import io.github.nomeyho.jumper.utils.AnimationWrapper;
import io.github.nomeyho.jumper.utils.Utils;


public class MenuScreen extends AbstractGameScreen implements ITranslatable {
    public static final float SIZE = Application.SIZE;
    private static float FADE_IN_DURATION = 0.05f;
    private static float FADE_OUT_DURATION = 0.15f;
    // View
    private ExtendViewport viewport;
    private Camera camera;
    // UI
    private Stage stage;
    private Table layout;
    private Label logo;
    private TextButton playBtn;
    private TextButton buyBtn;
    private TextButton settingsBtn;
    private SettingsMenu settingsMenu;
    private AnimatedImage planetImage;
    private MenuBackground background;

    public MenuScreen(Game game) {
        super(game);
        LanguageManager.get().register(this);
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        Application.worldHeight = this.viewport.getWorldHeight();
        Application.worldWidth = this.viewport.getWorldWidth();

        this.camera.position.set(this.camera.viewportWidth / 2,this.camera.viewportHeight /2,0);
        this.camera.update();

        Application.get().shapeRenderer.setProjectionMatrix(this.camera.combined);
        Application.get().shapeRenderer.updateMatrices();

        this.settingsMenu.setSize(this.viewport.getWorldWidth(), this.viewport.getWorldHeight());

        this.background.init();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this.stage;
    }

    @Override
    public void show() {
        // View
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(SIZE, SIZE, this.camera);
        // Layout & stage
        this.stage = new Stage(this.viewport);
        this.background = new MenuBackground();
        this.stage.addActor(this.background); // The order matters
        this.layout = new Table();
        this.stage.addActor(this.layout);
        this.layout.setFillParent(true);
        this.layout.align(Align.center);
        this.stage.setDebugAll(Application.DEBUG);
        // Fade in effect
        this.stage.getRoot().getColor().a = 0;
        this.stage.getRoot().addAction(Actions.fadeIn(FADE_IN_DURATION));

        Skin skin = Application.get().assetManager.get(Application.SKIN);

        // Logo
        this.logo = new Label(Application.TAG, skin, "large");
        this.layout.add(this.logo).padBottom(130);
        this.layout.row();

        // Planet
        AnimationWrapper planetAnimation = new AnimationWrapper(0.08f, "planet", Application.PLANET_ANIM_ATLAS);
        this.planetImage = new AnimatedImage(planetAnimation);
        this.planetImage.setScaling(Scaling.fit);
        this.layout.add(this.planetImage).padBottom(150);
        this.layout.row();

        // Play
        this.playBtn = new TextButton("", skin);
        this.playBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
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
            }
        });
        this.playBtn.getLabelCell().padBottom(7).padTop(7);
        this.layout.add(this.playBtn).padBottom(100);
        this.layout.row();

        // Buy
        this.buyBtn = new TextButton("", skin);
        this.buyBtn.getLabelCell().padBottom(7).padTop(7);
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
        this.settingsMenu = new SettingsMenu("", skin);

        // Lang
        updateLang();
        LanguageManager.get().register(this);

        // Start taking input from the UI
        Application.get().inputMultiplexer.addProcessor(this.stage);
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
        // Show the loading screen
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
        I18NBundle bundle = LanguageManager.get().getBundle();
        this.playBtn.setText(bundle.get("play"));
        this.buyBtn.setText(bundle.get("buy"));
        this.settingsBtn.setText(bundle.get("settings"));

        // Set button width depending on the contained text
        Utils.setButtonWidth(this.layout);
    }
}
