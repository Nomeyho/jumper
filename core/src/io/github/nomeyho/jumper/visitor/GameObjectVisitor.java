package io.github.nomeyho.jumper.visitor;

import io.github.nomeyho.jumper.objects.Bell;
import io.github.nomeyho.jumper.objects.Player;

public interface GameObjectVisitor {
    public void visit(Bell bell);
    public void visit(Player player);
}
