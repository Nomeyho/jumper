package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.nomeyho.jumper.AbstractGame;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.sound.SoundManager;


public class GameScreen extends AbstractGameScreen {
    private static float FADE_DURATION = 2f;
    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private OrthographicCamera guiCamera;
    private SpriteBatch batch;

    public GameScreen(AbstractGame game) {
        super(game);
    }

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

        Application.get().shapeRenderer.setProjectionMatrix(this.camera.combined);
        // TODO ?
        // Application.get().shapeRenderer.setTransformMatrix(this.camera.combined);
        // Application.get().shapeRenderer.updateMatrices();

        GameManager.get().starManager.resize();
        GameManager.get().background.resize();
        GameManager.get().gameUI.resize();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return GameManager.get().inputController;
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
        // Batch
        this.batch = new SpriteBatch();

        Application.worldWidth = Application.SIZE;
        Application.worldHeight = Application.worldWidth / Gdx.graphics.getWidth() * Gdx.graphics.getHeight();

        // /!\ if not called here, NULL reference
        GameManager.get().init(this.game, this.viewport, this.camera, this.guiCamera, this.batch);
        Application.get().inputMultiplexer.addProcessor(GameManager.get().inputController);
        SoundManager.get().playMusic();
    }

    @Override
    public void hide() {
        this.batch.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

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
        Gdx.gl.glClearColor(27 / 255f, 33 / 255f, 40 / 255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Render a new scene
     */
    private void draw() {
        // Grid
        if(Application.DEBUG)
            drawGrid();
        // World
        this.batch.begin();
        GameManager.get().draw();
        this.batch.end();
        // UI
        GameManager.get().drawUI();
    }

    /**
     * Update the game
     */
    private void update (float delta) {
        GameManager.get().update(delta);
        // Re-center camera
        updateCamera(delta);
        updateGuiCamera(delta);
    }

    private void updateCamera (float delta) {
        float x = this.camera.viewportWidth / 2;
        float y = GameManager.get().player.location.getY();

       if (y < Application.worldHeight / 2)
            y = Application.worldHeight / 2;

        this.camera.position.set(x, y,0);
        this.camera.update();
    }

    private void updateGuiCamera (float delta) {
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
}