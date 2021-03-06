package io.github.nomeyho.jumper.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.lang.LanguageEnum;

public class UserPreferences {
    private static final UserPreferences INSTANCE = new UserPreferences();

    public int sound;
    public int music;
    public LanguageEnum lang;

    private Preferences preferences;

    private UserPreferences () {
        this.preferences = Gdx.app.getPreferences(Application.PREFERENCES);
    }

    public static UserPreferences get() {
        return INSTANCE;
    }

    public void load () {
        this.sound = this.preferences.getInteger("sound", 50);
        this.sound = MathUtils.clamp(this.sound, 0, 100);

        this.music = this.preferences.getInteger("music", 50);
        this.music = MathUtils.clamp(this.music, 0, 100);

        String langStr = this.preferences.getString("lang", LanguageEnum.English.name());
        this.lang = LanguageEnum.valueOf(langStr);

        Gdx.app.log(Application.TAG, "Loaded preferences:\n" + this.toString());
        System.out.println("Loaded preferences:\n" + this.toString());
    }

    public void save () {
        Gdx.app.log(Application.TAG, "Saved preferences:\n" + this.toString());
        System.out.println("Saved preferences:\n" + this.toString());

        this.preferences.putInteger("sound", this.sound);
        this.preferences.putInteger("music", this.music);
        this.preferences.putString("lang", this.lang.name());
        this.preferences.flush();
    }

    @Override
    public String toString() {
        return ""
        + "\tSound: " + this.sound + "%\n"
        + "\tMusic: " + this.music + "%\n"
        + "\tLang: " + this.lang;
    }
}
