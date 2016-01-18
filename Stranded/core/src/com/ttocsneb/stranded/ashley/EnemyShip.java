package com.ttocsneb.stranded.ashley;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.ttocsneb.stranded.ashley.collision.CollisionListener;
import com.ttocsneb.stranded.ashley.component.BulletComponent;
import com.ttocsneb.stranded.ashley.component.EnemyComponent;
import com.ttocsneb.stranded.game.GameScreen;
import com.ttocsneb.stranded.util.Assets;
import com.ttocsneb.stranded.util.Global;

/**
 * @author TtocsNeb
 *
 */
public class EnemyShip extends EntitySystem implements
		CollisionListener<EnemyComponent> {

	private Array<Body> enemies;

	private Body player;

	private Particles particles;

	private PointLight[] exp;

	public EnemyShip(Array<Body> array, Body player, Particles particles,
			RayHandler lightSystem) {
		this.player = player;
		this.enemies = array;

		this.particles = particles;

		exp = new PointLight[5];

		for (int i=0; i<exp.length; i++) {
			exp[i] = new PointLight(lightSystem, (int) (512*Global.Config.SHADOW));
			exp[i].setColor(0, 1, 0, 1);
			exp[i].setDistance(5);
			exp[i].setActive(false);
		}

		for (Body b : array) {
			EnemyComponent c = new EnemyComponent();
			c.body = b;
			c.active = false;
			b.setUserData(c);
			PointLight l = new PointLight(lightSystem, (int) (512*Global.Config.SHADOW));
			l.attachToBody(b, 0, -0.26f);
			l.setColor(0, 1, 0, 0.45f);
			l.setDistance(3);
		}

	}

	@Override
	public void update(float delta) {
		for(PointLight p : exp) {
			if(p.isActive()) {
				p.setColor(0, 1, 0, p.getColor().a-delta/3f);
				if(p.getColor().a <= 0) {
					p.setActive(false);
				}
			}
		}
		for (Body b : enemies) {
			if (((EnemyComponent) b.getUserData()).active) {
				float desiredAngle = (float) (Math.atan2(player.getPosition().y
						- b.getPosition().y,
						player.getPosition().x - b.getPosition().x) - 0.5f * Math.PI);

				float nextAngle = b.getAngle() + b.getAngularVelocity() * delta;
				float totalRotation = desiredAngle - nextAngle;
				while (totalRotation < -Math.PI)
					totalRotation += 2 * Math.PI;
				while (totalRotation > Math.PI)
					totalRotation -= 2 * Math.PI;

				b.applyTorque(totalRotation < 0 ? -1 : 1, true);

				b.applyForceToCenter(5 * MathUtils
						.cos((float) (desiredAngle + 0.5f * Math.PI)),
						5 * MathUtils
								.sin((float) (desiredAngle + 0.5f * Math.PI)),
						true);
			}
		}
	}

	public void spawn() {
		for (Body b : enemies) {
			EnemyComponent c = (EnemyComponent) b.getUserData();
			if (!c.active) {
				c.active = true;
				b.setTransform(MathUtils.random(-95, 95),
						MathUtils.random(-95, 95), 0);
				c.health = c.max;
				return;
			}
		}
	}

	@Override
	public Class<EnemyComponent> getClaimedClass() {
		return EnemyComponent.class;
	}

	@Override
	public void beginContact(Object object1, Object object2) {
		EnemyComponent c = (EnemyComponent) object1;
		if (object2 instanceof BulletComponent) {
			
			GameScreen.points += 10*GameScreen.multiplier;
			
			if(!Global.Config.MUTE) {
				Assets.instance.sounds.explode.play(Global.Config.VOLUME);
			}
			
			c.health = c.health - 1;
			PooledEffect e = Assets.instance.particles.explode.obtain();
			e.getEmitters().first().getTint().setColors(new float[] {0, 1, 0, 1});
			e.scaleEffect(0.75f);
			e.setPosition(c.body.getPosition().x, c.body.getPosition().y);
			for(PointLight p : exp) {
				if(!p.isActive()) {
					p.setPosition(c.body.getPosition());
					p.setColor(0, 1, 0, 0.66f);
					p.setActive(true);
					break;
				}
			}
			particles.addEffect(e);
			if (c.health <= 0) {
				c.body.setTransform(110, 110, 0);
				c.body.setAngularVelocity(0);
				c.body.setLinearVelocity(0, 0);
				c.active = false;
				c.max += 1;
			}
		}
	}

	@Override
	public void endContact(Object object1, Object object2) {

	}

}
