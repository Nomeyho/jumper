package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.nomeyho.jumper.visitor.GameObjectVisitor;

public class Player extends AbstractGameObject {
    public static final float WIDTH = 100;
    public static final float HEIGHT = 180;
    public Texture playerTexture;

    public Player(float x, float y, int layer) {
        super(x, y, layer);
        this.playerTexture =  new Texture(Gdx.files.internal("player.png"));
    }

    @Override
    public void accept(GameObjectVisitor visitor) {
        visitor.visit(this);
    }
}
