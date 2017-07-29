package io.github.nomeyho.jumper.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.nomeyho.jumper.Application;

public class Location {
    private Vector2 location = new Vector2();

    public Location(float x, float y) {
        this.location.x = x;
        this.location.y = y;
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

    @Override
    public String toString() {
        return "x:" + this.location.x + " y:" + this.location.y;
    }
}
