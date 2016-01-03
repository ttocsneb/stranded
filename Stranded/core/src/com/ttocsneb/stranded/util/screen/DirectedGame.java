package com.ttocsneb.stranded.util.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransition;

public abstract class DirectedGame implements ApplicationListener {

	// WARNING: I am too lazy to add internal comments to this class, enter at
	// your own risk.

	private boolean init;
	private AbstractGameScreen currScreen;
	private AbstractGameScreen nextScreen;
	private FrameBuffer currFbo;
	private FrameBuffer nextFbo;
	private SpriteBatch batch;
	private float t;
	private ScreenTransition screenTransition;

	public void setScreen(AbstractGameScreen screen) {
		setScreen(screen, null);
	}

	/**
	 * Set the active screen, and transition to it.
	 * 
	 * @param screen
	 * @param screenTransition
	 */
	public void setScreen(AbstractGameScreen screen,
			ScreenTransition screenTransition) {
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
		nextScreen = screen;
		nextScreen.show();
		nextScreen.resize(w, h);
		nextScreen.render(0);
		if (currScreen != null) currScreen.pause();
		nextScreen.pause();
		Gdx.input.setInputProcessor(null);
		this.screenTransition = screenTransition;
	}

	@Override
	public void create() {

	}

	@Override
	public void resize(int width, int height) {
		if(nextScreen != null)
			nextScreen.resize(width, height);
		else if(currScreen != null)
			currScreen.resize(width, height);
	}
	
	private float averageDelta = 1/60f;

	@Override
	public void render() {
		// get delta time and ensure an upper limit of one 30th second.
		averageDelta = (averageDelta + Gdx.graphics.getDeltaTime())/2f;
		float delta = Math.min(averageDelta, 1 / 30f);
		
		if (nextScreen == null) {
			// no ongoing transition
			if (currScreen != null) currScreen.render(delta);
		} else {
			// ongoing transition.
			float duration = 0;
			if (screenTransition != null)
				duration = screenTransition.getDuration();
			// update progress of ongoing transition.
			t = Math.min(t + delta, duration);
			if (screenTransition == null || t >= duration) {
				if (currScreen != null) currScreen.hide();
				nextScreen.resume();
				// enable input for nextScreen.
				Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
				// switch screens.
				currScreen = nextScreen;
				nextScreen = null;
				screenTransition = null;
			} else {
				// render screens to FBOs.
				currFbo.begin();
				if (currScreen != null) currScreen.render(delta);
				currFbo.end();
				nextFbo.begin();
				nextScreen.render(delta);
				nextFbo.end();
				// render transition effect to screen.
				float alpha = t / duration;
				batch.begin();
				screenTransition.render(batch, currFbo.getColorBufferTexture(),
						nextFbo.getColorBufferTexture(), alpha);
				batch.end();
			}
		}
	}

	@Override
	public void pause() {
		if (currScreen != null) currScreen.pause();
	}

	@Override
	public void resume() {
		if (currScreen != null) currScreen.resume();
	}

	@Override
	public void dispose() {
		if (currScreen != null) currScreen.hide();
		if (nextScreen != null) nextScreen.hide();
		if (init) {
			currFbo.dispose();
			currScreen = null;
			nextFbo.dispose();
			nextScreen = null;
			batch.dispose();
			init = false;
		}
	}

}