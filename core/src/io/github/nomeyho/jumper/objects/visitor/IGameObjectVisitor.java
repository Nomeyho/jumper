package io.github.nomeyho.jumper.objects.visitor;

import io.github.nomeyho.jumper.objects.Bell;
import io.github.nomeyho.jumper.objects.Player;

public interface IGameObjectVisitor {
    public void visit(Bell bell);
    public void visit(Player player);
}
