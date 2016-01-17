package com.ttocsneb.stranded.ashley.collision;


/**
 * @author TtocsNeb
 *
 */
public interface CollisionListener<T> {

	/**
	 * @return Class T to get what type it is.
	 */
	public Class<T> getClaimedClass();
	
	/**
	 * Called when a claimed body collides with another body.
	 * @param object1 Your body
	 * @param object2 Other body
	 */
	public void beginContact(Object object1, Object object2);

	/**
	 * Called when a claimed body stops colliding with another body.
	 * @param object1 Your body
	 * @param object2 Other body
	 */
	public void endContact(Object object1, Object object2);
	
}
