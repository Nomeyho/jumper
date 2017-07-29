package io.github.nomeyho.jumper.UI;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import io.github.nomeyho.jumper.AbstractGame;

public abstract class AbstractGameScreen implements Screen {
    protected AbstractGame game;

    public AbstractGameScreen (AbstractGame game) {
        this.game = game;
    }

    @Override
    public abstract void render(float delta);

    @Override
    public abstract void resize(int width, int height);

    public abstract InputProcessor getInputProcessor();

    @Override
    public abstract void show();

    @Override
    public abstract void hide();

    @Override
    public abstract void pause();

    @Override
    public abstract void resume ();

    // Avoid confusion, only allow hide() to be implemented by subclasses
    @Override
    public final void dispose () {
        this.hide();
    };
}