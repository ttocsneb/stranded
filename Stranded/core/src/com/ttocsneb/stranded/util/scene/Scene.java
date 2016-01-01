package com.ttocsneb.stranded.util.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.ttocsneb.stranded.game.scene.AbstractGameScene;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransition;

/**
 * This class manages, and renders {@link AbstractGameScene}.
 * 
 * @author TtocsNeb
 * 
 */
public class Scene implements Disposable {

	private boolean init;
	private AbstractScene currScene;
	private AbstractScene nextScene;
	private FrameBuffer currFbo;
	private FrameBuffer nextFbo;
	private float t;
	private ScreenTransition screenTransition;
	private SceneChangeListener listener;
	private SpriteBatch batch;

	/**
	 * Set the active scene.
	 * 
	 * @param scene
	 */
	public void setScene(AbstractScene scene) {
		setScene(scene, null);
	}

	/**
	 * Set the active scene, and transition to it.
	 * 
	 * @param scene
	 * @param screenTransition
	 */
	public void setScene(AbstractScene scene, ScreenTransition screenTransition) {
		t = 0;
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		if (!init) {
			currFbo = new FrameBuffer(Format.RGB565, w, h, false);
			nextFbo = new FrameBuffer(Format.RGB565, w, h, false);
			batch = new SpriteBatch();
			init = true;
		}
		// Start a new Transition
		nextScene = scene;
		nextScene.show();
		nextScene.resize(w, h);
		this.screenTransition = screenTransition;
		if (listener != null) listener.sceneChanged(nextScene);
	}

	public void resize(int width, int height) {
		if (nextScene != null)
			nextScene.resize(width, height);
		else if (currScene != null) currScene.resize(width, height);
	}

	public void render(float delta) {
		if (nextScene == null) {
			// No active Transition
			if (currScene != null) currScene.render(delta);
		} else {
			// There is an ongoing transition.
			float duration = 0;
			if (screenTransition != null)
				duration = screenTransition.getDuration();
			// update progress of ongoing transition.
			t = Math.min(t + delta, duration);
			if (screenTransition == null || t >= duration) {
				// Close the current Scene
				if (currScene != null) currScene.close();
				// switch screens.
				currScene = nextScene;
				nextScene = null;
				screenTransition = null;
			} else {
				// Render scenes to FBOs.
				currFbo.begin();
				if (currScene != null) currScene.render(delta);
				currFbo.end();
				nextFbo.begin();
				nextScene.render(delta);
				nextFbo.end();
				// Render transition effect to scene.
				float alpha = t / duration;
				batch.begin();
				screenTransition.render(batch, currFbo.getColorBufferTexture(),
						nextFbo.getColorBufferTexture(), alpha);
				batch.end();
			}
		}
	}

	public interface SceneChangeListener {

		/**
		 * Called when the scene has changed.
		 * 
		 * @param scene
		 *            the current scene.
		 */
		public void sceneChanged(AbstractScene scene);
	}

	public void setSceneListener(SceneChangeListener listener) {
		this.listener = listener;
	}

	public AbstractScene getScene() {
		return currScene;
	}

	@Override
	public void dispose() {
		if (currScene != null) currScene.close();
		if (nextScene != null) nextScene.close();
		if (init) {
			currFbo.dispose();
			nextFbo.dispose();
			batch.dispose();
			currScene = null;
			nextScene = null;
			init = false;

		}
	}

}
