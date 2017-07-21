package io.github.nomeyho.jumper.collisions;

import com.badlogic.gdx.utils.ObjectMap;

public class HitboxAtlas {
    public ObjectMap<String, Hitbox> hitboxes;

    public HitboxAtlas() {
        this.hitboxes = new ObjectMap<String, Hitbox>();
    }

    public Hitbox get (String key) {
        if(!this.hitboxes.containsKey(key))
            throw new IllegalArgumentException("Hitbox " + key + "does not exist");
        // Get a copy
        return this.hitboxes.get(key).copy();
    }
}
