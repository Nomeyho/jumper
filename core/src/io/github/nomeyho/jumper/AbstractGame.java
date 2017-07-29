package io.github.nomeyho.jumper;

import com.badlogic.gdx.Game;

public abstract class AbstractGame extends Game {

    public AdService service;

    public AbstractGame(AdService service) {
        // Always check because the service might not exist...
        this.service = service;
    }

    @Override
    public abstract void create ();

    @Override
    public void resume () {
        super.pause();
    }

    @Override
    public void pause () {
        super.pause();
    }

    @Override
    public void dispose () { super.dispose(); }

    @Override
    public void render () { super.render(); }
}
