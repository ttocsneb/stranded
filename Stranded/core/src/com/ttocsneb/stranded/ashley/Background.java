package com.ttocsneb.stranded.ashley;

import com.badlogic.ashley.core.EntitySystem;
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
	private Vector2 size;

	/**
	 * Create a new BackgroundSystem with a non-seamless texture.
	 * 
	 * @param background
	 * @param width
	 *            of the screen
	 * @param height
	 *            of the screen
	 */
	public Background(TextureRegion background, float width, float height) {
		setTexture(background, false, 0, 0, width, height);
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
	public Background(TextureRegion background, boolean seamless,
			float imageWidth, float imageHeight, float width, float height) {
		setTexture(background, seamless, imageWidth, imageHeight, width, height);
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
			float imageWidth, float imageHeight, float width, float height) {
		this.background = background;
		this.seamless = seamless;
		if (dimension == null) dimension = new Vector2();
		if (size == null) size = new Vector2();
		
		dimension.set(imageWidth, imageHeight);
		size.set(width, height);
	}

	public void update(float delta) {
		if (seamless) {
			for (float i = 0; i < size.x; i += dimension.x) {
				for (float ii = 0; ii < size.y; ii += dimension.y) {
					Global.batch.draw(background, i, ii, dimension.x,
							dimension.y);
				}
			}
		} else {
			Global.batch.draw(background, 0, 0, size.x, size.y);
		}
	}

}
