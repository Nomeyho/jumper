package io.github.nomeyho.jumper.visitor.updater;

import io.github.nomeyho.jumper.objects.Bell;
import io.github.nomeyho.jumper.objects.Player;
import io.github.nomeyho.jumper.visitor.GameObjectVisitor;

public class UpdaterVisitor implements GameObjectVisitor {
    private float delta = 0;

    public UpdaterVisitor (float delta) {
        this.delta = delta;
    }

    @Override
    public void visit(Bell bell) {

    }

    @Override
    public void visit(Player player) {

    }
}
