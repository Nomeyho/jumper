package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.lang.LanguageEnum;

public class UserPreferences {
    private static final UserPreferences INSTANCE = new UserPreferences();

    public boolean sound;
    public boolean music;
    public int volSound;
    public int volMusic;
    public LanguageEnum lang;

    private Preferences preferences;

    private UserPreferences () {
        this.preferences = Gdx.app.getPreferences(Application.PREFERENCES);
    }

    public static UserPreferences get() {
        return INSTANCE;
    }

    public void load () {
        this.sound = this.preferences.getBoolean("sound", true);
        this.music = this.preferences.getBoolean("music", true);

        this.volSound = this.preferences.getInteger("volSound", 50);
        this.volSound = MathUtils.clamp(this.volSound, 0, 100);

        this.volMusic = this.preferences.getInteger("volMusic", 50);
        this.volMusic = MathUtils.clamp(this.volMusic, 0, 100);

        String langStr = this.preferences.getString("lang", LanguageEnum.English.name());
        this.lang = LanguageEnum.valueOf(langStr);

        if(Application.DEBUG) {
            System.out.println("Loaded preferences:");
            System.out.println(this.toString());
        }
    }

    public void save () {
        if(Application.DEBUG) {
            System.out.println("Saved preferences:");
            System.out.println(this.toString());
        }

        this.preferences.putBoolean("sound", this.sound);
        this.preferences.putBoolean("music", this.music);
        this.preferences.putInteger("volSound", this.volSound);
        this.preferences.putInteger("volMusic", this.volMusic);
        this.preferences.putString("lang", this.lang.name());
        this.preferences.flush();
    }

    @Override
    public String toString() {
        return ""
        + "Sound: " + this.sound + " (" + this.volSound + ")\n"
        + "Music: " + this.music + " (" + this.volMusic + ")\n"
        + "Lang: " + this.lang;
    }
}
