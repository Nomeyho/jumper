package io.github.nomeyho.jumper.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameScreen;
import io.github.nomeyho.jumper.JumperGame;


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
        this.logo.setWidth(SIZE * 0.4f);
        this.logo.setScaling(Scaling.fillX);
        this.logo.setX((this.viewport.getWorldWidth() - this.logo.getWidth()) / 2);
        this.logo.setY((this.viewport.getWorldHeight() - this.logo.getHeight()) / 2 + 100);

        this.progressBar.setPosition(100, 100);
        this.progressBar.setSize(100, 50);
        this.progressBar.setValue(this.progressBar.getValue() + 0.1f);
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
        this.progressBar = new ProgressBar(0f, 1f, 0.01f, false, getProgressBarStyle());
        this.stage.addActor(this.progressBar);
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
        if (Application.assetManager.update()) {
            // If the screen is touched after loading
            if(Gdx.input.isTouched())
                this.game.setScreen(new GameScreen());
        } else {
            this.progress = Application.assetManager.getProgress();
            // TODO this.progress = Interpolation.linear.apply(progress, Application.assetManager.getProgress(), 0.1f);
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

    private ProgressBar.ProgressBarStyle getProgressBarStyle () {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        Pixmap pixmap;
        TextureRegionDrawable drawable;

        // Background
        pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        style.background = drawable;

        pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        style.knob = drawable;
        style.knobBefore = drawable;

        return style;
    }
}
