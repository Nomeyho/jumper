package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.ConvexHull;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.FloatArray;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.utils.AnimationWrapper;
import io.github.nomeyho.jumper.utils.Utils;

public class MenuScreenBackground extends Actor {
    private static final int NB_POINTS = 20;
    private static final float TIME = 200; // 1000 sec to finish the full path
    private float time;
    private AnimationWrapper animation;
    private float width;
    private float height;
    private Vector2[] path;
    private CatmullRomSpline<Vector2> spline;
    private Vector2 tmp;

    public MenuScreenBackground () {
        this.time = 0;

        // Animation
        TextureAtlas atlas = Application.get().assetManager.get(Application.ROCKET_ANIM_ATLAS);
        this.animation = new AnimationWrapper(0.05f, atlas.findRegions("rocket"));
        this.animation.animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        // Size
        TextureRegion frame = this.animation.getCurrentTexture();
        this.width = 80;
        this.height = frame.getRegionHeight() * (this.width / frame.getRegionWidth());

        this.tmp = new Vector2();
    }

    @Override
    public void act (float delta) {
        time += delta;
        this.animation.update(delta);
    }

    @Override
    public void draw (Batch batch, float parentDelta) {
        // Current frame of the animation to display
        TextureRegion frame = this.animation.getCurrentTexture();
        // Progress
        float progress = this.time / TIME;

        // Make sur the rocket leave the screen and restart with a new path
        if(progress >= 1.2) {
            init();
            this.time = 0;
        }
        // System.out.println("ici " + progress);

        // Angle
        this.spline.derivativeAt(tmp, progress);
        float angle = tmp.angle() - 90;
        // Position
        this.spline.valueAt(this.tmp, progress);

        batch.draw(
                frame,
                this.tmp.x,
                this.tmp.y,
                0,
                0,
                this.width,
                frame.getRegionHeight() * (this.width / frame.getRegionWidth()),
                1,
                1,
                angle
        );
    }

    /**
     * Mandatory to be called before being usable
     * Based on:
     * http://blog.meltinglogic.com/2013/12/how-to-generate-procedural-racetracks/
     */
    public void init () {
        float border = Math.max(this.width, this.height);
        float w = Application.worldWidth - border;
        float h = Application.worldHeight - border;

        // Generate randoms points
        FloatArray points = new FloatArray(2 * NB_POINTS);
        for (int i = points.size, end = 2 * NB_POINTS; i < end; i=i+2) {
            points.add(Utils.randomFloat(border, w));
            points.add(Utils.randomFloat(border, h));
        }

        // Avoid "little loops" and "turn around"
        smoothPoints(points);

        // Compute convex hull
        ConvexHull convexHull = new ConvexHull();
        FloatArray polygon = convexHull.computePolygon(points, true);

        // Convert
        this.path = new Vector2[polygon.size / 2];
        for (int i = 0, end = polygon.size; i < end; i=i+2)
            this.path[i / 2] = new Vector2(polygon.get(i), polygon.get(i + 1));

        // Compute spline for smooth curves
        this.spline = new CatmullRomSpline<Vector2>(this.path, true);
    }

    private void smoothPoints(FloatArray points) {
        float MIN_DIST = 60;
        float hx, hy, hl;

        for(int i = 0, end1 = points.size;  i < end1; i = i + 2) {
            for(int j = i + 2, end2 = points.size; j < end2; j = j + 2) {
                hx = points.get(i) - points.get(j);
                hy = points.get(i+1) - points.get(j + 1);
                hl = (float) Math.sqrt(hx*hx + hy*hy);

                // Too close
                if(hl < MIN_DIST) {
                    float dif = MIN_DIST - hl;
                    hx = (hx / hl) * dif;
                    hy = (hy / hl) * dif;
                    points.set(i, points.get(i) + hx);
                    points.set(i + 1, points.get(i + 1) + hy);
                    points.set(j, points.get(j) - hx);
                    points.set(j + 1, points.get(j + 1) - hy);
                }
            }
        }
    }
}
