package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.utils.Utils;

public class ShootingStar extends Star {

    public ShootingStar(float x, float y) {
        super(x, y);
    }

    @Override
    public void init() {
        // Random with and opacity
        super.width = super.height = Utils.randomFloat(200, 300);
        super.opacity = Utils.randomFloat(0, 1);
        // Texture
        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        super.starTexture = atlas.findRegion("shooting_star");
    }
}
