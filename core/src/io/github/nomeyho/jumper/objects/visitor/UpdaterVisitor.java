package io.github.nomeyho.jumper.objects.visitor;

import io.github.nomeyho.jumper.objects.Bell;
import io.github.nomeyho.jumper.objects.Player;

public class UpdaterVisitor implements IGameObjectVisitor {
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
