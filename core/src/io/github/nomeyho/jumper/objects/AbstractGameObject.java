package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.collisions.Hitbox;
import io.github.nomeyho.jumper.math.Location;

public abstract class AbstractGameObject {
    public static final float WIDTH = 1;
    public static final float HEIGHT = 1;
    public Location location = new Location(0, 0);
    public Vector3 speed = new Vector3(0,0,0);
    public Hitbox hitbox;

    public abstract void update(float delta);
    public abstract void draw(SpriteBatch batch);
    public abstract void drawBackground(SpriteBatch batch);
    public abstract void init(float x, float y);

    public void updateHitbox (float width, float height, float x, float y) {
        if(this.hitbox == null) return;

        //System.out.println("Before:" + this.hitbox);
        for(int i=0; i<this.hitbox.polygons.size; ++i) {
            Polygon polygon = this.hitbox.polygons.get(i);
            // System.out.println("ici1: " + polygon.getVertices()[0]);
            polygon.setScale(width, height);
            polygon.setPosition(x, y);
            // System.out.println("ici2: " + polygon.getVertices()[0]);
        }
        //System.out.println("After:" + this.hitbox);
    }
}
