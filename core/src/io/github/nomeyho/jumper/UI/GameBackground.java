package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.math.Location;
import io.github.nomeyho.jumper.utils.AnimationWrapper;

public class GameBackground {

    private TextureRegion background;
    private TextureRegion planet;
    private TextureRegion satelite;
    private float time =0;
    private final static float PERIOD = 10;
    private final static float SATELITEWIDTH = 32;
    private final static float SATELITEHEIGHT = 70;
    private static float PLANETSIZE;
    private static Location PLANETLOCATION;
    private Location satelitePosition;


    public GameBackground () {
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        this.background = atlas.findRegion("background");
        this.planet = atlas.findRegion("planet");
        this.satelite = atlas.findRegion("satelite");
        init();
    }

    public void init () {
        PLANETSIZE = Application.worldWidth / 2f;
        PLANETLOCATION = new Location(Application.worldWidth / 4f, -50, -1);
        satelitePosition = new Location(
                PLANETLOCATION.getX()+ PLANETSIZE/2f - SATELITEWIDTH/2f,
                PLANETLOCATION.getY() - SATELITEHEIGHT / 2, -1
        );
    }

    public void update (float delta) {
        this.time += delta;
        float x, y;
        x = (float) Math.cos(2 * Application.PI * time / PERIOD);
        y = (float) Math.sin(2 * Application.PI * time / PERIOD);
        x = x * PLANETSIZE / 2 + PLANETLOCATION.getX() + PLANETSIZE / 2 - SATELITEWIDTH / 2;
        y = y * PLANETSIZE / 2 + PLANETLOCATION.getY() + PLANETSIZE / 2 - SATELITEHEIGHT / 2;

        this.satelitePosition.setLocation(x,y);

    }

    public void draw (SpriteBatch batch) {
        float w = Application.worldWidth;
        float h = this.background.getRegionHeight() * (w / this.background.getRegionWidth());

        batch.draw(this.planet, PLANETLOCATION.getX(), PLANETLOCATION.getY(), PLANETSIZE, PLANETSIZE);

        float angle = (2 * Application.PI * time / PERIOD) % (2 * Application.PI);
        batch.draw(this.satelite, satelitePosition.getX(), satelitePosition.getY(), SATELITEWIDTH / 2f, SATELITEHEIGHT / 2f, SATELITEWIDTH, SATELITEHEIGHT, 1f, 1f, (float) Math.toDegrees(angle) -90);

        // Background
        batch.draw(this.background, 0, 0, w, h);
    }

    public void resize () {
        init();
    }
}
