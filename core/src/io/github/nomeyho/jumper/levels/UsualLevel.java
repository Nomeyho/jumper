package io.github.nomeyho.jumper.levels;

import io.github.nomeyho.jumper.objects.Bell;
import io.github.nomeyho.jumper.objects.Player;

public class UsualLevel extends AbstractLevel {
    public UsualLevel () {
        super();
        this.player = new Player(0, 0, 0);
        this.objects.add( new Bell(500, 500, 0) );
        this.objects.add( new Bell(900, 1200, 0) );
    }
}
