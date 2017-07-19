package io.github.nomeyho.jumper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Logger;

public class Application {
    public static final boolean DEBUG = true;
    public static final String TAG = "Jumper";
    public static final float SIZE = 1000;
    public static final float CELL = SIZE / 10;
    public static final int MIN_LAYER = -1;
    public static final int MAX_LAYER = +1;
    public static float worldHeight = SIZE;
    public static float worldWidth = SIZE;

    // Assets
    // TODO DO NOT USE ASSET IN STATIC
    public static final AssetManager assetManager = new AssetManager();
    public static void loadAssets () {
        // java -jar runnable-texturepacker.jar ./Jumper/android/assets/img/ ./Jumper/android/assets/ assets
        if(DEBUG)
            assetManager.getLogger().setLevel(Logger.DEBUG);
        // Textures
        assetManager.load("assets.atlas", TextureAtlas.class);

        // Fonts
        BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
        Application.assetManager.load("fonts/dejavu.fnt", BitmapFont.class);

        // Locales
        Application.assetManager.load("lang/locale", I18NBundle.class);
    }

    // Locale
    public static String locale = "lang/locale";
    public static void loadLocale (String locale) {
        // e.g. "en" -> "lang/locale_en" or "" -> "lang/locale"
        if (locale.length() > 0)
            locale = "lang/locale_" + locale;
        else
            locale = "lang/locale";

        // Already same language?
        if (Application.locale.equals(locale))
            return;

        // Load locale synchronously
        Application.assetManager.load(locale, I18NBundle.class);
        Application.assetManager.finishLoading();

        // Unload previous locale
        Application.assetManager.unload(Application.locale);
        Application.locale = locale;
    }
}
