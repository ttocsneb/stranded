package com.ttocsneb.stranded.util.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.ttocsneb.stranded.util.Assets;

public abstract class AbstractGameScreen implements Screen {
	protected DirectedGame game;
	
	public AbstractGameScreen(DirectedGame game) {
		this.game = game;
	}
	
	@Override
	public abstract void show();
	@Override
	public abstract void render(float delta);
	@Override
	public abstract void resize(int width, int height);
	@Override
	public abstract void pause();
	@Override
	public abstract void hide();

	@Override
	public void resume() {
		if(!Assets.instance.isLoaded())
			Assets.instance.init(new AssetManager());
	}

	@Override
	public void dispose() {
		Assets.instance.dispose();
	}
	
	/**
	 * Get the Input processor.
	 * @return Input Processor
	 */
	public abstract InputProcessor getInputProcessor();
	
}

