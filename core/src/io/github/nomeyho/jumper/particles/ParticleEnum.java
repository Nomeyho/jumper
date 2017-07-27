package io.github.nomeyho.jumper.particles;


import com.badlogic.gdx.utils.Array;

public enum ParticleEnum {
    SMOKE("effects/smoke"),
    FIRE("effects/fire");

    private String filename;

    private ParticleEnum(String filename){
        this.filename = filename;
    }

    public String getFilename(){
        return this.filename;
    }

    // Chapter 7, p. 257 of "Learning LibGDX Development" 2nd Edition
    public static Array<ParticleEnum> toList() {
        Array<ParticleEnum> ret = new Array<ParticleEnum>();

        for(ParticleEnum effect: ParticleEnum.values())
            ret.add(effect);

        return ret;
    }
}
