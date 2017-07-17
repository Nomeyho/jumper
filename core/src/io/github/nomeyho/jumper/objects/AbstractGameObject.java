package io.github.nomeyho.jumper.objects;

import io.github.nomeyho.jumper.math.Location;
import io.github.nomeyho.jumper.objects.visitor.IGameObjectVisitor;

public abstract class AbstractGameObject {
    public static final float WIDTH = 1;
    public static final float HEIGHT = 1;
    public Location location = new Location(0, 0, 0);

    AbstractGameObject(float x, float y, int layer) {
        this.location.setLocation(x, y);
        this.location.setLayer(layer);
    }

    public abstract void accept(IGameObjectVisitor visitor);
}
