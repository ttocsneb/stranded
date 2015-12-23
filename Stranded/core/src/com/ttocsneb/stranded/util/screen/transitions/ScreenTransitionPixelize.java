package com.ttocsneb.stranded.util.screen.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;

public class ScreenTransitionPixelize implements ScreenTransition {
	private static final ScreenTransitionPixelize instance = new ScreenTransitionPixelize();
	
	private ScreenTransitionPixelize() {
		pix = new ShaderProgram(Gdx.files.internal("shaders/pix.vert"), Gdx.files.internal("shaders/pix.frag"));
	}
	
	private float duration;
	
	private ShaderProgram pix;
	
	/**
	 * Initiate the TransitionPixelize Transition.
	 * @param duration
	 * @return
	 */
	public static ScreenTransitionPixelize init(float duration) {
		instance.duration = duration;
		
		instance.pix.begin();
		instance.pix.setUniformf("u_amount", Gdx.graphics.getWidth());
		instance.pix.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		instance.pix.end();
		return instance;
	}
	
	@Override
	public float getDuration() {
		return instance.duration;
	}

	@Override
	public void render(SpriteBatch batch, Texture currScreen,
			Texture nextScreen, float alpha) {

		
		
		float w = currScreen.getWidth();
		float h = currScreen.getHeight();
		
		Texture tex = null;
		
		if(alpha > 0.5f) {
			tex = nextScreen;

			alpha = Interpolation.circleIn.apply((alpha-0.5f)*2);

			pix.begin();
			pix.setUniformf("u_amount", w*alpha);
			pix.end();
		} else {
			tex = currScreen;
			
			alpha = Interpolation.circleOut.apply(alpha*2);

			pix.begin();
			pix.setUniformf("u_amount", w*(1-alpha));
			pix.end();
		}
		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		batch.setShader(pix);
		
		batch.begin();
		batch.setColor(1, 1, 1, 1);
		batch.draw(tex, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, tex.getWidth(), tex.getHeight(), false, true);
		batch.end();
		
		batch.setShader(null);
		
	}

}
