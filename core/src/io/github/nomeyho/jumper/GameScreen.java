package io.github.nomeyho.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 640;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;

    public GameScreen() {
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    // Called once at startup
    @Override
    public void show() {
        // Camera
        this.camera = new OrthographicCamera();
        this.camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        this.camera.update();
        // Viewport
        // TODO use extend viewport
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, this.camera);
        // Grid
        this.shapeRenderer = new ShapeRenderer();
        // Batch
        this.batch = new SpriteBatch();
    }

    // Called 60 times per sec
    @Override
    public void render(float delta) {
        clearScreen();
        draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        /*
        // Render new scene
        this.batch.setProjectionMatrix(this.camera.projection);
        this.batch.setTransformMatrix(this.camera.view);
        this.batch.begin();
            // TODO: draw images (textures) here
        this.batch.end();
        */
    }
}