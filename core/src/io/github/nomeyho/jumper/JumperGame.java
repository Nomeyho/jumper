package io.github.nomeyho.jumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import io.github.nomeyho.jumper.UI.LoadingScreen;
import io.github.nomeyho.jumper.files.UserPreferences;
import io.github.nomeyho.jumper.collisions.HitboxAtlas;
import io.github.nomeyho.jumper.collisions.HitboxLoader;
import io.github.nomeyho.jumper.lang.LanguageManager;
import io.github.nomeyho.jumper.sound.SoundManager;

public class JumperGame extends AbstractGame {

	public JumperGame(AdService service) {
		super(service);
	}

	@Override
	public void create () {
		if(Application.DEBUG)
			Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_DEBUG);
		else
			Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_ERROR);

		Application.get().assetManager.setLoader(HitboxAtlas.class, new HitboxLoader(new InternalFileHandleResolver()));
		Application.get().loadUIAssets();
		UserPreferences.get().load();
		LanguageManager.get().setLang(UserPreferences.get().lang);
		Gdx.input.setInputProcessor(Application.get().inputMultiplexer);
		setScreen(new LoadingScreen(this));
	}

	@Override
	public void resume () {
		super.pause();
	}

	@Override
	public void pause () {
		super.pause();
	}

	@Override
	public void dispose () {
		super.dispose(); // = screen.dispose() = screen.hide()
		// TODO dispose everything (game objects, managers? not only screens)
		SoundManager.get().dispose();
		Application.get().assetManager.dispose();
		Application.get().shapeRenderer.dispose();
	}

	@Override
	public void render () {
		super.render();
	}
}
