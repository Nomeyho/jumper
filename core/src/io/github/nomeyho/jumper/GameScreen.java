package io.github.nomeyho.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


public class GameScreen extends ScreenAdapter {
    private ShapeRenderer shapeRenderer;
    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private OrthographicCamera guiCamera;
    private SpriteBatch batch;
    private GameManager gm;

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
        // Game manager
        this.gm = new GameManager();
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
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
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
        this.gm.draw(this.batch);
        this.batch.end();
        // UI
        this.batch.setProjectionMatrix(this.guiCamera.projection);
        this.batch.setTransformMatrix(this.guiCamera.view);
        this.batch.begin();
        System.out.println("ICI" + this.guiCamera.viewportHeight + "/" + this.guiCamera.viewportWidth);
        this.gm.drawUI(this.batch);
        this.batch.end();
    }

    /**
     * Update the game
     */
    private void update (float delta) {
        this.gm.input(camera);
        this.gm.update(delta);
        // Re-center camera
        this.camera.position.set(camera.viewportWidth/2,this.gm.player.location.getY(),0);
        this.camera.update();
    }

    @Override
    public void dispose () {
        this.batch.dispose();
        this.shapeRenderer.dispose();
    }
}