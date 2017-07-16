package io.github.nomeyho.jumper.math;

import com.badlogic.gdx.math.Vector2;

public class Location {
    private Vector2 location = new Vector2();
    private int layer;

    public Location(float x, float y, int layer) {
        this.location.x = x;
        this.location.y = y;
        this.layer = layer;
    }

    public float getX() {
        return this.location.x;
    }

    public float getY() {
        return this.location.y;
    }

    public void setLocation(float x, float y) {
        this.location.x = x;
        this.location.y =y;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return this.layer;
    }
}
