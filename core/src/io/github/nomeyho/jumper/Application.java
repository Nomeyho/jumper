package io.github.nomeyho.jumper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Logger;
import io.github.nomeyho.jumper.lang.LanguageEnum;
import io.github.nomeyho.jumper.lang.LanguageManager;

public class Application {
    private static Application INSTANCE = new Application();

    public static final boolean DEBUG = true;
    public static final String TAG = "Jumper";
    public static final float SIZE = 1000;
    public static final float CELL = SIZE / 10;
    public static final int MIN_LAYER = -1;
    public static final int MAX_LAYER = +1;
    public static float worldHeight = SIZE;
    public static float worldWidth = SIZE;
    public static final String LOCALES = "lang/locale";
    public static final String PREFERENCES =  "settings.prefs"; // ~/.prefs or %UserProfile%/.prefs
    public static final String SKIN = "UI/neutralizer-ui.json";

    public AssetManager assetManager = new AssetManager();

    public void loadUIAssets () {
        if(DEBUG)
            this.assetManager.getLogger().setLevel(Logger.DEBUG);

        this.assetManager.load(
                SKIN,
                Skin.class,
                new SkinLoader.SkinParameter("UI/neutralizer-ui.atlas")
        );

        // Synchronous!
        this.assetManager.finishLoading();
    }

    public void loadAssets () {
        // java -jar runnable-texturepacker.jar ./Jumper/android/assets/img/ ./Jumper/android/assets/ assets
        if(DEBUG)
            this.assetManager.getLogger().setLevel(Logger.DEBUG);
        // Textures
        this.assetManager.load("assets.atlas", TextureAtlas.class);

        // Fonts
        BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
        this.assetManager.load("fonts/dejavu.fnt", BitmapFont.class);
    }

    private Application() {}

    public static Application get() {
        return INSTANCE;
    }
}
