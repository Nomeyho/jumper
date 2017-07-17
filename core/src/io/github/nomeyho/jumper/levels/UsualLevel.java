package io.github.nomeyho.jumper.levels;

import io.github.nomeyho.jumper.objects.Bell;

public class UsualLevel extends AbstractLevel {
    public UsualLevel () {
        super();
        this.objects.add( new Bell(200, 500, 0) );
        this.objects.add( new Bell(900, 1200, 0) );
    }
}
