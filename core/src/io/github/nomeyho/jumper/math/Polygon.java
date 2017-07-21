package io.github.nomeyho.jumper.math;

import com.badlogic.gdx.utils.StringBuilder;

public class Polygon extends com.badlogic.gdx.math.Polygon {

    public Polygon () {
        super();
    }

    public Polygon (float[] vertices) {
        super(vertices);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        float[] vertices = this.getTransformedVertices();
        for(int i=0; i<vertices.length; ++i) {
            if(i%2 == 0) {
                builder.append("(");
                builder.append(vertices[i]);
            }
            else {
                builder.append(",");
                builder.append(vertices[i]);
                builder.append(") ");
            }
        }
        return builder.toString();
    }
}
