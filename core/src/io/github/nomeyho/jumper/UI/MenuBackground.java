package io.github.nomeyho.jumper.UI;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.nomeyho.jumper.Application;
import io.github.nomeyho.jumper.utils.AnimationWrapper;
import io.github.nomeyho.jumper.utils.Utils;

public class MenuBackground extends Actor {
    private static final float TIME = 50;
    private float time;
    private AnimationWrapper animation;
    private float width;
    private float height;
    private Vector2[] path;
    private Bezier<Vector2> spline;
    private Vector2 tmp;

    public MenuBackground() {
        this.time = 0;

        // Animation
        this.animation = new AnimationWrapper(0.15f, "rocket", Application.ROCKET_ANIM_ATLAS, Animation.PlayMode.LOOP_PINGPONG);

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

        // Angle
        this.spline.derivativeAt(tmp, progress);
        float angle = tmp.angle() - 90;
        // Position
        this.spline.valueAt(this.tmp, progress);

        // Make sur the rocket leave the screen and restart with a new path
        if(progress > 1 && (this.tmp.x < -this.width || this.tmp.x > Application.worldWidth || this.tmp.y < -this.height || this.tmp.y > Application.worldHeight)) {
            init();
            this.time = 0;
        }

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

    private boolean isInside (Vector2 pos) {
        // return pos.x < 0 || pos.x > this.width ||
        return false;
    }

    /**
     * Mandatory to be called before being usable
     */
    public void init () {
        float w = Application.worldWidth;
        float h = Application.worldHeight;

        this.path = new Vector2[4];
        this.path[0] = new Vector2(
                Utils.randomFloat(- w / 2, 0), // outside
                Utils.randomFloat(- h / 2, 0)
        );
        this.path[1] = new Vector2(
                Utils.randomFloat(w / 2, w + w/2),
                Utils.randomFloat(0, h / 2)
        );
        this.path[2] = new Vector2(
                Utils.randomFloat(w / 2, w + w/2),
                Utils.randomFloat(h / 2, h)
        );
        this.path[3] = new Vector2(
                Utils.randomFloat(0, w / 2),
                Utils.randomFloat(h / 2, h)
        );

        this.spline = new Bezier<Vector2>(this.path);

        /*
        http://blog.meltinglogic.com/2013/12/how-to-generate-procedural-racetracks/

        // Generate randoms points
        FloatArray points = new FloatArray(2 * NB_POINTS);
        for (int i = 0, end = 2 * NB_POINTS; i < end; i=i+2) {
            points.add(Utils.randomFloat(border, w));
            points.add(Utils.randomFloat(border, h));
        }

        // Avoid "little loops" and "turn around"
        for(int i=0; i<100; ++i)
            smoothPoints(points, 15);

        // Compute convex hull
        ConvexHull convexHull = new ConvexHull();
        FloatArray polygon = convexHull.computePolygon(points, false);

        // Convert
        this.path = new Vector2[polygon.size / 2];
        for (int i = 0, end = polygon.size; i < end; i=i+2)
            this.path[i / 2] = new Vector2(polygon.get(i), polygon.get(i + 1));

        // Compute spline for smooth curves
        this.spline = new Bezier<Vector2>(this.path);
        */
    }

    /*
    private void smoothPoints(FloatArray points, int dst) {
        float hx, hy, hl;

        for(int i = 0, end1 = points.size;  i < end1; i = i + 2) {
            for(int j = i + 2, end2 = points.size; j < end2; j = j + 2) {
                hx = points.get(i) - points.get(j);
                hy = points.get(i+1) - points.get(j + 1);
                hl = (float) Math.sqrt(hx*hx + hy*hy);

                // Too close
                if(hl < dst) {
                    float dif = dst - hl;
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
    */

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        int k = 100;
        Vector2 tmp1 = new Vector2();
        Vector2 tmp2 = new Vector2();

        for(int i = 0; i < k-1; ++i)
        {
            shapeRenderer.line(
                    this.spline.valueAt(tmp1, ((float)i)/((float)k-1)),
                    this.spline.valueAt(tmp2, ((float)(i+1))/((float)k-1))
            );
        }
    }
}
