package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.collisions.Hitbox;
import io.github.nomeyho.jumper.math.Location;

public abstract class AbstractGameObject {
    public Location location = new Location(0, 0);
    public Vector3 speed = new Vector3(0,0,0);
    public Hitbox hitbox;

    public abstract void update(float delta);
    public abstract void draw(SpriteBatch batch);
    public abstract void drawBackground(SpriteBatch batch);
    public abstract void init(float x, float y);
    public abstract int getScore();

    public void updateHitbox (float width, float height, float x, float y, float angle) {
        if(this.hitbox == null) return;

        //System.out.println("Before:" + this.hitbox);
        for(int i=0, end=this.hitbox.polygons.size; i<end; ++i) {
            Polygon polygon = this.hitbox.polygons.get(i);
            // System.out.println("ici1: " + polygon.getVertices()[0]);
            polygon.setScale(width, height);
            // TODO polygon.setOrigin(width/2, height/2);
            polygon.setPosition(x, y);
            // TODO polygon.setRotation(angle);
            // System.out.println("ici2: " + polygon.getVertices()[0]);
        }
        //System.out.println("After:" + this.hitbox);
    }
}
