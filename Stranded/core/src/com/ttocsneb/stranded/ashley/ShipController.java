package com.ttocsneb.stranded.ashley;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ttocsneb.stranded.ashley.collision.CollisionListener;
import com.ttocsneb.stranded.ashley.component.EnemyComponent;
import com.ttocsneb.stranded.ashley.component.ShipComponent;
import com.ttocsneb.stranded.game.GameScreen;
import com.ttocsneb.stranded.util.Assets;
import com.ttocsneb.stranded.util.Global;

/**
 * @author TtocsNeb
 *
 */
public class ShipController extends EntitySystem implements
		CollisionListener<ShipComponent> {

	public Body ship;

	public float health = 100;

	private float acceleration = 5;
	private float angularAcceleration = 0.5f;

	private PooledEffect thruster1;
	private PooledEffect thruster2;
	private PooledEffect turn;

	Particles particles;

	private boolean thrustActivated;
	private boolean turnActivated;

	private PointLight burn1;
	private PointLight burn2;
	
	private PointLight exp;

	private Bullet bullets;

	private Sound shipSounds;
	public ShipController(Body ship, RayHandler lightSystem,
			Particles particles, Bullet bullets) {
		this.ship = ship;
		this.particles = particles;
		this.bullets = bullets;
		
		exp = new PointLight(lightSystem, (int) (512*Global.Config.SHADOW));
		exp.setColor(1, 0, 0, 1);
		exp.setDistance(10);
		exp.setActive(false);

		shipSounds = Assets.instance.sounds.rocket;

		ShipComponent c = new ShipComponent();
		c.body = ship;
		ship.setUserData(c);

		Gdx.app.debug("Ship", ship.getUserData() == null ? "None" : ship
				.getUserData().getClass().getName());

		thruster1 = Assets.instance.particles.fire.obtain();// Obtain that fire!
		thruster1.scaleEffect(0.25f);
		thruster2 = Assets.instance.particles.fire.obtain();// Obtain that fire!
		thruster2.scaleEffect(0.25f);
		thrustActivated = false;

		turn = Assets.instance.particles.fire.obtain();
		turnActivated = false;
		turn.scaleEffect(0.141f);
		turn.getEmitters().get(0).getTint().setColors(new float[] {
				1, 1, 1, 0.5f
		});

		burn1 = new PointLight(lightSystem, (int) (512*Global.Config.SHADOW));
		burn1.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b, 0.5f));
		burn1.attachToBody(ship, 0.25f, -0.57f);
		burn1.setDistance(5);
		burn2 = new PointLight(lightSystem, (int) (512*Global.Config.SHADOW));
		burn2.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b, 0.5f));
		burn2.attachToBody(ship, -0.25f, -0.57f);
		burn2.setDistance(5);

		new PointLight(lightSystem, (int) (512*Global.Config.SHADOW), new Color(1, 1, 1, 0.75f), 10, 0, 0)
				.attachToBody(ship, 0, 0.6f);

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
	private void setData(PooledEffect effect, float distance, float rotation,
			float orientation, float delta) {
		float angle = ship.getAngle() + rotation;

		
		effect.setPosition(
				ship.getPosition().x
						+ distance
						* MathUtils
								.sin((float) (2 * Math.PI - angle + 0.5f * Math.PI))
						+ ship.getLinearVelocity().x * delta,
				ship.getPosition().y
						+ distance
						* MathUtils
								.cos((float) (2 * Math.PI - angle + 0.5f * Math.PI))
						+ ship.getLinearVelocity().y * delta);

		angle = ship.getAngle() + rotation;

		effect.getEmitters().get(0).getAngle()
				.setHighMin(angle * MathUtils.radiansToDegrees - 15);
		effect.getEmitters().get(0).getAngle()
				.setHighMax(angle * MathUtils.radiansToDegrees + 15);
		effect.getEmitters().get(0).getAngle()
				.setLow(angle * MathUtils.radiansToDegrees);

		effect.getEmitters().get(0).getWind()
				.setHigh(ship.getLinearVelocity().x);
		effect.getEmitters().get(0).getGravity()
				.setHigh(ship.getLinearVelocity().y);
	}

	private boolean flip;

	private boolean playing = false;

	@Override
	public void update(float delta) {

		if(exp.isActive()) {
			exp.setColor(1, 0, 0, exp.getColor().a-delta/2f);
			if(exp.getColor().a <= 0) {
				exp.setActive(false);
			}
		}
		
		// ////////// Ship Controls /////////////////////
		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
			// Move the ship forwards if the up arrow/W key is pressed
			if (!playing && !Global.Config.MUTE) {
				shipSounds.loop(Global.Config.VOLUME);
				playing = true;
			}

			setData(thruster1, 0.56f, (float) (-1.1f),
					(float) (1.5f * Math.PI), delta);
			setData(thruster2, 0.56f, (float) (1.1f + Math.PI),
					(float) (1.5f * Math.PI), delta);

			if (!thrustActivated) {
				particles.addEffect(thruster1);
				particles.addEffect(thruster2);
				thrustActivated = true;
				burn1.setActive(true);
				burn2.setActive(true);
				burn1.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b,
						0.5f));
				burn2.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b,
						0.5f));
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
			thruster1.allowCompletion();
			thruster1 = Assets.instance.particles.fire.obtain();
			thruster1.scaleEffect(0.25f);
			thruster2.allowCompletion();
			thruster2 = Assets.instance.particles.fire.obtain();
			thruster2.scaleEffect(0.25f);
			thrustActivated = false;

			if (burn1.getColor().a <= 0) {
				burn1.setActive(false);
				burn2.setActive(true);
			} else {
				burn1.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b,
						burn1.getColor().a - delta));
				burn2.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b,
						burn1.getColor().a - delta));
			}
			shipSounds.stop();
			playing = false;
		}
		// Turn the ship
		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {

			setData(turn, 0.5f, (float) (0.25f * Math.PI),
					(float) (0 * Math.PI), delta);

			if (!turnActivated) {
				particles.addEffect(turn);
				turnActivated = true;
			}

			ship.applyTorque(angularAcceleration * ship.getMass(), true);

		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)
				|| Gdx.input.isKeyPressed(Keys.D)) {

			setData(turn, 0.5f, (float) (0.75f * Math.PI),
					(float) (0 * Math.PI), delta);

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
						0.5f,
						(float) ((ship.getAngularVelocity() > 0 ? 0.75f : 0.25f) * Math.PI),
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
				turn.getEmitters().get(0).getTint().setColors(new float[] {
						1, 1, 1, 0.5f
				});
			}

		} else {
			turn.allowCompletion();
			turn = Assets.instance.particles.fire.obtain();
			turn.scaleEffect(0.1414f);
			turnActivated = false;
			turn.getEmitters().get(0).getTint().setColors(new float[] {
					1, 1, 1, 0.5f
			});
		}

		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			
			flip = !flip;
			if(bullets.shoot(
					new Vector2(
							(float) Math.sin(MathUtils.PI2 - ship.getAngle()
									+ (flip ? -1.331f : +1.331f)) * 0.1f,
							(float) Math.cos(MathUtils.PI2 - ship.getAngle()
									+ (flip ? -1.331f : +1.331f)) * 0.1f)
							.add(ship.getPosition()),
					new Vector2(
							MathUtils.sin(MathUtils.PI2 - ship.getAngle()) * 10,
							MathUtils.cos(MathUtils.PI2 - ship.getAngle()) * 10)
							.add(ship.getLinearVelocity()), ship.getAngle()) && !Global.Config.MUTE) {
				Assets.instance.sounds.shoot.play(Global.Config.VOLUME);
			}
		}
		
	}

	@Override
	public Class<ShipComponent> getClaimedClass() {
		return ShipComponent.class;
	}

	@Override
	public void beginContact(Object object1, Object object2) {
		
		if (object2 instanceof EnemyComponent) {
			EnemyComponent c = (EnemyComponent) object2;
			float h = ship.getLinearVelocity().dst(c.body.getLinearVelocity()) / 4f;
			Gdx.app.debug("ShipController", "HealthLoss: " + h);
			if (h > 0.75f) {
				health -= h;
				Gdx.app.debug("SHIP Health", health + "");
				
				if(!Global.Config.MUTE)Assets.instance.sounds.explode.play(Global.Config.VOLUME);
				PooledEffect e = Assets.instance.particles.explode.obtain();
				e.setPosition(ship.getPosition().x, ship.getPosition().y);
				exp.setActive(true);
				exp.setColor(1, 0, 0, 1);
				exp.setPosition(ship.getPosition());
				particles.addEffect(e);
				GameScreen.multiplier /= 4;
				GameScreen.multiplier = Math.max(GameScreen.multiplier, 1);
			} else {
				health -= 1/20f;
				Gdx.app.debug("SHIP Health", health + "");
			}
			
			if (health <= 0) {
				GameScreen.lose = true;
				Gdx.app.debug("GAME", "You LOSE");
			}
		} else if(object2 instanceof Body) {
			Body b = (Body) object2;
			float h = ship.getLinearVelocity().dst(b.getLinearVelocity())/4f;
			Gdx.app.debug("ShipController", "Hit Something!; HealthLos: " + h);
			if(h > 1) {
				health -= h;
				Gdx.app.debug("SHIP Health", health + "");
				
				if(!Global.Config.MUTE)Assets.instance.sounds.explode.play(Global.Config.VOLUME);
				PooledEffect e = Assets.instance.particles.explode.obtain();
				e.setPosition(ship.getPosition().x, ship.getPosition().y);
				particles.addEffect(e);
				exp.setActive(true);
				exp.setColor(1, 0, 0, 1);
				exp.setPosition(ship.getPosition());
				GameScreen.multiplier /= 4;
				GameScreen.multiplier = Math.max(GameScreen.multiplier, 1);
			}
		}
	}

	@Override
	public void endContact(Object object1, Object object2) {

	}

}
