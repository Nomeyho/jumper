package io.github.nomeyho.jumper.collisions;

import com.badlogic.gdx.utils.ObjectMap;

public class HitboxAtlas {
    public ObjectMap<String, Hitbox> hitboxes;

    public HitboxAtlas() {
        this.hitboxes = new ObjectMap<String, Hitbox>();
    }
}
