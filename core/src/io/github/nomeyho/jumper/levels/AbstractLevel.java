package io.github.nomeyho.jumper.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.GameManager;
import io.github.nomeyho.jumper.objects.AbstractGameObject;

public abstract class AbstractLevel {

    // GameObject
    public Array<AbstractGameObject> objects;
    public final static float MIN_HEIGHT = 500;

    public AbstractLevel () {
        init();
    }

    public void init () {
        this.objects = new Array<AbstractGameObject>();
        this.objects.ordered = false;
    }

    public abstract void update(float delta, float playerX, float playerY);
    public abstract void draw(SpriteBatch batch);
    public abstract void disappear(AbstractGameObject go);

    public void drawHitbox() {
        ShapeRenderer shapeRenderer = Application.get().shapeRenderer;
        shapeRenderer.setProjectionMatrix(GameManager.get().camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for(int i=0, end=this.objects.size; i<end; ++i)
            this.objects.get(i).hitbox.draw(shapeRenderer);

        GameManager.get().player.hitbox.draw(shapeRenderer);

        shapeRenderer.end();
    }
}
