package io.github.nomeyho.jumper.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bell extends AbstractGameObject{

    private Texture bellTexture;
    public static final float WIDTH = 100;
    public static final float HEIGHT = 100;

    public Bell(){
        this.bellTexture =  new Texture(Gdx.files.internal("bell.png"));
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this.bellTexture,this.location.getX(),this.location.getY(), WIDTH, HEIGHT);
    }

}
