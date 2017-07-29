package io.github.nomeyho.jumper.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.github.nomeyho.jumper.objects.AbstractGameObject;

public abstract class AbstractLevel {
    // GameObject
    public Array<AbstractGameObject> objects = new Array<AbstractGameObject>();

    public abstract void update(float delta, float playerX, float playerY);
    public abstract void draw(SpriteBatch batch);
}
