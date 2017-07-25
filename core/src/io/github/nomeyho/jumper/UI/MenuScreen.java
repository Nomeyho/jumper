package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import io.github.nomeyho.jumper.utils.AnimatedImage;
import io.github.nomeyho.jumper.utils.AnimationWrapper;


public class MenuScreen extends AbstractGameScreen implements ITranslatable {
    public static final float SIZE = Application.SIZE;
    private static float FADE_IN_DURATION = 0.05f;
    private static float FADE_OUT_DURATION = 0.15f;
    // View
    private ExtendViewport viewport;
    private Camera camera;
    // UI
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Table layout;
    private Label logo;
    private TextButton playBtn;
    private TextButton buyBtn;
    private TextButton settingsBtn;
    // TODO private SettingsMenu settingsMenu;
    private AnimatedImage planetImage;
    private MenuScreenBackground background;

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

        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer.updateMatrices();

        // TODO this.settingsMenu.setSize(this.viewport.getWorldWidth() - 10, this.viewport.getWorldHeight() - 10);
        // TODO this.settingsMenu.setPosition(centerX, centerY, Align.center);

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
        this.shapeRenderer = new ShapeRenderer();
        // Layout & stage
        this.stage = new Stage(this.viewport);
        this.background = new MenuScreenBackground();
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
        this.layout.add(this.logo).padBottom(60);
        this.layout.row();

        // Planet
        AnimationWrapper planetAnimation = new AnimationWrapper(0.08f, "planet", Application.PLANET_ANIM_ATLAS);
        this.planetImage = new AnimatedImage(planetAnimation);
        this.planetImage.setScaling(Scaling.fit);
        this.layout.add(this.planetImage).padBottom(60);
        this.layout.row();

        // Play
        this.playBtn = new TextButton("", skin);
        this.playBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                // Fade out effect
                stage.addAction(Actions.sequence(
                        Actions.fadeOut(FADE_OUT_DURATION),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(new GameScreen(game));
                            }
                        })
                ));
            }
        });
        this.layout.add(this.playBtn).padBottom(75);
        this.layout.row();

        // Buy
        this.buyBtn = new TextButton("", skin);
        this.layout.add(this.buyBtn).padBottom(75);
        this.layout.row();

        // Settings
        this.settingsBtn = new TextButton("", skin);
        this.settingsBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                // TODO settingsMenu.setVisible(true);
            }
        });
        this.layout.add(this.settingsBtn).padBottom(75);
        // TODO this.settingsMenu = new SettingsMenu("", skin);
        // TODO this.stage.addActor(this.settingsMenu);

        // Lang
        updateLang();
        LanguageManager.get().register(this);

        // Start taking input from the UI
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void hide() {
        this.shapeRenderer.dispose();
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
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < this.viewport.getWorldWidth(); x += Application.CELL) {
            for (int y = 0; y < this.viewport.getWorldHeight(); y += Application.CELL) {
                this.shapeRenderer.rect(x,y, Application.CELL, Application.CELL);
            }
        }
        this.shapeRenderer.end();
    }

    public void updateLang() {
        I18NBundle bundle = LanguageManager.get().getBundle();
        this.playBtn.setText(bundle.get("play"));
        this.buyBtn.setText(bundle.get("buy"));
        this.settingsBtn.setText(bundle.get("settings"));

        // Set button width depending on the contained text
        this.setButtonWidth();
    }

    private void setButtonWidth () {
        this.layout.layout();

        // Get width
        float width = 0;
        SnapshotArray<Actor> children = this.layout.getChildren();
        Actor child;
        for(int i=0, end = children.size; i<end; ++i) {
            child = children.get(i);
            if(child instanceof TextButton) {
                TextButton button = (TextButton) child;
                if(button.getLabel().getWidth() > width)
                    width = button.getLabel().getWidth();
            }
        }

        // Padding:
        width += 30;

        // Set width
        Array<Cell> cells = this.layout.getCells();
        for(int i=0, end = cells.size; i<end; ++i) {
            Cell cell = cells.get(i);
            if(cell.getActor() instanceof Button)
                cell.width(width).expandX();
            this.layout.layout();
            // Effectively apply new style
            cell.getActor().setSize(width, cell.getActorHeight());
        }

        // Effectively apply new style
        this.layout.layout();
    }
}
