package io.github.nomeyho.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PlayerStats {

    private static PlayerStats INSTANCE = new PlayerStats();
    private Preferences preferences;
    public int remainingLives = 0;
    public int bestScore = 0;
    public int currentScore = 0;

    private PlayerStats () {
        this.preferences = Gdx.app.getPreferences(Application.STATISTICS);
    }

    public static PlayerStats get () {
        return INSTANCE;
    }

    public void load () {
        this.remainingLives = this.preferences.getInteger("lives", 0);
    }

    public void save () {

    }

    // TODO encrypt/decrypt

    private void encrypt () {

    }

    public void decrypt () {

    }
}
