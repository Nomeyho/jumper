package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends AbstractGameObject {

    private Texture playerTexture;
    public static final float WIDTH = 100;
    public static final float HEIGHT = 180;

    public Player(){
        this.playerTexture =  new Texture(Gdx.files.internal("player.png"));
    }


    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this.playerTexture,this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }
}
