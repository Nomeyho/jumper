package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.JumperGame;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.sound.SoundManager;
import io.github.nomeyho.jumper.utils.Utils;


public class LoadingScreen extends ScreenAdapter {
    // View
    private static float SIZE = 480;
    private static float CELL = 32;
    private ExtendViewport viewport;
    private Camera camera;
    // State
    private JumperGame game;
    private float progress = 0;
    // UI
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Texture logoTexture;
    private Image logo;
    private ProgressBar progressBar;
    private Label progressLabel;

    public LoadingScreen(JumperGame game) {
        this.game = game;
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
        this.logo.setY(centerY - this.logo.getHeight()/2 + 100);

        this.progressBar.setSize(SIZE * 0.9f, 30);
        this.progressBar.setX(centerX - this.progressBar.getWidth()/2);
        this.progressBar.setY(CELL * 2);

        this.progressLabel.setX(centerX - this.progressLabel.getWidth()/2);
        this.progressLabel.setY(CELL * 2 + this.progressBar.getHeight());
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

        // Progress bar
        Skin skin = Application.get().assetManager.get(Application.SKIN);
        this.progressBar = new ProgressBar(0f, 1f,0.01f,false, skin);
        this.stage.addActor(this.progressBar);

        // Progress label
        this.progressLabel = new Label("", skin);
        this.stage.addActor(this.progressLabel);

        // Start queuing other assets for loading
        Application.get().loadAssets();
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
        if (Application.get().assetManager.update()) {
            this.progressBar.setValue(1);
            this.progressLabel.setText("100%");
            this.game.setScreen(new MenuScreen(this.game));
            // Init managers depending on the assets
            SoundManager.get().init();
        } else {
            this.progress = Interpolation.linear.apply(this.progress, Application.get().assetManager.getProgress(), 0.1f);
            this.progressBar.setValue(this.progress);
            this.progressLabel.setText((int)this.progress + "%");
        }
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
}
