package io.github.nomeyho.jumper.math;

import com.badlogic.gdx.math.Vector2;
import io.github.nomeyho.jumper.Application;

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

    public void add(float x, float y) {
        this.location.x += x;
        this.location.y += y;
    }

    public void setLayer(int layer) {
        if(layer < Application.MIN_LAYER)
            this.layer = 0;
        if(layer > Application.MAX_LAYER)
            this.layer = 0;
        else
            this.layer = layer;
    }

    public int getLayer() {
        return this.layer;
    }

    @Override
    public String toString() {
        return "x:" + this.location.x + " y:" + this.location.y + " layer:" + this.layer;
    }
}
