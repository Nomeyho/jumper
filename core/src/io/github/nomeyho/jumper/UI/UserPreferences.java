package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.lang.LanguageEnum;

public class UserPreferences {
    public static final UserPreferences INSTANCE = new UserPreferences();

    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public LanguageEnum lang;

    private Preferences preferences;

    private UserPreferences () {
        this.preferences = Gdx.app.getPreferences(Application.PREFERENCES);
    }

    public void load () {
        this.sound = this.preferences.getBoolean("sound", true);
        this.music = this.preferences.getBoolean("music", true);
        this.volSound = this.preferences.getFloat("volSound", 0.5f);
        this.volSound = MathUtils.clamp(this.volSound, 0.0f, 1.0f);
        this.volMusic = this.preferences.getFloat("volMusic", 0.5f);
        this.volMusic = MathUtils.clamp(this.volMusic, 0.0f, 1.0f);
    }

    public void save () {
        this.preferences.putBoolean("sound", this.sound);
        this.preferences.putBoolean("music", this.music);
        this.preferences.putFloat("volSound", this.volSound);
        this.preferences.putFloat("volMusic", this.volMusic);
        this.preferences.flush();
    }
}
