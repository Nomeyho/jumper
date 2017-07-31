package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;

public class ScoreLabel extends Label implements Pool.Poolable {
    public boolean toRemove = false;

    public ScoreLabel(CharSequence text, Skin skin) {
        super(text, skin, "small");
        this.init(0, 0, "");
    }

    public void init (float x, float y, String score) {
        this.toRemove = false;
        this.setPosition(x, y);
        this.setText(score);
        this.setAlignment(Align.center);
    }

    @Override
    public void reset() {}

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.addAction(Actions.sequence(
                Actions.fadeIn(0.14f),
                Actions.fadeOut(0.14f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        toRemove = true;
                    }
                })
        ));
        super.draw(batch, parentAlpha);
    }
}
