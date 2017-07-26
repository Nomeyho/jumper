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
    private boolean blink = false; // is this star blinking
    private float blinkTime = 0;   // for how long the star is blinking
    private float nextBlink = 0;   // how long before next blink

    public Star(float x, float y) {
        this.location.setLocation(x,y);
    }

    // Should
    public void init() {
        // Random values
        this.width = this.height = Utils.randomFloat(2, 12);
        this.opacity = Utils.randomFloat(0, 1);
        this.blink = false;
        this.blinkTime = 0;
        this.nextBlink = Utils.randomFloat(10, 40);

        // Select new random image
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.starTexture = atlas.findRegion("star", Utils.randomInt(1, 3));
    }

    public void update(float delta) {
        this.location.add(this.speed.x * delta, this.speed.y * delta);
        this.blinkTime += delta;

        if(this.blink) {
            // Blink to 0.1 sec at most
            if(this.blinkTime > 0.1) {
                this.blink = false;
                this.blinkTime = 0;
                this.nextBlink = Utils.randomFloat(10, 40);
            }
        } else {
            if(this.blinkTime > this.nextBlink) {
                this.blink = true;
                this.blinkTime = 0;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if(this.blink)
            return;

        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, this.opacity);
        batch.draw(this.starTexture, this.location.getX(), this.location.getY(), width, height);
        batch.setColor(color);
    }

    public float getSize(){
        return this.height; // = width
    }


}
