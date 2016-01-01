package com.ttocsneb.stranded.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.gushikustudios.rube.RubeScene;
import com.ttocsneb.stranded.ashley.RubeRendererSystem;
import com.ttocsneb.stranded.util.Global;
import com.ttocsneb.stranded.util.scene.Scene;

/**
 * This is the scene where the station explodes.
 * 
 * @author TtocsNeb
 *
 */
public class ExplodingStationScene extends AbstractGameScene {

	private RubeScene rubeScene;

	private RubeRendererSystem renderer;

	/**
	 * @param scene
	 */
	public ExplodingStationScene(Scene scene) {
		super(scene);
	}

	@Override
	public void show() {
		rubeScene = Global.rubeLoader.loadScene(Gdx.files
				.internal("rube/spaceStationExplosion.json"));
		renderer = new RubeRendererSystem(rubeScene.getImages());
	}

	@Override
	public void render(float delta) {
		rubeScene.getWorld().step(delta, rubeScene.velocityIterations,
				rubeScene.positionIterations);

		// Change Scenes when the T key is pressed.
		if (Gdx.input.isKeyPressed(Keys.T)) {
			scene.setScene(new TestScene(scene));
		}

		renderer.update(delta);
	}

	@Override
	public void close() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public RubeScene getScene() {
		return rubeScene;
	}

}
