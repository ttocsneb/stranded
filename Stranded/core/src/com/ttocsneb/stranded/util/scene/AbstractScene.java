package com.ttocsneb.stranded.util.scene;

/**
 * Scenes should extend this class.
 * 
 * @author TtocsNeb
 *
 */
public abstract class AbstractScene {

	protected final Scene scene;

	public AbstractScene(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Called when this scene becomes the current scene.
	 */
	public abstract void show();

	/**
	 * Called when the scene should render itself.
	 * 
	 * @param delta
	 */
	public abstract void render(float delta);

	/**
	 * Called when this screen is no longer the current scene.
	 */
	public abstract void close();

	public abstract void resize(int width, int height);

}
