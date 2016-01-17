package com.ttocsneb.stranded.game;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;
import com.ttocsneb.stranded.ashley.Background;
import com.ttocsneb.stranded.ashley.Bullet;
import com.ttocsneb.stranded.ashley.CameraController;
import com.ttocsneb.stranded.ashley.EnemyShip;
import com.ttocsneb.stranded.ashley.Particles;
import com.ttocsneb.stranded.ashley.RubeRenderer;
import com.ttocsneb.stranded.ashley.ShipController;
import com.ttocsneb.stranded.ashley.collision.Box2DCollision;
import com.ttocsneb.stranded.menu.MenuScreen;
import com.ttocsneb.stranded.util.Assets;
import com.ttocsneb.stranded.util.Global;
import com.ttocsneb.stranded.util.InputMultiplexer;
import com.ttocsneb.stranded.util.screen.AbstractGameScreen;
import com.ttocsneb.stranded.util.screen.DirectedGame;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransition;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransitionPixelize;

/**
 * @author TtocsNeb
 *
 */
public class GameScreen extends AbstractGameScreen {

	public static boolean lose;
	public static int points;
	
	boolean renderDebug = false;

	InputListener input;

	RubeScene scene;
	Engine engine;
	OrthographicCamera cam;

	Box2DDebugRenderer debug;

	InputMultiplexer inputProcessor;

	ShipController ship;
	EnemyShip enemy;

	CameraController camera;

	private Particles particles;

	RayHandler lightSystem;
	
	Box2DCollision collisions;
	
	RubeSceneLoader loader;
	
	Stage stage;

	Label score;
	ProgressBar health;
	
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

		initWorld();

		inputProcessor = new InputMultiplexer(input);

		camera = new CameraController(cam);
		camera.speed = 1f;
		camera.setZoom(1f);
		
		initStage();
	}
	
	private void initStage() {
		stage = new Stage(new ScreenViewport());
		
		Table t = new Table();
		t.setPosition(0, 0);
		t.setHeight(100);
		t.setWidth(250);
		stage.addActor(t);
		
		t.add(new Label("Health", Assets.instance.skins.skin));
		t.add(new Label("Score", Assets.instance.skins.skin)).row();;

		
		health = new ProgressBar(0, 100, 0.1f, false, Assets.instance.skins.skin);
		health.setAnimateInterpolation(Interpolation.sine);
		health.setAnimateDuration(0.25f);
		health.setValue(100);
		health.setColor(1, 0, 0, 1);
		t.add(health);
		

		score = new Label("0", Assets.instance.skins.skin);
		t.add(score);
		
	}

	private void initWorld() {

		// Setup the Scene

		loader = new RubeSceneLoader();
		
		scene = loader.loadScene(Gdx.files
				.internal("rube/asteroidField.json"));

		lightSystem = new RayHandler(scene.getWorld(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		lightSystem.setAmbientLight(0.2f);
		lightSystem.setBlurNum(1);

		engine = new Engine();

		TextureRegion background = Assets.instance.textures.background;
		Background back = new Background(cam, background, true,
				background.getRegionWidth() / 60,
				background.getRegionHeight() / 60);
		engine.addSystem(back);

		particles = new Particles();

		Bullet b = new Bullet(scene.getNamed(Body.class, "PlayerBullet"), lightSystem);
		engine.addSystem(b);
		
		ship = new ShipController(scene.getNamed(Body.class, "ship").first(),
				lightSystem, particles, b);
		engine.addSystem(ship);
		
		enemy = new EnemyShip(scene.getNamed(Body.class, "Enemy"), scene.getNamed(Body.class, "ship").first(), particles, lightSystem);
		engine.addSystem(enemy);

		RubeRenderer renderer = new RubeRenderer(scene.getImages());
		engine.addSystem(renderer);

		collisions = new Box2DCollision(ship, enemy, b);
		scene.getWorld().setContactListener(collisions);

		engine.addSystem(particles);
		debug = new Box2DDebugRenderer();

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
				| GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));

		scene.getWorld().step(delta, scene.velocityIterations,
				scene.positionIterations);
		collisions.update();

		camera.moveTo(ship.ship.getPosition());

		camera.update(delta);
		Global.batch.setProjectionMatrix(cam.combined);
		Global.batch.begin();
		engine.update(delta);
		Global.batch.end();
		lightSystem.setCombinedMatrix(cam);
		lightSystem.updateAndRender();

		camera.zoomTo(Math
				.max(1,
						ship.ship.getPosition().dst(cam.position.x,
								cam.position.y) / 4));

		if (renderDebug) debug.render(scene.getWorld(), cam.combined);
		
		stage.act(delta);
		stage.draw();
		
		score.setText("" + points);
		health.setValue(ship.health);
		
		if(MathUtils.randomBoolean(0.005f)) {
			enemy.spawn();
			Gdx.app.debug("GameScreen", "INCOMING!");
		}
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
		ScreenTransition transition = ScreenTransitionPixelize.init(1);
		game.setScreen(new MenuScreen(game), transition);
	}

	/**
	 * 
	 */
	public void reload() {
		game.setScreen(new GameScreen(game), ScreenTransitionPixelize.init(1));
	}

}
