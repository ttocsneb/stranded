package com.ttocsneb.stranded.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.gushikustudios.rube.RubeScene;
import com.ttocsneb.stranded.ashley.Background;
import com.ttocsneb.stranded.ashley.CameraController;
import com.ttocsneb.stranded.ashley.Particles;
import com.ttocsneb.stranded.ashley.RubeRenderer;
import com.ttocsneb.stranded.ashley.ShipController;
import com.ttocsneb.stranded.menu.MenuScreen;
import com.ttocsneb.stranded.util.Assets;
import com.ttocsneb.stranded.util.Global;
import com.ttocsneb.stranded.util.InputMultiplexer;
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
	
	InputMultiplexer inputProcessor;
	
	ShipController ship;
	
	CameraController camera;
	
	private Particles particles;
	
	/**
	 * @param game
	 */
	public GameScreen(DirectedGame game) {
		super(game);
	}

	@Override
	public void show() {
		input = new InputListener(this);

		cam = new OrthographicCamera();
		cam.setToOrtho(false, 16, 9);
		
		initStage();
		
		inputProcessor = new InputMultiplexer(input);
		
		camera = new CameraController(cam);
		camera.speed = 0.5f;
		camera.setZoom(0.1f);
	}

	private void initStage() {

		// Setup the Scene

		scene = Global.rubeLoader.loadScene(Gdx.files.internal("rube/spaceStationExplosion.json"));

		engine = new Engine();

		TextureRegion background = Assets.instance.textures.background;
		Background back = new Background(cam, background, true,
				background.getRegionWidth() / 120,
				background.getRegionHeight() / 120);
		engine.addSystem(back);

		particles = new Particles();
		engine.addSystem(particles);
		
		ship = new ShipController(scene.getNamed(Body.class, "ship").first(), particles);
		engine.addSystem(ship);
		
		RubeRenderer renderer = new RubeRenderer(scene.getImages());
		engine.addSystem(renderer);
		


		debug = new Box2DDebugRenderer();
		
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
				| GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));

		scene.getWorld().step(delta, scene.velocityIterations, scene.positionIterations);

		camera.moveTo(ship.ship.getPosition());
		
		camera.update(delta);
		Global.batch.setProjectionMatrix(cam.combined);
		Global.batch.begin();
		engine.update(delta);
		Global.batch.end();

		camera.zoomTo(Math.max(1, ship.ship.getPosition().dst(cam.position.x, cam.position.y)/4));
		
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
		return inputProcessor;
	}

	void back() {
		Gdx.app.debug("GameScreen;back", "Swithing to MenuScreen");
		ScreenTransition transition = ScreenTransitionFade.init(1);
		game.setScreen(new MenuScreen(game), transition);
	}

}
