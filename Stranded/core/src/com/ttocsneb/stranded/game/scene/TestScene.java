package com.ttocsneb.stranded.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.gushikustudios.rube.RubeScene;
import com.ttocsneb.stranded.ashley.RubeRendererSystem;
import com.ttocsneb.stranded.util.Global;
import com.ttocsneb.stranded.util.scene.Scene;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransitionFade;

/**
 * THis is a test to show that multiple scenes can work together.
 * 
 * @author TtocsNeb
 *
 */
public class TestScene extends AbstractGameScene {

	RubeScene rubeScene;

	RubeRendererSystem renderer;

	/**
	 * @param scene
	 */
	public TestScene(Scene scene) {
		super(scene);
	}

	@Override
	public RubeScene getScene() {
		return rubeScene;
	}

	@Override
	public void show() {
		rubeScene = Global.rubeLoader.loadScene(Gdx.files
				.internal("rube/testShip.json"));

		renderer = new RubeRendererSystem(rubeScene.getImages());
	}

	@Override
	public void render(float delta) {
		rubeScene.getWorld().step(delta, rubeScene.velocityIterations,
				rubeScene.positionIterations);

		// Change scenes when the B key is pressed.
		if (Gdx.input.isKeyPressed(Keys.B)) {
			scene.setScene(new ExplodingStationScene(scene),
					ScreenTransitionFade.init(0.5f));
		}

		renderer.update(delta);
	}

	@Override
	public void close() {

	}

	@Override
	public void resize(int width, int height) {

	}

}
