package com.ttocsneb.stranded.ashley;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.ttocsneb.stranded.util.Assets;

/**
 * @author TtocsNeb
 *
 */
public class ShipController extends EntitySystem {

	public Body ship;

	private float acceleration = 5;
	private float angularAcceleration = 0.5f;

	private PooledEffect thruster;
	private PooledEffect turn;

	Particles particles;

	private boolean thrustActivated;
	private boolean turnActivated;

	public ShipController(Body ship, Particles particles) {
		this.ship = ship;

		this.particles = particles;

		thruster = Assets.instance.particles.fire.obtain();// Obtain that fire!
		thrustActivated = false;
		thruster.scaleEffect(0.25f);

		turn = Assets.instance.particles.fire.obtain();
		turnActivated = false;
		turn.scaleEffect(0.141f);
		turn.getEmitters().get(0).getTint().setColors(new float[] {1, 1, 1, 0.5f});

	}

	@Override
	public void addedToEngine(Engine engine) {

	}

	/**
	 * Set data for effects
	 * 
	 * @param effect
	 * @param distance
	 *            distance from the center of the ship
	 * @param rotation
	 *            offset in radians
	 */
	private void setData(PooledEffect effect, float distance, float rotation, float delta) {
		float angle = ship.getAngle() + rotation;

		effect.setPosition(
				ship.getPosition().x
						+ distance
						* MathUtils
								.sin((float) (2 * Math.PI - angle + 0.5f * Math.PI)) + ship.getLinearVelocity().x*delta,
				ship.getPosition().y
						+ distance
						* MathUtils
								.cos((float) (2 * Math.PI - angle + 0.5f * Math.PI)) + ship.getLinearVelocity().y*delta);

		effect.getEmitters().get(0).getAngle()
				.setHighMin(angle * MathUtils.radiansToDegrees - 15);
		effect.getEmitters().get(0).getAngle()
				.setHighMax(angle * MathUtils.radiansToDegrees + 15);
		effect.getEmitters().get(0).getAngle()
				.setLow(angle * MathUtils.radiansToDegrees);
		
		effect.getEmitters().get(0).getWind().setHigh(ship.getLinearVelocity().x);
		effect.getEmitters().get(0).getGravity().setHigh(ship.getLinearVelocity().y);
	}

	@Override
	public void update(float delta) {

		// ////////// Ship Controls /////////////////////
		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
			// Move the ship forwards if the up arrow/W key is pressed

			setData(thruster, 0.25f, (float) (1.5f * Math.PI), delta);

			if (!thrustActivated) {
				particles.addEffect(thruster);
				thrustActivated = true;
			}

			ship.applyForceToCenter(
					acceleration
							* ship.getMass()
							* MathUtils.sin((float) (2 * Math.PI - ship
									.getAngle())),
					acceleration
							* ship.getMass()
							* MathUtils.cos((float) (2 * Math.PI - ship
									.getAngle())), true);

		} else {
			thruster.allowCompletion();
			thruster = Assets.instance.particles.fire.obtain();
			thruster.scaleEffect(0.25f);
			thrustActivated = false;
		}
		// Turn the ship
		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {

			setData(turn, 0.141f, (float) (0.25f * Math.PI), delta);

			if (!turnActivated) {
				particles.addEffect(turn);
				turnActivated = true;
			}

			ship.applyTorque(angularAcceleration * ship.getMass(), true);

		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)
				|| Gdx.input.isKeyPressed(Keys.D)) {

			setData(turn, 0.141f, (float) (0.75f * Math.PI), delta);

			if (!turnActivated) {
				particles.addEffect(turn);
				turnActivated = true;
			}

			ship.applyTorque(-angularAcceleration * ship.getMass(), true);

		} else if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
				|| Gdx.input.isKeyPressed(Keys.NUM_0)) {
			// Stability Assist.
			if (Math.abs(ship.getAngularVelocity()) > 0.1f) {

				ship.applyTorque((ship.getAngularVelocity() > 0 ? -1 : 1)
						* angularAcceleration * ship.getMass(), true);

				setData(turn,
						0.141f,
						(float) ((ship.getAngularVelocity() > 0 ? 0.75f : 0.25f) * Math.PI),
						delta);

				if (!turnActivated) {
					particles.addEffect(turn);
					turnActivated = true;
				}
			} else {
				ship.setAngularVelocity(0);
				
				turn.allowCompletion();
				turn = Assets.instance.particles.fire.obtain();
				turn.scaleEffect(0.1414f);
				turnActivated = false;
				turn.getEmitters().get(0).getTint().setColors(new float[] {1, 1, 1, 0.5f});
			}

		} else {
			turn.allowCompletion();
			turn = Assets.instance.particles.fire.obtain();
			turn.scaleEffect(0.1414f);
			turnActivated = false;
			turn.getEmitters().get(0).getTint().setColors(new float[] {1, 1, 1, 0.5f});
		}
	}

}
