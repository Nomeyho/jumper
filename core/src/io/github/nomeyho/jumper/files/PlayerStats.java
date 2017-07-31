package io.github.nomeyho.jumper.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.nomeyho.jumper.Application;

import java.util.Date;

public class PlayerStats {
    private static PlayerStats INSTANCE = new PlayerStats();

    // 50 lives + 25 every 12h
    private static final int INITIAL_LIFES = 50;
    private static final int DAILY_LIFES = 25;
    private static final int TIME_LIFES = Application.DEBUG ? (30*1000) : (12*60*60*1000);

    private Preferences preferences;
    private String hash;
    public int remainingLifes = 0;
    public int bestScore = 0;
    public Date last = null;
    public int currentScore = 0; // non persistent

    private PlayerStats () {
        this.preferences = Gdx.app.getPreferences(Application.STATISTICS);
    }

    public static PlayerStats get () {
        return INSTANCE;
    }

    public void load () {
        this.remainingLifes = this.preferences.getInteger("lives", INITIAL_LIFES);
        this.remainingLifes = MathUtils.clamp(this.remainingLifes, 0, Integer.MAX_VALUE);

        this.bestScore = this.preferences.getInteger("best", 0);
        this.bestScore = MathUtils.clamp(this.bestScore, 0, Integer.MAX_VALUE);

        long tmp = this.preferences.getLong("last", TimeUtils.millis());
        this.last = new Date(tmp);

        this.hash = this.preferences.getString("hash", "");

        // If the file was modified, reset it
        if(!this.checkHash()) {
            Gdx.app.log(Application.TAG, "Invalid hash, reset");
            this.remainingLifes = INITIAL_LIFES;
            this.bestScore = 0;
            this.last = new Date();
            this.save();
        }

        Gdx.app.log(Application.TAG, "Loaded stats:\n" + this.toString());
    }

    public void save () {
        Gdx.app.log(Application.TAG, "Saved stats:\n" + this.toString());

        this.preferences.putInteger("lives", this.remainingLifes);
        this.preferences.putInteger("best", this.bestScore);
        this.preferences.putLong("last", this.last.getTime());
        // Compute hash before storing
        this.preferences.putString("hash", hash());
        this.preferences.flush();
    }

    private String hash () {
        int hash = 7;
        String clearText = this.remainingLifes + "-" + this.bestScore + "-" + this.last;

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
        + "\n\tlast: " + this.last
        + "\n\thash: " + this.hash;
    }

    public void decreaseLifes() {
        this.remainingLifes = Math.max(this.remainingLifes - 1, 0);
    }

    public void increaseLifes (int nb) {
        nb = nb < 0 ? 0 : nb;
        this.remainingLifes += nb;
    }

    private void getDailyLifes () {
            this.last = new Date();
            this.remainingLifes += DAILY_LIFES;
            save();
    }

    public String getCountdown(I18NBundle bundle) {
        long time = TimeUtils.timeSinceMillis(this.last.getTime());

        if(time > TIME_LIFES) {
            getDailyLifes();
            return null;
        } else {
            // Remaining time to wait

            time = TIME_LIFES - time;
            int seconds = (int) (time / 1000) % 60;
            int minutes = (int) ((time / (1000*60)) % 60);
            int hours   = (int) ((time / (1000*60*60)) % 24);
            if(hours > 0)
                return hours + " " + bundle.get("hours") + " " + minutes + bundle.get("minutes");
            if(minutes > 0)
                return minutes + " " + bundle.get("minutes");
            else
                return seconds + " " + bundle.get("seconds");
        }
    }
}
