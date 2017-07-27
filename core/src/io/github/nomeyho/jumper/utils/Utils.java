package io.github.nomeyho.jumper.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.Random;

public class Utils {
    private Utils() {}

    private static Random rand = new Random();

    /**
     * Creates an image of determined size filled with determined color.
     *
     * @param width of an image.
     * @param height of an image.
     * @param color of an image fill.
     * @return Drawable of determined size filled with determined color.
     */
    public static Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return drawable;
    }

    public static boolean randomBool() {
        return MathUtils.randomBoolean();
    }

    public static float randomFloat (float min, float max) {
        return rand.nextFloat() * (max - min) + min;
    }

    public static int randomInt(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    public static <T> T getRandom(T[] array) {
        if(array == null || array.length == 0)
            throw new IllegalArgumentException();
        return array[randomInt(0, array.length - 1)];
    }

    public static void setButtonWidth (Table table) {
        table.layout();

        // Get width
        float width = 0;
        SnapshotArray<Actor> children = table.getChildren();
        Actor child;
        for(int i=0, end = children.size; i<end; ++i) {
            child = children.get(i);
            if(child instanceof TextButton) {
                TextButton button = (TextButton) child;
                if(button.getLabel().getWidth() > width)
                    width = button.getLabel().getWidth();
            }
        }

        // Padding:
        width += 50;

        // Set width
        Array<Cell> cells = table.getCells();
        for(int i=0, end = cells.size; i<end; ++i) {
            Cell cell = cells.get(i);
            if(cell.getActor() instanceof Button)
                cell.width(width).expandX();
            cell.getActor().setSize(width, cell.getActorHeight());
        }
    }
}
