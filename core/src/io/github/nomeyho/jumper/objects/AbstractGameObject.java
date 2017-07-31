package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
    public abstract void disappear();

    public void updateHitbox (float width, float height, float x, float y, float angle) {
        if(this.hitbox == null) return;

        for(int i=0, end=this.hitbox.polygons.size; i<end; ++i) {
            Polygon polygon = this.hitbox.polygons.get(i);
            polygon.setScale(width, width); // back magic
            polygon.setPosition(x, y);
            polygon.setOrigin(hitbox.originX, hitbox.originY);
            polygon.translate(width * hitbox.originX, width * hitbox.originY);
            polygon.setRotation(angle);
        }
    }
}
