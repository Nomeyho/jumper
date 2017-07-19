package io.github.nomeyho.jumper;

import com.badlogic.gdx.Game;
import io.github.nomeyho.jumper.UI.LoadingScreen;

public class JumperGame extends Game {
	@Override
	public void create () {
		setScreen(new LoadingScreen(this));
	}
}
