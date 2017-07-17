package io.github.nomeyho.jumper.visitor.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.objects.Bell;
import io.github.nomeyho.jumper.objects.Player;
import io.github.nomeyho.jumper.visitor.GameObjectVisitor;

public class RendererVisitor implements GameObjectVisitor {
    private SpriteBatch batch;

    public RendererVisitor(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void visit(Bell bell) {
        batch.draw(bell.bellTexture,
                   bell.location.getX(),
                   bell.location.getY(),
                   Bell.WIDTH,
                   Bell.HEIGHT);
    }

    @Override
    public void visit(Player player) {
        batch.draw(player.playerTexture,
                   player.location.getX(),
                   player.location.getY(),
                   Player.WIDTH,
                   Player.HEIGHT);
    }
}
