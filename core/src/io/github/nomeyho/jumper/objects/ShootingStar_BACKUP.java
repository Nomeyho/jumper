package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.math.Location;

public class ShootingStar_BACKUP {

    public Location from = new Location(0, 0, 0);
    public Location to = new Location(0, 0, 0);
    public Vector3 speed = new Vector3(100,300,0);

    public ShootingStar_BACKUP(float x1, float y1, float x2, float y2) {
        this.from.setLocation(x1, y1);
        this.to.setLocation(x2, y2);
    }

    public void update(float delta) {
        this.from.setLocation(
                this.from.getX() - delta * this.speed.x,
                this.from.getY() - delta * this.speed.y
        );
        this.to.setLocation(
                this.to.getX() - delta * this.speed.x,
                this.to.getY() - delta * this.speed.y
        );
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.line(this.from.getX(), this.from.getY(), this.to.getX(), this.to.getY());
    }
}
