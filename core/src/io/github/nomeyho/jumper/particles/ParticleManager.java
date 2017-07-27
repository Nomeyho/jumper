package io.github.nomeyho.jumper.particles;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import io.github.nomeyho.jumper.Application;

public class ParticleManager {

    private static ParticleManager INSTANCE = new ParticleManager();
    private ObjectMap<ParticleEnum, ParticleEffect> effects;

    private ParticleManager(){}

    public static ParticleManager get () {
        return INSTANCE;
    }

    public void init(){
        this.effects = new ObjectMap<ParticleEnum, ParticleEffect>();

        TextureAtlas atlas = Application.get().assetManager.get(Application.TEXTURE_ATLAS);
        Array<ParticleEnum> particleEnums = ParticleEnum.toList();
        ParticleEffect particleEffect;
        for(int i=0; i<particleEnums.size; ++i) {
            ParticleEnum particleEnum = particleEnums.get(i);
            particleEffect = new ParticleEffect();
            particleEffect.load(Gdx.files.internal(particleEnum.getFilename()), atlas);
            this.effects.put(particleEnum, particleEffect);
        }
    }

    public ParticleEffect getEffect(ParticleEnum particleEnum){
        return this.effects.get(particleEnum);
    }

}
