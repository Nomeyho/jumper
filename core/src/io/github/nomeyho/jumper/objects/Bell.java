package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.nomeyho.jumper.visitor.GameObjectVisitor;

public class Bell extends AbstractGameObject{

    public static final float WIDTH = 100;
    public static final float HEIGHT = 100;
    public Texture bellTexture;

    public Bell(float x, float y, int layer) {
        super(x, y, layer);
        this.bellTexture =  new Texture(Gdx.files.internal("bell.png"));
    }

    @Override
    public void accept(GameObjectVisitor visitor) {
        visitor.visit(this);
    }
}
