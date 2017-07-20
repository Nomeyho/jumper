package io.github.nomeyho.jumper.collisions;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;
import io.github.nomeyho.jumper.Application;

public class HitboxLoader extends
        AsynchronousAssetLoader<HitboxAtlas,HitboxLoader.HitboxParameter> {

    public HitboxLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    HitboxAtlas atlas;

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, HitboxLoader.HitboxParameter parameter) {
        this.atlas = null;
        HitboxAtlas atlas = new HitboxAtlas();

        // Format from:
        // http://www.aurelienribon.com/blog/projects/physics-body-editor/
        Gdx.app.log(Application.TAG, "Parsing hitbox atlas...");
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file.readString());

        JsonValue bodies = root.get("rigidBodies");
        Gdx.app.log(Application.TAG, "Found " + bodies.size + " bodies");
        for(JsonValue body: bodies) {
            Gdx.app.log(Application.TAG, "Body: " + body.getString("name"));
            JsonValue polygons = body.get("polygons");
        }

        this.atlas = atlas;
        // TODO
    }

    @Override
    public HitboxAtlas loadSync(AssetManager manager, String fileName, FileHandle file, HitboxLoader.HitboxParameter parameter) {
        HitboxAtlas atlas = this.atlas;
        this.atlas = null;
        return atlas;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, HitboxLoader.HitboxParameter parameter) {
        return null;
    }

    static public class HitboxParameter extends AssetLoaderParameters<HitboxAtlas> {}
}
