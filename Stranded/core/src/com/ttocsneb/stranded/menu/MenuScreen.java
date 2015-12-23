package com.ttocsneb.stranded.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ttocsneb.stranded.util.screen.AbstractGameScreen;
import com.ttocsneb.stranded.util.screen.DirectedGame;
import com.uwsoft.editor.renderer.SceneLoader;
/**
 * @author TtocsNeb
 *
 *
 */
public class MenuScreen extends AbstractGameScreen {
	
	SceneLoader s1;
	Viewport view;
	
	/**
	 * @param game
	 */
	public MenuScreen(DirectedGame game) {
		super(game);
	}

	@Override
	public void show() {
		initStage();
		
		
	}

	private void initStage() {

		view = new ScalingViewport(Scaling.stretch, 16, 9);
		s1 = new SceneLoader();
		s1.loadScene("MainScene", view);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
				| GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));
		s1.getEngine().update(delta);
		
	}

	@Override
	public void resize(int width, int height) {
		view.update(width, height);
		view.apply();
		
	}

	@Override
	public void pause() {

	}

	@Override
	public void hide() {
	}

	@Override
	public InputProcessor getInputProcessor() {
		return null;
	}

}
