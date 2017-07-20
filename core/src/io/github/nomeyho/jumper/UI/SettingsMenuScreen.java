package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameScreen;
import io.github.nomeyho.jumper.JumperGame;
import io.github.nomeyho.jumper.lang.ITranslatable;
import io.github.nomeyho.jumper.lang.LanguageManager;


public class SettingsMenuScreen extends ScreenAdapter implements ITranslatable {
    // View
    private static float SIZE = 480;
    private static float CELL = 32;
    private ExtendViewport viewport;
    private Camera camera;
    // State
    private JumperGame game;
    // UI
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Texture logoTexture;
    private Image logo;
    private TextButton playBtn;
    private TextButton buyBtn;
    private TextButton settingsBtn;

    public SettingsMenuScreen(JumperGame game) {
        this.game = game;
        LanguageManager.get().register(this);
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        this.camera.position.set(this.camera.viewportWidth / 2,this.camera.viewportHeight /2,0);
        this.camera.update();
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer.updateMatrices();

        // Position widgets
        float centerX = this.viewport.getWorldWidth() / 2;
        float centerY = this.viewport.getWorldHeight() / 2;

        this.logo.setWidth(SIZE * 0.4f);
        this.logo.setScaling(Scaling.fillX);
        this.logo.setX(centerX - this.logo.getWidth()/2);
        this.logo.setY(centerY - this.logo.getHeight()/2 + 150);

        this.playBtn.setWidth(SIZE * 0.4f);
        this.playBtn.setPosition(centerX, centerY - 50, Align.center);

        this.buyBtn.setWidth(SIZE * 0.4f);
        this.buyBtn.setPosition(centerX, this.playBtn.getY() - this.buyBtn.getHeight(), Align.center);

        this.settingsBtn.setWidth(SIZE * 0.4f);
        this.settingsBtn.setPosition(centerX, this.buyBtn.getY() - this.settingsBtn.getHeight(), Align.center);
    }

    @Override
    public void show() {
        // View
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(SIZE, SIZE, this.camera);
        this.shapeRenderer = new ShapeRenderer();
        this.stage = new Stage(this.viewport);

        // Logo
        this.logoTexture = new Texture(Gdx.files.internal("logo.png"));
        this.logo = new Image(this.logoTexture);
        this.stage.addActor(this.logo);

        // Play
        this.playBtn = new TextButton("", getBtnStyle());
        this.playBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new GameScreen());
            }
        });
        this.stage.addActor(this.playBtn);

        // Buy
        this.buyBtn = new TextButton("", getBtnStyle());
        this.stage.addActor(this.buyBtn);

        // Settings
        this.settingsBtn = new TextButton("", getBtnStyle());
        this.stage.addActor(this.settingsBtn);

        updateLang();

        // Start taking input from the UI
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        this.logoTexture.dispose();
        this.shapeRenderer.dispose();
    }

    private void update() {
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, Color.GRAY.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT) ;
    }

    private void draw() {
        // Draw the grid
        if(Application.DEBUG)
            this.drawGrid();
        // Show the loading screen
        this.stage.act();
        this.stage.draw();
    }

    private void drawGrid () {
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < this.viewport.getWorldWidth(); x += CELL) {
            for (int y = 0; y < this.viewport.getWorldHeight(); y += CELL) {
                this.shapeRenderer.rect(x,y, CELL, CELL);
            }
        }
        this.shapeRenderer.end();
    }

    private TextButton.TextButtonStyle getBtnStyle () {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        TextureAtlas atlas = Application.get().assetManager.get("assets.atlas");
        style.up = new TextureRegionDrawable(atlas.findRegion("button"));
        style.down = new TextureRegionDrawable(atlas.findRegion("button_down"));
        style.font = new BitmapFont();

        return style;
    }

    public void updateLang() {
        I18NBundle bundle = LanguageManager.get().getBundle();
        this.playBtn.setText(bundle.get("play"));
        this.buyBtn.setText(bundle.get("buy"));
        this.settingsBtn.setText(bundle.get("settings"));
    }
}
