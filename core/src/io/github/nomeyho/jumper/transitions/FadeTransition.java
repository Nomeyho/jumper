package io.github.nomeyho.jumper.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public class FadeTransition implements ITransition {

    // TODO use a pool if not restricted to screens
    private static final FadeTransition INSTANCE = new FadeTransition();
    private float duration;

    private FadeTransition () {}

    /*
     * Clever to force the init to get the instance
     */
    public static FadeTransition init(float duration) {
        INSTANCE.duration = duration;
        return INSTANCE;
    }

    @Override
    public float getDuration() {
        return this.duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture from, Texture to, float alpha) {
        float w = from.getWidth();
        float h = from.getHeight();
        alpha = Interpolation.fade.apply(alpha);

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(from, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, from.getWidth(), from.getHeight(), false, true);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(to, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, to.getWidth(), to.getHeight(), false, true);
        batch.end();
    }
}
