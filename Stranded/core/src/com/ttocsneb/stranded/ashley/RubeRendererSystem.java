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

	private static HashMap<String, TextureRegion> textures;
	private static boolean loaded;

	/**
	 * Create a new Renderer system.
	 * 
	 * @param images
	 *            The Images from the RubeScene.
	 */
	public RubeRendererSystem(Array<RubeImage> images) {
		if (!loaded) load();

		this.images = new HashMap<Integer, Array<RubeImage>>();

		if (images != null) {
			// Order the images into Arrays grouped based on their Render order.
			// These arrays, are then put into a HashMap.
			for (RubeImage image : images) {
				// Create a new group of images if there is no corresponding
				// Render Order.
				if (!this.images.containsKey(image.renderOrder)) {
					Array<RubeImage> i = new Array<RubeImage>();
					i.add(image);
					this.images.put(image.renderOrder, i);
				} else {
					this.images.get(image.renderOrder).add(image);
				}
			}
		}

	}

	/**
	 * Create a new RubeRendererSystem, without any images to render.
	 */
	public RubeRendererSystem() {
		this(null);
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

		if (!loaded) load();

		// Clear the hash map, to prevent duplicates.
		if (this.images != null) this.images.clear();

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
		Vector2 tmp = new Vector2();
		float angle;
		float rot;
		float dist;

		if (images != null) {
			// Order the keys from the hashmap, from least to greatest.
			SortedSet<Integer> keys = new TreeSet<Integer>(images.keySet());
			for (Integer i : keys) {

				// Get the array of images from the current key.
				Array<RubeImage> images = this.images.get(i);

				// Go through each image, and render it.
				for (RubeImage image : images) {
					// Set the color tint, as well as opacity. for the image.
					Global.batch.setColor(new Color(image.color.r,
							image.color.g, image.color.b, image.opacity));

					// Find the texture to draw.
					TextureRegion reg = textures.get(image.file);

					if (reg != null) {
						// If the image is attached to a body, render it with
						// the
						// body.
						if (image.body != null) {

							// Find the bottom left corner of the image.
							pos.set(image.body.getPosition());

							// If the offset of the image is not zero, do this
							// vvv
							if (!(image.center.x == 0 && image.center.y == 0)) {
								// Get the angle from the offset.
								angle = (float) Math.atan2(image.center.y,
										image.center.x);
								// find the distance of the offset.
								dist = (float) Math.sqrt(Math.pow(
										image.center.x, 2)
										+ Math.pow(image.center.y, 2));

								// get the current x, y values. ArcTangents can
								// be
								// 180 degrees off, we need to check if they are
								// off.
								tmp.set(MathUtils.sin(angle) * dist,
										MathUtils.cos(angle) * dist);

								// Check if the angle is 180 degrees offset, and
								// compensate
								if (tmp.x == -image.center.x
										&& tmp.y == -image.center.y) {
									angle += Math.PI;
								}

								// Add the rotation of the body to the image.
								angle += image.body.getAngle();

								// calculate the the absolute center of the
								// image.
								pos.add(MathUtils.cos(angle) * dist,
										MathUtils.sin(angle) * dist);
							}

							// get the bottom left corner of the image.
							pos.sub(image.width / 2, image.height / 2);

							// Get the rotation of the image.
							rot = image.body.getAngle() + image.angleInRads;

							// Draw the image.
							Global.batch.draw(reg.getTexture(), pos.x, pos.y,
									image.width / 2, image.height / 2,
									image.width, image.height, (image.flip ? -1
											: 1), 1, rot
											* MathUtils.radiansToDegrees, reg
											.getRegionX(), reg.getRegionY(),
									reg.getRegionWidth(),
									reg.getRegionHeight(), false, false);

						} else {

							// Draw the image.
							Global.batch.draw(reg.getTexture(), image.center.x
									- image.width / 2, image.center.y
									- image.height / 2, image.center.x
									+ image.width / 2, image.center.y
									+ image.height / 2, image.width,
									image.height, image.flip ? -1 : 1, 1,
									image.angleInRads
											* MathUtils.radiansToDegrees,
									reg.getRegionX(), reg.getRegionY(),
									reg.getRegionWidth(),
									reg.getRegionHeight(), false, false);
						}

					} else {
						// If there is no image associated, get real angry about
						// it.
						Gdx.app.error("RubeRendererSystem;update", image.file
								+ " is not paired with an Image!");
					}

				}
			}
		}
	}

	private static void load() {
		// Create a Hashmap to hold the images, as well as the path
		textures = new HashMap<String, TextureRegion>();

		// Manually load the hashmap with images. -_-

		textures.put("spaceStationExplosion/pan0.png",
				Assets.instance.textures.station.pan0);
		textures.put("spaceStationExplosion/pan1.png",
				Assets.instance.textures.station.pan1);
		textures.put("spaceStationExplosion/pan2.png",
				Assets.instance.textures.station.pan2);
		textures.put("spaceStationExplosion/pan3.png",
				Assets.instance.textures.station.pan3);
		textures.put("spaceStationExplosion/pan4.png",
				Assets.instance.textures.station.pan4);
		textures.put("spaceStationExplosion/pan5.png",
				Assets.instance.textures.station.pan5);
		textures.put("spaceStationExplosion/pan6.png",
				Assets.instance.textures.station.pan6);
		textures.put("spaceStationExplosion/pan7.png",
				Assets.instance.textures.station.pan7);

		textures.put("spaceStationExplosion/arm0.png",
				Assets.instance.textures.station.arm0);

		textures.put("spaceStationExplosion/sat0.png",
				Assets.instance.textures.station.sat0);
		textures.put("spaceStationExplosion/sat1.png",
				Assets.instance.textures.station.sat1);
		textures.put("spaceStationExplosion/sat2.png",
				Assets.instance.textures.station.sat2);

		textures.put("spaceStationExplosion/Asteroid.png",
				Assets.instance.textures.asteroid.asteroid);

		textures.put("asteroids/asteroid0.png",
				Assets.instance.textures.asteroid.ast0);
		textures.put("asteroids/asteroid1.png",
				Assets.instance.textures.asteroid.ast1);
		textures.put("asteroids/asteroid2.png",
				Assets.instance.textures.asteroid.ast2);
		textures.put("asteroids/asteroid3.png",
				Assets.instance.textures.asteroid.ast3);
		textures.put("asteroids/asteroid4.png",
				Assets.instance.textures.asteroid.ast4);
		textures.put("asteroids/asteroid5.png",
				Assets.instance.textures.asteroid.ast5);
		textures.put("asteroids/asteroid6.png",
				Assets.instance.textures.asteroid.ast6);
		textures.put("asteroids/asteroid7.png",
				Assets.instance.textures.asteroid.ast7);
		textures.put("asteroids/asteroid8.png",
				Assets.instance.textures.asteroid.ast8);

		textures.put("tmpship.png", Assets.instance.textures.spaceShip);
		textures.put("background.png", Assets.instance.textures.background);
		loaded = true;
	}

}
