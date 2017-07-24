package io.github.nomeyho.jumper.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ITransition {
    public float getDuration ();
    public void render(SpriteBatch batch, Texture from, Texture to, float alpha);
}
