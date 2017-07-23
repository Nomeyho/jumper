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
    private static float SIZE = Application.SIZE;
    private static float CELL = Application.CELL;
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
        this.logo.setY(centerY - this.logo.getHeight()/2 + 130);

        this.progressBar.setSize(SIZE * 0.9f, 15);
        this.progressBar.setX(centerX - this.progressBar.getWidth()/2);
        this.progressBar.setY(CELL * 2);

        this.progressLabel.setX(centerX - this.progressLabel.getWidth()/2);
        this.progressLabel.setY(this.progressBar.getY() + 70);
    }

    @Override
    public void show() {
        // View
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(SIZE, SIZE, this.camera);
        this.shapeRenderer = new ShapeRenderer();
        this.stage = new Stage(this.viewport);
        this.stage.setDebugAll(Application.DEBUG);

        // Logo
        this.logoTexture = new Texture(Gdx.files.internal("logo.png"), true);
        this.logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
        this.logo = new Image(this.logoTexture);
        this.stage.addActor(this.logo);

        // Progress bar
        Skin skin = Application.get().assetManager.get(Application.SKIN);
        this.progressBar = new ProgressBar(0, 1,0.01f,false, skin);
        this.stage.addActor(this.progressBar);

        // Progress label
        this.progressLabel = new Label("", skin, "small");
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

    private long t1 = System.currentTimeMillis();
    private void update() {
        if(Application.DEBUG) {
            long t2 = System.currentTimeMillis();
            if (t2 - t1 > 50) { // +10% every 0.5sec
                t1 = t2;
                this.progress++;
                this.progressBar.setValue(this.progress);
                this.progressLabel.setText((int) this.progress + "%");
            }
            if(this.progress >= 100 && Application.get().assetManager.update()) {
                this.game.setScreen(new MenuScreen(this.game));
                SoundManager.get().init();
            } else {
                Gdx.app.log(Application.TAG, "Loading: " + (int)(Application.get().assetManager.getProgress()*100) + "%");
            }
            return;
        }

        if (Application.get().assetManager.update()) {
            this.progressBar.setValue(1);
            this.progressLabel.setText("100%");
            this.game.setScreen(new MenuScreen(this.game));
            // Init managers depending on the assets
            SoundManager.get().init();
        } else {
            this.progress = Interpolation.linear.apply(this.progress, Application.get().assetManager.getProgress(), 0.1f);
            this.progressBar.setValue(this.progress);
            this.progressLabel.setText((int)(this.progress*100) + "%");
        }

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
