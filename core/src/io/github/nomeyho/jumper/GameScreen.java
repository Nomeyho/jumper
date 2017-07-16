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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.nomeyho.jumper.objects.Player;

public class GameScreen extends ScreenAdapter {
    private ShapeRenderer shapeRenderer;
    private ExtendViewport viewport;
    private Camera camera;
    private SpriteBatch batch;

    public GameScreen() {}

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
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
        // Grid
        this.shapeRenderer = new ShapeRenderer();
        // Batch
        this.batch = new SpriteBatch();
    }

    /**
     * Called 60 times per sec
     * @param delta, time in ms since the previous frame
     */
    @Override
    public void render(float delta) {
        clearScreen();
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
            // TODO: draw images (textures) here
        Player player = new Player();
        player.draw(this.batch);
        this.batch.end();
    }

    @Override
    public void dispose () {
        this.batch.dispose();
        this.shapeRenderer.dispose();
    }
}