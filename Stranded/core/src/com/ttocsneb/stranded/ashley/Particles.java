package com.ttocsneb.stranded.ashley;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.ttocsneb.stranded.util.Global;

/**
 * Handles Particles for the game.
 * 
 * @author TtocsNeb
 *
 */
public class Particles extends EntitySystem implements Disposable {

	private Array<PooledEffect> effects;

	public Particles() {
		effects = new Array<PooledEffect>();
	}

	/**
	 * Add an effect to the pool
	 * 
	 * @param effect
	 */
	public void addEffect(PooledEffect effect) {
		effects.add(effect);
	}

	@Override
	public void update(float delta) {
		// Go through each effect, and draw it.
		for (PooledEffect e : effects) {
			e.draw(Global.batch, delta);
			// Kill the effect if it has lived a full life.
			if (e.isComplete()) {
				e.free();
				effects.removeValue(e, true);
			}
		}
	}

	@Override
	public void dispose() {
		// Go through each effect, and set it free!
		for (PooledEffect e : effects)
			// Be free effect!
			e.free();
		// remove all dead effects from the array.
		effects.clear();
	}

}
