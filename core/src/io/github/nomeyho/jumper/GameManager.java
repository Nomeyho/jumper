package io.github.nomeyho.jumper;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.UI.FpsCounter;
import io.github.nomeyho.jumper.levels.AbstractLevel;
import io.github.nomeyho.jumper.levels.UsualLevel;
import io.github.nomeyho.jumper.objects.AbstractGameObject;
import io.github.nomeyho.jumper.objects.Player;

public class GameManager {
    public Player player;
    public AbstractLevel level;
    public Camera camera;
    public Camera guiCamera;
    private FpsCounter fpscounter = new FpsCounter();
    private InputController inputController;
    public static boolean GAME_STARTING = false;

    public GameManager(Camera camera, Camera guiCamera) {
        this.player = new Player(Application.worldWidth / 2, 0, 0);
        this.level = new UsualLevel();
        this.camera = camera;
        this.guiCamera = guiCamera;
        this.inputController = new InputController(this);
    }

    public void update (float delta) {
        this.player.update(delta);
        this.level.update(this.player.location.getX(), this.player.location.getY());
        for(AbstractGameObject go: this.level.objects)
            go.update(delta);
    }

    public void draw (SpriteBatch batch) {
        // Draw layer per layer
        for(int layer = Application.MIN_LAYER; layer < Application.MAX_LAYER; ++layer) {
            // Draw player
            if (this.player.location.getLayer() == layer)
                this.player.draw(batch);
            // Draw objects
            for(AbstractGameObject go: this.level.objects) {
                if(go.location.getLayer() == layer)
                    go.draw(batch);
            }
        }
    }

    public void drawUI (SpriteBatch batch) {
        this.fpscounter.draw(batch);
    }
}
