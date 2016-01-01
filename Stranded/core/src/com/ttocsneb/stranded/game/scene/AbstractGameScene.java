package com.ttocsneb.stranded.game.scene;

import com.gushikustudios.rube.RubeScene;
import com.ttocsneb.stranded.util.scene.AbstractScene;
import com.ttocsneb.stranded.util.scene.Scene;

/**
 * This class is designed specifically for Game Scenes, and should only be
 * extended if part of the game screen.
 * 
 * @author TtocsNeb
 *
 */
public abstract class AbstractGameScene extends AbstractScene {

	/**
	 * @param scene
	 */
	public AbstractGameScene(Scene scene) {
		super(scene);
	}

	/**
	 * Get the RubeScene from the current scene.
	 * 
	 * @return
	 */
	public abstract RubeScene getScene();

}
