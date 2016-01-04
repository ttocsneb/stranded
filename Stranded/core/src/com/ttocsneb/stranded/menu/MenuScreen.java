package com.ttocsneb.stranded.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ttocsneb.stranded.ashley.Background;
import com.ttocsneb.stranded.game.GameScreen;
import com.ttocsneb.stranded.util.Assets;
import com.ttocsneb.stranded.util.Global;
import com.ttocsneb.stranded.util.screen.AbstractGameScreen;
import com.ttocsneb.stranded.util.screen.DirectedGame;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransition;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransitionFade;

/**
 * @author TtocsNeb
 *
 *
 */
public class MenuScreen extends AbstractGameScreen {

	InputListener input;

	Stage stage;
	
	Engine engine;
	

	/**
	 * @param game
	 * 
	 *            Initialize a new Menu Screen.
	 */
	public MenuScreen(DirectedGame game) {
		super(game);
		input = new InputListener(this);
	}

	@Override
	public void show() {
		initStage();
		
		engine = new Engine();
		
		Background background = new Background((OrthographicCamera)stage.getCamera(),
				Assets.instance.textures.background, true,
				Assets.instance.textures.background.getRegionWidth()*2,
				Assets.instance.textures.background.getRegionHeight()*2);
		engine.addSystem(background);

	}

	/**
	 * Setup the stage, and populate it.
	 */
	private void initStage() {

		// Setup the Stage
		stage = new Stage(new ScreenViewport());
		
		
		Table t = new Table();
		t.setFillParent(true);
		stage.addActor(t);
		
		//Populate the stage with ... stuff.
		TextButton button = new TextButton("Play", Assets.instance.skins.skin);
		button.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ScreenTransition transition = ScreenTransitionFade.init(1);
				game.setScreen(new GameScreen(game), transition);
			}
		});
		t.add(button);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
				| GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));
		
		Global.batch.setProjectionMatrix(stage.getCamera().combined);
		Global.batch.begin();
		engine.update(delta);
		Global.batch.end();
		
		stage.act(delta);
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {

	}

	@Override
	public void hide() {
		stage.dispose();
	}

	@Override
	public InputProcessor getInputProcessor() {
		return input;
	}

}
