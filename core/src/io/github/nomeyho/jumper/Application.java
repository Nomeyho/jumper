package io.github.nomeyho.jumper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Logger;

public class Application {
    public static final boolean DEBUG = true;
    public static final String TAG = "Jumper";
    public static final float SIZE = 1000;
    public static final int MIN_LAYER = -1;
    public static final int MAX_LAYER = +1;
    public static float worldHeight = SIZE;
    public static float worldWidth = SIZE;
    // TODO DO NOT USE ASSET IN STATIC
    public static final AssetManager assetManager = new AssetManager();

    static {
        // java -jar runnable-texturepacker.jar ./Jumper/android/assets/img/ ./Jumper/android/assets/ assets
        if(DEBUG)
            assetManager.getLogger().setLevel(Logger.DEBUG);
        // Textures
        assetManager.load("assets.atlas", TextureAtlas.class);
        assetManager.finishLoading();
    }
}
