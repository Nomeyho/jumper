package io.github.nomeyho.jumper;

import com.badlogic.gdx.Game;

public class JumperGame extends Game {
	@Override
	public void create () {
		setScreen(new GameScreen());
	}
}
