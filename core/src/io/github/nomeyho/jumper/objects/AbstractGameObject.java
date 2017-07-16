package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.math.Location;

public abstract class AbstractGameObject {
    public static final float WIDTH = 1;
    public static final float HEIGHT = 1;
    public Location location = new Location(0, 0, 0);

    AbstractGameObject() {}

    public abstract void update(float delta);
    public abstract void draw(SpriteBatch batch);
}
