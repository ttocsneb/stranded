package com.ttocsneb.stranded.ashley.collision;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

/**
 * @author TtocsNeb
 *
 */
public class Box2DCollision implements ContactListener {

	private CollisionListener<?>[] contactListener;

	private Array<Cont> beginContact;
	private Array<Cont> endContact;

	private class Cont {

		private final Body body1;
		private final Body body2;

		private Cont(Body b1, Body b2) {
			body1 = b1;
			body2 = b2;
		}
	}

	public Box2DCollision(CollisionListener<?>... contact) {
		contactListener = contact;
		beginContact = new Array<Box2DCollision.Cont>();
		endContact = new Array<Box2DCollision.Cont>();
	}

	public void update() {
		Object a;
		Object b;

		// Notify listeners for begin Contact.
		// go through every contact
		for (Cont c : beginContact) {
			a = c.body1.getUserData();
			b = c.body2.getUserData();
			// Go through every listener
			for (CollisionListener<?> cl : contactListener) {
				// Check if the contact was made by the selected listener, and
				// notify it.
				if (a != null && a.getClass().equals(cl.getClaimedClass())) {
					if (b == null)
						cl.beginContact(a, c.body2);
					else cl.beginContact(a, b);
				} else if (b != null
						&& b.getClass().equals(cl.getClaimedClass())) {
					if (a == null)
						cl.beginContact(b, c.body1);
					else cl.beginContact(b, a);
				}
			}
		}

		// Notify listeners for end Contact.
		// go through every contact
		for (Cont c : endContact) {
			a = c.body1.getUserData();
			b = c.body2.getUserData();
			// Go through every listener
			for (CollisionListener<?> cl : contactListener) {
				// Check if the contact was made by the selected listener, and
				// notify it.
				if (a != null && a.getClass().equals(cl.getClaimedClass())) {
					if (b == null)
						cl.endContact(a, c.body2);
					else cl.endContact(a, b);
				} else if (b != null
						&& b.getClass().equals(cl.getClaimedClass())) {
					if (a == null)
						cl.endContact(b, c.body1);
					else cl.endContact(b, a);
				}
			}
		}

		// Clear the contact storage for the next routine
		beginContact.clear();
		endContact.clear();
	}

	@Override
	public void beginContact(Contact contact) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();

		// Go through each listener.
		for (CollisionListener<?> cl : contactListener) {
			// Check if the contact belongs to the selected listener.
			if (a != null && a.getClass().equals(cl.getClaimedClass())) {
				beginContact.add(new Cont(contact.getFixtureA().getBody(),
						contact.getFixtureB().getBody()));
				return;
			} else if (b != null && b.getClass().equals(cl.getClaimedClass())) {
				beginContact.add(new Cont(contact.getFixtureA().getBody(),
						contact.getFixtureB().getBody()));
				return;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();

		// Go through each listener.
		for (CollisionListener<?> cl : contactListener) {
			// Check if the contact belongs to the selected listener.
			if (a != null && a.getClass().equals(cl.getClaimedClass())) {
				beginContact.add(new Cont(contact.getFixtureA().getBody(),
						contact.getFixtureB().getBody()));
				return;
			} else if (b != null && b.getClass().equals(cl.getClaimedClass())) {
				beginContact.add(new Cont(contact.getFixtureA().getBody(),
						contact.getFixtureB().getBody()));
				return;
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
