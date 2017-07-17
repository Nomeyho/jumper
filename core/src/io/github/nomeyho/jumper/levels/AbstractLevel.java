package io.github.nomeyho.jumper.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.github.nomeyho.jumper.objects.AbstractGameObject;
import io.github.nomeyho.jumper.objects.Player;
import io.github.nomeyho.jumper.objects.visitor.RendererVisitor;
import io.github.nomeyho.jumper.objects.visitor.UpdaterVisitor;

public abstract class AbstractLevel {
    // GameObject
    public Array<AbstractGameObject> objects = new Array<AbstractGameObject>();
}
