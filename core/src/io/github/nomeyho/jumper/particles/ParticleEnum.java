package io.github.nomeyho.jumper.particles;


import com.badlogic.gdx.utils.Array;

public enum ParticleEnum {
    SMOKE("effects/smoke", false),
    DUST("effects/dust", true),
    FIRE("effects/fire", false);

    private String filename;
    private boolean pooled;

    private ParticleEnum(String filename, boolean pooled){
        this.filename = filename;
        this.pooled = pooled;
    }

    public String getFilename(){
        return this.filename;
    }

    public boolean isPooled(){
        return this.pooled;
    }

    // Chapter 7, p. 257 of "Learning LibGDX Development" 2nd Edition
    public static Array<ParticleEnum> toList() {
        Array<ParticleEnum> ret = new Array<ParticleEnum>();

        for(ParticleEnum effect: ParticleEnum.values())
            ret.add(effect);

        return ret;
    }
}
