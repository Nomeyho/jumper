package io.github.nomeyho.jumper.collisions;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
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

        // List of bodies (= textures)
        JsonValue bodies = root.get("rigidBodies");
        Gdx.app.log(Application.TAG, "Found " + bodies.size + " bodies");
        for(JsonValue body: bodies) {
            // List of polygons - assume to have the origin at (0,0)
            JsonValue polygons = body.get("polygons");
            Array<Polygon> _polygons = new Array<Polygon>();
            for(JsonValue polygon: polygons) {
                // List of vertices
                Gdx.app.log(Application.TAG, body.getString("name") + "(" + polygon.size + " points)");
                float[] vertices = new float[polygon.size * 2];
                int idx = 0;
                for(JsonValue vertex: polygon) {
                    vertices[idx++] = vertex.getFloat("x");
                    vertices[idx++] = vertex.getFloat("y");
                }
                _polygons.add(new Polygon(vertices));
            }
            atlas.hitboxes.put(body.getString("name"), new Hitbox(_polygons));
        }

        this.atlas = atlas;
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
