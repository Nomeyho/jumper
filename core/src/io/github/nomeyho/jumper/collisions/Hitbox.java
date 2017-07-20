package io.github.nomeyho.jumper.collisions;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;

public class Hitbox implements Shape2D {

    public Array<Polygon> polygons;

    public Hitbox () {
        this.polygons = new Array<Polygon>();
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
        for(int i=0; i<this.polygons.size; ++i)
            for(int j=0; j<hitbox.polygons.size; ++j)
                if(Intersector.overlapConvexPolygons(this.polygons.get(i), hitbox.polygons.get(j)))
                    return true;
        return false;
    }
}
