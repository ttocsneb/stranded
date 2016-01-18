package com.ttocsneb.stranded.ashley;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.ttocsneb.stranded.ashley.collision.CollisionListener;
import com.ttocsneb.stranded.ashley.component.BulletComponent;
import com.ttocsneb.stranded.ashley.component.ShipComponent;
import com.ttocsneb.stranded.util.Global;

/**
 * @author TtocsNeb
 *
 */
public class Bullet extends EntitySystem implements
		CollisionListener<BulletComponent> {

	Array<Body> bullets;

	public Bullet(Array<Body> array, RayHandler lights) {
		this.bullets = array;
		for (Body b : array) {
			BulletComponent c = new BulletComponent();
			c.body = b;
			c.inUse = false;
			b.setUserData(c);

			PointLight l = new PointLight(lights, (int) (512*Global.Config.SHADOW));
			l.attachToBody(b);
			l.setDistance(5);
			l.setColor(0, 0, 1, 0.6f);
		}
	}
	
	@Override
	public void update(float delta) {
		for(Body b : bullets) {
			BulletComponent c = (BulletComponent) b.getUserData();
			if(c.inUse && (c.time -= delta) < 0) {
				c.inUse = false;
				b.setLinearVelocity(0, 0);
				b.setTransform(-110, -110, 0);
			}
		}
	}

	public boolean shoot(Vector2 position, Vector2 velocity, float rotation) {

		for (Body b : bullets) {
			BulletComponent c = (BulletComponent) b.getUserData();
			if (!c.inUse) {
				b.setTransform(position, rotation);
				b.setLinearVelocity(velocity);
				b.setAngularVelocity(0);
				c.inUse = true;
				c.time = 10;
				return true;
			}
		}

		return false;
	}

	@Override
	public Class<BulletComponent> getClaimedClass() {
		return BulletComponent.class;
	}

	@Override
	public void beginContact(Object object1, Object object2) {
		if (!(object2 instanceof BulletComponent || object2 instanceof ShipComponent)) {
			Gdx.app.debug("Bullet", "DIE DIE DIE DIE!!!!");
			BulletComponent b = (BulletComponent) object1;
			b.inUse = false;
			Body body = b.body;
			body.setLinearVelocity(0, 0);
			body.setTransform(-110, -110, 0);
		}
	}

	@Override
	public void endContact(Object object1, Object object2) {

	}

}
