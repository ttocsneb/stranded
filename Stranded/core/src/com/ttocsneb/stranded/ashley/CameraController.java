package com.ttocsneb.stranded.ashley;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * @author TtocsNeb
 *
 */
public class CameraController extends EntitySystem {

	private OrthographicCamera camera;

	private Vector3 target;
	private float targetZoom;
	
	/**
	 * The time it takes to move from point to point.
	 */
	public float speed = 1;
	/**
	 * The closest the camera can zoom to.
	 */
	public float maxZoom = 0.1f;
	/**
	 * The time it takes to move from zoom to zoom.
	 */
	public float maxZoomSpeed = 1;
	
	/**
	 * Smoothly zoom to a zoom.
	 * @param zoom
	 */
	public void zoomTo(float zoom) {
		targetZoom = zoom;
	}
	
	public float getTargetZoom() {
		return targetZoom;
	}
	
	public Vector2 getTargetPosition() {
		return new Vector2(target.x, target.y);
	}
	
	/**
	 * set the zoom directly without transition.
	 * @param zoom
	 */
	public void setZoom(float zoom) {
		camera.zoom = Math.max(maxZoom, zoom);
		targetZoom = zoom;
		camera.update();
	}
	
	/**
	 * smoothly move the camera to this point
	 * @param position
	 */
	public void moveTo(Vector2 position) {
		target.set(position, camera.position.z);
	}
	
	/**
	 * move directly to this point without transition.
	 * @param position
	 */
	public void setPosition(Vector2 position) {
		camera.position.set(position, camera.position.z);
		target.set(position, camera.position.z);
		camera.update();
	}

	public CameraController(OrthographicCamera cam) {
		camera = cam;
		target = new Vector3(camera.position);
		targetZoom = camera.zoom;
		
	}
	
	@Override
	public void addedToEngine(Engine engine) {
	}

	@Override
	public void update(float delta) {
		
		camera.position.lerp(target, delta/speed);
		
		camera.zoom = Math.max(maxZoom, lerp(camera.zoom, targetZoom, delta/maxZoomSpeed));
		
		camera.update();
		
	}
	
	private float lerp(float start, float end, float percent)
	{
	     return (start + percent*(end - start));
	}

}
