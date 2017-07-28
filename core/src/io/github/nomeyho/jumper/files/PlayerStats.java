package io.github.nomeyho.jumper.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Base64Coder;
import io.github.nomeyho.jumper.Application;

public class PlayerStats {

    private static PlayerStats INSTANCE = new PlayerStats();
    private static final int INITIAL_LIVES = 10;
    private Preferences preferences;
    private String hash;
    public int remainingLifes = 0;
    public int bestScore = 0;
    public int currentScore = 0; // non persistent

    private PlayerStats () {
        this.preferences = Gdx.app.getPreferences(Application.STATISTICS);
    }

    public static PlayerStats get () {
        return INSTANCE;
    }

    public void load () {
        this.remainingLifes = this.preferences.getInteger("lifes", INITIAL_LIVES);
        this.remainingLifes = MathUtils.clamp(this.remainingLifes, 0, Integer.MAX_VALUE);

        this.bestScore = this.preferences.getInteger("best", 0);
        this.bestScore = MathUtils.clamp(this.bestScore, 0, Integer.MAX_VALUE);

        this.hash = this.preferences.getString("hash", "");

        // If the file was modified, reset it
        if(!this.checkHash()) {
            Gdx.app.log(Application.TAG, "Invalid hash, reset");
            this.remainingLifes = INITIAL_LIVES;
            this.bestScore = 0;
            this.save();
        }

        Gdx.app.log(Application.TAG, "Loaded stats:\n" + this.toString());
    }

    public void save () {
        Gdx.app.log(Application.TAG, "Saved stats:\n" + this.toString());

        this.preferences.putInteger("lives", this.remainingLifes);
        this.preferences.putInteger("best", this.bestScore);
        // Compute hash before storing
        this.preferences.putString("hash", hash());
        this.preferences.flush();
    }

    private String hash () {
        int hash = 7;
        String clearText = this.remainingLifes + "-" + this.bestScore;

        for(int i=0, end=clearText.length(); i<end; ++i)
            hash = hash * 31 + clearText.charAt(i);

        String cypherText = hash + "";
        char[] cypher = Base64Coder.encode(cypherText.getBytes());

        return new String(cypher);
    }

    public boolean checkHash () {
        return this.hash.equals(hash());
    }

    @Override
    public String toString () {
        return ""
        + "\tlives: " + this.remainingLifes
        + "\n\tbest score: " + this.bestScore
        + "\n\thash: " + this.hash;
    }

    public void decreaseLifes() {
        this.remainingLifes = Math.max(this.remainingLifes - 1, 0);
    }

    public void increaseLifes (int nb) {
        nb = nb < 0 ? 0 : nb;
        this.remainingLifes += nb;
    }
}
