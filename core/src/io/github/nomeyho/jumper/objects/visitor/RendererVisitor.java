package io.github.nomeyho.jumper.objects.visitor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.nomeyho.jumper.objects.Bell;
import io.github.nomeyho.jumper.objects.Player;

public class RendererVisitor implements IGameObjectVisitor {
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
