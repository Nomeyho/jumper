package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.collisions.Hitbox;
import io.github.nomeyho.jumper.math.Location;

public abstract class AbstractGameObject {
    public static final float WIDTH = 1;
    public static final float HEIGHT = 1;
    public Location location = new Location(0, 0, 0);
    public Vector3 speed = new Vector3(0,0,0);
    public Hitbox hitbox;

    AbstractGameObject(float x, float y, int layer) {
        this.location.setLocation(x, y);
        this.location.setLayer(layer);
    }

    public abstract void update(float delta);
    public abstract void draw(SpriteBatch batch);

    public void updateHitbox () {
        if(hitbox == null) return;

        for(int i=0; i<this.hitbox.polygons.size; ++i) {
            Polygon polygon = this.hitbox.polygons.get(i);
            polygon.setScale(WIDTH, HEIGHT);
            polygon.translate(this.location.getX(), this.location.getY());
        }
    }
}
