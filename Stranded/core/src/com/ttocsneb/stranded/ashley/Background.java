package com.ttocsneb.stranded.ashley;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ttocsneb.stranded.util.Global;

/**
 * @author TtocsNeb
 *
 */
public class Background extends EntitySystem {

	private TextureRegion background;

	boolean seamless;

	private Vector2 dimension;

	private OrthographicCamera cam;

	/**
	 * Create a new BackgroundSystem with a non-seamless texture.
	 * 
	 * @param background
	 * @param width
	 *            of the screen
	 * @param height
	 *            of the screen
	 */
	public Background(OrthographicCamera cam, TextureRegion background) {
		setTexture(background, false, 0, 0);
		this.cam = cam;
	}

	/**
	 * Create a new BackgroundSystem.
	 * 
	 * @param background
	 * @param seamless
	 *            whether the texture given is repeatable
	 * @param imageWidth
	 *            the width to draw the texture <b>(only applies if
	 *            seamless)</b>
	 * @param imageHeight
	 *            the height to draw the texture <b>(only applies if
	 *            seamless)</b>
	 * @param width
	 *            of the screen
	 * @param height
	 *            of the screen
	 */
	public Background(OrthographicCamera cam, TextureRegion background,
			boolean seamless, float imageWidth, float imageHeight) {
		setTexture(background, seamless, imageWidth, imageHeight);
		this.cam = cam;
	}

	/**
	 * Reset the background image.
	 * 
	 * @param background
	 * @param seamless
	 *            whether the texture given is repeatable
	 * @param imageWidth
	 *            the width to draw the texture <b>(only applies if
	 *            seamless)</b>
	 * @param imageHeight
	 *            the height to draw the texture <b>(only applies if
	 *            seamless)</b>
	 * @param width
	 *            of the screen
	 * @param height
	 *            of the screen
	 */
	public void setTexture(TextureRegion background, boolean seamless,
			float imageWidth, float imageHeight) {
		this.background = background;
		this.seamless = seamless;
		if (dimension == null) dimension = new Vector2();

		dimension.set(imageWidth, imageHeight);
	}

	public void update(float delta) {
		float width = cam.viewportWidth * cam.zoom, height = cam.viewportHeight
				* cam.zoom;
		float posx = cam.position.x - width / 2, posy = cam.position.y - height
				/ 2;

		if (seamless) {
			for (float i = posx-(posx%dimension.x)-dimension.x; i < width + posx; i += dimension.x) {
				for (float ii = posy-(posy%dimension.y)-dimension.y; ii < height + posy; ii += dimension.y) {
					Global.batch.draw(background, i, ii, dimension.x,
							dimension.y);
				}
			}
		} else {
			Global.batch.draw(background, posx, posy, width, height);
		}
	}

}
