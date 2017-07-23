package io.github.nomeyho.jumper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;
import io.github.nomeyho.jumper.sound.SoundEnum;

public class Application {
    private static Application INSTANCE = new Application();

    public static final float PI = 3.1415926f;
    public static final boolean DEBUG = false;
    public static final String TAG = "Jumper";
    public static final float SIZE = 1000;
    public static final float CELL = SIZE / 10;
    public static final int MIN_LAYER = -2;
    public static final int MAX_LAYER = +1;
    public static float worldHeight = SIZE;
    public static float worldWidth = SIZE;
    public static final String TEXTURE_ATLAS = "assets.atlas";
    public static final String HITBOX_ATLAS = "hitbox/hitbox.json";
    public static final String LOCALES = "lang/locale";
    public static final String PREFERENCES =  "settings.prefs"; // ~/.prefs or %UserProfile%/.prefs
    public static final String SKIN = "UI/custom.json";
    public static final String SKIN_ATLAS = "UI/custom.atlas";
    public static final String MUSIC = "sound/music.mp3";

    public AssetManager assetManager = new AssetManager();

    public void loadUIAssets () {
        if(DEBUG)
            this.assetManager.getLogger().setLevel(Logger.DEBUG);

        this.assetManager.load(
                SKIN,
                Skin.class,
                new SkinLoader.SkinParameter(SKIN_ATLAS)
        );

        // Synchronous!
        this.assetManager.finishLoading();
    }

    public void loadAssets () {
        // java -jar runnable-texturepacker.jar ./Jumper/android/assets/img/ ./Jumper/android/assets/ assets
        if(DEBUG)
            this.assetManager.getLogger().setLevel(Logger.DEBUG);

        // Textures
        this.assetManager.load(TEXTURE_ATLAS, TextureAtlas.class);

        // Fonts
        BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
        this.assetManager.load("fonts/dejavu.fnt", BitmapFont.class, parameter);

        // Hitbox
        this.assetManager.load(HITBOX_ATLAS, HitboxAtlas.class);

        // Sound
        this.assetManager.load(MUSIC, Music.class);
        Array<SoundEnum> sounds = SoundEnum.toList();
        for(int i=0; i<sounds.size; ++i)
            this.assetManager.load(sounds.get(i).getFileName(), Sound.class);
    }

    private Application() {}

    public static Application get() {
        return INSTANCE;
    }
}
