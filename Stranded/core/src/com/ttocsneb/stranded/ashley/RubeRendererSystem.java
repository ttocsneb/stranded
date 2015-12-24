package com.ttocsneb.stranded.ashley;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;
import com.ttocsneb.stranded.util.Assets;
import com.ttocsneb.stranded.util.Global;

/**
 * @author TtocsNeb
 * 
 *         Use this class to render a RubeScene.
 *
 */
public class RubeRendererSystem extends EntitySystem {

	private HashMap<Integer, Array<RubeImage>> images;

	private HashMap<String, TextureRegion> textures;

	/**
	 * Create a new Renderer system.
	 * 
	 * @param images
	 *            The Images from the RubeScene.
	 */
	public RubeRendererSystem(Array<RubeImage> images) {

		this.images = new HashMap<Integer, Array<RubeImage>>();

		// Order the images into Arrays grouped based on their Render order.
		// These arrays, are then put into a HashMap.
		for (RubeImage image : images) {
			// Create a new group of images if there is no corresponding Render
			// Order.
			if (!this.images.containsKey(image.renderOrder)) {
				Array<RubeImage> i = new Array<RubeImage>();
				i.add(image);
				this.images.put(image.renderOrder, i);
			} else {
				this.images.get(image.renderOrder).add(image);
			}
		}

		// Create a Hashmap to hold the images, as well as the path
		textures = new HashMap<String, TextureRegion>();

		// Manually load the hashmap with images. -_-
		textures.put("../../../../raw/textures/tmpship.png",
				Assets.instance.textures.spaceShip);
		textures.put("../../../../raw/textures/background.png",
				Assets.instance.textures.background);
	}

	/**
	 * This will update the renderer with a new set of images.
	 * 
	 * <b>
	 * 
	 * <pre>
	 * Note: this will replace the old set of images.
	 * </pre>
	 * 
	 * </b>
	 * 
	 * @param images
	 */
	public void updateImages(Array<RubeImage> images) {
		// Clear the hash map, to prevent duplicates.
		this.images.clear();

		// Order the images into Arrays grouped based on their Render order.
		// These arrays, are then put into a HashMap.
		for (RubeImage image : images) {
			// Create a new group of images if there is no corresponding Render
			// Order.
			if (!this.images.containsKey(image.renderOrder)) {
				Array<RubeImage> i = new Array<RubeImage>();
				i.add(image);
				this.images.put(image.renderOrder, i);
			} else {
				this.images.get(image.renderOrder).add(image);
			}
		}

	}

	@Override
	public void update(float delta) {
		Vector2 pos = new Vector2();
		float rot;

		// Order the keys from the hashmap, from least to greatest.
		SortedSet<Integer> keys = new TreeSet<Integer>(images.keySet());
		for (Integer i : keys) {

			// Get the array of images from the current key.
			Array<RubeImage> images = this.images.get(i);

			// Go through each image, and render it.
			for (RubeImage image : images) {
				// Set the color tint, as well as opacity. for the image.
				Global.batch.setColor(new Color(image.color.r, image.color.g,
						image.color.b, image.opacity));

				// Find the texture to draw.
				TextureRegion reg = textures.get(image.file);

				if (reg != null) {
					// If the image is attached to a body, render it with the
					// body.
					if (image.body != null) {

						// Find the bottom left corner of the image.
						pos.set(image.body.getPosition());
						pos.sub(image.center);
						pos.sub(image.width / 2, image.height / 2);

						// Get the rotation of the image.
						rot = image.body.getAngle() + image.angleInRads;

						// Draw the image.
						Global.batch.draw(reg.getTexture(), pos.x, pos.y,
								image.center.x + image.width / 2,
								image.center.y + image.height / 2, image.width,
								image.height, (image.flip ? -1 : 1), 1, rot
										* MathUtils.radiansToDegrees,
								reg.getRegionX(), reg.getRegionY(),
								reg.getRegionWidth(), reg.getRegionHeight(),
								false, false);

					} else {

						// Draw the image.
						Global.batch.draw(reg.getTexture(), image.center.x
								- image.width / 2, image.center.y
								- image.height / 2, image.center.x
								+ image.width / 2, image.center.y
								+ image.height / 2, image.width, image.height,
								image.flip ? -1 : 1, 1, image.angleInRads
										* MathUtils.radiansToDegrees,
								reg.getRegionX(), reg.getRegionY(),
								reg.getRegionWidth(), reg.getRegionHeight(),
								false, false);
					}
				} else {
					// If there is no image associated, get real angry about it.
					Gdx.app.error("RubeRendererSystem;update", image.file
							+ " is not paired with an Image!");
				}

			}
		}
	}

}
