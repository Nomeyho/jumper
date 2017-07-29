package io.github.nomeyho.jumper;

import com.badlogic.gdx.Game;

/**
 * Core -> platform communication
 */
public interface AdService {
    void register(Game game);
    void openAdd ();
}
