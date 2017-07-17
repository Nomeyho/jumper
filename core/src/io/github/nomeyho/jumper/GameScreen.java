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
    private Camera camera;
    private SpriteBatch batch;
    private GameManager gm;

    public GameScreen() {}

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        Application.worldHeight = this.viewport.getWorldHeight();
        Application.worldWidth = this.viewport.getWorldWidth();
        this.camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        this.camera.update();
    }

    /**
     * Called once at startup
     */
    @Override
    public void show() {
        // Camera
        this.camera = new OrthographicCamera();
        this.camera.position.set(Application.SIZE / 2, Application.SIZE / 2, 0);
        this.camera.update();
        // Application
        this.viewport = new ExtendViewport(Application.SIZE,Application.SIZE-1,this.camera);
        Application.worldHeight = this.viewport.getWorldHeight();
        Application.worldWidth = this.viewport.getWorldWidth();
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
        this.batch.setProjectionMatrix(this.camera.projection);
        this.batch.setTransformMatrix(this.camera.view);
        this.batch.begin();
        this.gm.draw(this.batch);
        this.batch.end();
    }

    /**
     * Update the game
     */
    private void update (float delta) {
        this.gm.update(delta);
    }

    @Override
    public void dispose () {
        this.batch.dispose();
        this.shapeRenderer.dispose();
    }
}