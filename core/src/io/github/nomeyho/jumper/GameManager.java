package io.github.nomeyho.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.UI.FpsCounter;
import io.github.nomeyho.jumper.levels.AbstractLevel;
import io.github.nomeyho.jumper.levels.UsualLevel;
import io.github.nomeyho.jumper.objects.AbstractGameObject;
import io.github.nomeyho.jumper.objects.Player;

public class GameManager {
    public Player player;
    public AbstractLevel level;
    private FpsCounter fpscounter = new FpsCounter();

    public GameManager() {
        this.player = new Player(500, 0, 0);
        this.level = new UsualLevel();
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

    public void input(Camera camera) {
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); //Convert pixels to World units
            this.player.setTouchedPos(touchPos);
        }
    }
}
