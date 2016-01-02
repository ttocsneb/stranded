package com.ttocsneb.stranded.ashley;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author TtocsNeb
 *
 */
public class ShipController extends EntitySystem {

	private Body ship;

	private float acceleration = 5;
	private float angularAcceleration = 0.5f;

	public ShipController(Body ship) {
		this.ship = ship;

	}

	@Override
	public void addedToEngine(Engine engine) {

	}

	@Override
	public void update(float delta) {

		// ////////// Ship Controls /////////////////////
		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
			// Move the ship forwards if the up arrow/W key is pressed

			ship.applyForceToCenter(
					acceleration
							* ship.getMass()
							* MathUtils.sin((float) (2 * Math.PI - ship
									.getAngle())),
					acceleration
							* ship.getMass()
							* MathUtils.cos((float) (2 * Math.PI - ship
									.getAngle())), true);
		}
		// Turn the ship
		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {

			ship.applyTorque(angularAcceleration * ship.getMass(), true);

		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)
				|| Gdx.input.isKeyPressed(Keys.D)) {

			ship.applyTorque(-angularAcceleration * ship.getMass(), true);

		} else if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
				|| Gdx.input.isKeyPressed(Keys.NUM_0)) {
			// Stability Assist.
			ship.applyTorque((ship.getAngularVelocity() > 0 ? -1 : 1)
					* angularAcceleration * ship.getMass(), true);

		}
	}

}
