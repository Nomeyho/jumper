package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.math.Location;
import io.github.nomeyho.jumper.utils.Utils;


public class Star {

    protected float width, height;
    public Location location = new Location(0, 0, -1);
    public Vector3 speed = new Vector3(0,0,0);
    protected TextureRegion starTexture;
    protected float opacity;

    public Star(float x, float y) {
        this.location.setLocation(x,y);
    }

    // Should
    public void init() {
        // Random with and opacity
        this.width = this.height = Utils.randomFloat(2, 15);
        this.opacity = Utils.randomFloat(0, 1);

        // Select new random image
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.starTexture = atlas.findRegion("star", Utils.randomInt(1, 3));


    }


    public void update(float delta) {
        this.location.add(this.speed.x * delta, this.speed.y * delta);
    }

    public void draw(SpriteBatch batch) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, this.opacity);
        batch.draw(this.starTexture, this.location.getX(), this.location.getY(), width, height);
        batch.setColor(color);
    }

    public float getSize(){
        return this.height; // = width
    }


}
