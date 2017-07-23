package io.github.nomeyho.jumper.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.UI.UserPreferences;

public class SoundManager {
    private static SoundManager INSTANCE = new SoundManager();

    private Music music;
    private ObjectMap<SoundEnum, Sound> sounds;

    private SoundManager () {}

    public static SoundManager get () {
        return INSTANCE;
    }

    public void init () {
        this.music = Application.get().assetManager.get(Application.MUSIC);
        this.sounds = new ObjectMap<SoundEnum, Sound>();

        Array<SoundEnum> sounds = SoundEnum.toList();
        for(int i=0; i<sounds.size; ++i) {
            SoundEnum sound = sounds.get(i);
            this.sounds.put(sound, Application.get().assetManager.get(sound.getFileName(), Sound.class));
        }
    }

    public void playMusic () {
        this.music.play();
        this.music.setLooping(true);
        updateVolume();
    }

    public void playSound (SoundEnum name) {
        if(this.sounds.containsKey(name)) {
            Sound sound = this.sounds.get(name);
            long id = sound.play(1.0f);
            sound.setVolume(id, UserPreferences.get().sound / 100f);
        }
    }

    public void updateVolume () {
        this.music.setVolume(UserPreferences.get().music / 100f);
    }

    // TODO use
    public void dispose () {
        this.music.dispose();
    }
}
