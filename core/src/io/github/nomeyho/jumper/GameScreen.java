package io.github.nomeyho.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.nomeyho.jumper.sound.SoundManager;


public class GameScreen extends ScreenAdapter {
    private ShapeRenderer shapeRenderer;
    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private OrthographicCamera guiCamera;
    private SpriteBatch batch;

    public GameScreen() {}

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        Application.worldHeight = this.viewport.getWorldHeight();
        Application.worldWidth = this.viewport.getWorldWidth();

        this.camera.position.set(Application.worldWidth/2,Application.worldHeight/2,0);
        this.camera.update();

        this.guiCamera.viewportWidth = Application.worldWidth;
        this.guiCamera.viewportHeight = Application.worldHeight;
        this.guiCamera.position.set(Application.worldWidth/2,Application.worldHeight/2,0);
        this.guiCamera.update();

        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer.updateMatrices();

        GameManager.get().snowflakeManager.resize();
    }

    /**
     * Called once at startup
     */
    @Override
    public void show() {
        // Camera
        this.camera = new OrthographicCamera();
        this.guiCamera = new OrthographicCamera();
        // Application
        this.viewport = new ExtendViewport(Application.SIZE, Application.SIZE, this.camera);
        // Grid
        this.shapeRenderer = new ShapeRenderer();
        // Batch
        this.batch = new SpriteBatch();

        Application.worldWidth = Application.SIZE;
        Application.worldHeight = Application.worldWidth / Gdx.graphics.getWidth() * Gdx.graphics.getHeight();
        // /!\ if not called here, NULL reference
        GameManager.get().init(this.camera, this.guiCamera);

        SoundManager.get().playMusic();
    }

    /**
     * Called 60 times per sec
     * @param delta, time in ms since the previous frame
     */
    @Override
    public void render(float delta) {
        clearScreen();
        update(delta);
        draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(27 / 250f, 33 / 255f, 40 / 255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Render a new scene
     */
    private void draw() {
        // World
        this.batch.setProjectionMatrix(this.camera.projection);
        this.batch.setTransformMatrix(this.camera.view);
        this.batch.begin();
        GameManager.get().draw(this.batch);
        this.batch.end();
        // UI
        this.batch.setProjectionMatrix(this.guiCamera.projection);
        this.batch.setTransformMatrix(this.guiCamera.view);
        this.batch.begin();
        GameManager.get().drawUI(this.batch);
        this.batch.end();
        // Grid
        if(Application.DEBUG)
            drawGrid();
    }

    /**
     * Update the game
     */
    private void update (float delta) {
        GameManager.get().update(delta);
        // Re-center camera
        updateCamera(delta);
        updateGuiCamera(delta);
        this.camera.update();
    }

    @Override
    public void dispose () {
        this.batch.dispose();
        this.shapeRenderer.dispose();
    }

    private void updateCamera (float delta) {
        float x = camera.viewportWidth / 2;
        float y = GameManager.get().player.location.getY();

       if (y < Application.worldHeight / 2)
            y = Application.worldHeight / 2;

        this.camera.position.set(x, y,0);
        this.camera.update();
    }

    private void updateGuiCamera (float delta) {
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
}