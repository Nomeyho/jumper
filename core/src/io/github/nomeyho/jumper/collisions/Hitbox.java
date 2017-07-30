package io.github.nomeyho.jumper.collisions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import io.github.nomeyho.jumper.math.Polygon;

import java.util.Arrays;

public class Hitbox implements Shape2D {

    public Array<Polygon> polygons;
    public float originX;
    public float originY;

    public Hitbox () {
        this.polygons = new Array<Polygon>();
    }

    public Hitbox (Array<Polygon> polygons) {
        this.polygons = polygons;
    }

    @Override
    public boolean contains(Vector2 point) {
        for(int i=0; i<this.polygons.size; ++i)
            if(this.polygons.get(i).contains(point))
                return true;
        return false;
    }

    @Override
    public boolean contains(float x, float y) {
        for(int i=0; i<this.polygons.size; ++i)
            if(this.polygons.get(i).contains(x, y))
                return true;
        return false;
    }

    public boolean overlap(Hitbox hitbox) {
        for(int i=0; i<this.polygons.size; ++i) {
            for(int j=0; j<hitbox.polygons.size; ++j) {
                if(Intersector.overlapConvexPolygons(this.polygons.get(i), hitbox.polygons.get(j)))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        for(int i=0; i<this.polygons.size; ++i) {
            builder.append(this.polygons.get(i).toString());
            builder.append("\n");
        }

        return builder.toString();
    }

    public Hitbox copy() {
        Hitbox hitbox = new Hitbox();

        for(int i=0; i<this.polygons.size; ++i) {
            float[] vertices = this.polygons.get(i).getTransformedVertices();
            float[] copy = Arrays.copyOf(vertices, vertices.length);
            hitbox.polygons.add(new Polygon(copy));
        }

        return hitbox;
    }

    public void draw (ShapeRenderer shapeRenderer) {
        for(int i=0; i<this.polygons.size; ++i) {
            shapeRenderer.polygon(this.polygons.get(i).getTransformedVertices());
        }
    }
}
