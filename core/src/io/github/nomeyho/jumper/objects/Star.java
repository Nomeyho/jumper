package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.math.Location;
import io.github.nomeyho.jumper.utils.Utils;

public class Star {

    private float width, height;
    public Location location = new Location(0, 0, 0);
    public Vector3 speed = new Vector3(0,0,0);
    private TextureRegion starTexture;

    public Star(float x, float y) {
        this.location.setLocation(x,y);
        init();

        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.starTexture =  atlas.findRegion("star");
    }

    public void init() {
        this.width = this.height = Utils.randomFloat(12, 20);
    }


    public void update(float delta) {}

    public void draw(SpriteBatch batch) {
        batch.draw(this.starTexture, this.location.getX(), this.location.getY(), width, height);
    }

    public float getHeight(){
        return this.height;
    }

}
