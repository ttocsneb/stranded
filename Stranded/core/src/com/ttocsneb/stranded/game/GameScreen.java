package com.ttocsneb.stranded.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.gushikustudios.rube.RubeScene;
import com.ttocsneb.stranded.ashley.RubeRendererSystem;
import com.ttocsneb.stranded.menu.MenuScreen;
import com.ttocsneb.stranded.util.Global;
import com.ttocsneb.stranded.util.screen.AbstractGameScreen;
import com.ttocsneb.stranded.util.screen.DirectedGame;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransition;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransitionFade;

/**
 * @author TtocsNeb
 *
 */
public class GameScreen extends AbstractGameScreen {

	boolean renderDebug = false;
	
	InputListener input;

	RubeScene scene;
	Engine engine;
	OrthographicCamera cam;

	Box2DDebugRenderer debug;

	/**
	 * @param game
	 */
	public GameScreen(DirectedGame game) {
		super(game);
		input = new InputListener(this);
	}

	@Override
	public void show() {
		initStage();
	}

	private void initStage() {

		// Setup the Scene

		scene = Global.rubeLoader.loadScene(Gdx.files.internal("rube/spaceStationExplosion.json"));

		engine = new Engine();

		RubeRendererSystem renderer = new RubeRendererSystem(scene.getImages());

		engine.addSystem(renderer);

		cam = new OrthographicCamera();
		cam.setToOrtho(false, 16, 9);

		debug = new Box2DDebugRenderer();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
				| GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));

		scene.step();

		cam.update();
		Global.batch.setProjectionMatrix(cam.combined);
		Global.batch.begin();
		engine.update(delta);
		Global.batch.end();
		
		if(renderDebug)
			debug.render(scene.getWorld(), cam.combined);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void hide() {

	}

	@Override
	public InputProcessor getInputProcessor() {
		return input;
	}

	void back() {
		Gdx.app.debug("GameScreen;back", "Swithing to MenuScreen");
		ScreenTransition transition = ScreenTransitionFade.init(1);
		game.setScreen(new MenuScreen(game), transition);
	}

}
