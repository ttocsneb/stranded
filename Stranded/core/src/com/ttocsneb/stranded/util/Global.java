package com.ttocsneb.stranded.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gushikustudios.rube.loader.RubeSceneLoader;

/**
 * Global variables accessible by all classes.
 * 
 * @author TtocsNeb
 *
 */
public class Global {

	public static final float VIEWPORT_WIDTH = 4;
	public static final float VIEWPORT_GUI_HEIGHT = 7.111111f;

	public static final String TEXTURE_ATLAS = "textures/textures.atlas";

	public static final RubeSceneLoader rubeLoader = new RubeSceneLoader();
	
	public static final SpriteBatch batch = new SpriteBatch();
	public static final ShapeRenderer shape = new ShapeRenderer();
	
	public static final Color RED = new Color(218/255f, 67/255f, 32/255f, 1);
	public static final Color ORANGE = new Color(250/255f, 128/255f, 40/255f, 1);
	public static final Color BLUE = new Color(49/255f, 136/255f, 183/255f, 1);
	public static final Color GREEN = new Color(63/255f, 193/255f, 91/255f, 1);
	
	/**
	 * Performs a linear interpolation
	 * 
	 * @param alpha
	 * @param start
	 * @param end
	 * @return
	 */
	public static float lerp(float alpha, float start, float end) {
		return (start + alpha * (end - start));
	}

	/**
	 * Contains the configurations for the application.
	 * 
	 * @author TtocsNeb
	 *
	 */
	public static class Config {

		public static Preferences prefs = Gdx.app.getPreferences("Stranded");

		private static final String MUTE_ID = "mute";
		public static boolean MUTE = false;
		private static final String VOL_ID = "volume";
		public static float VOLUME = 1f;
		
		private static final String MUSIC_MUTE_ID = "mute_music";
		public static boolean MUTEMUSIC = false;
		private static final String MUSIC_VOL_ID = "music_volume";
		public static float MUSICVOLUME = 1f;
		

		/**
		 * Load the configuration.
		 */
		public static void load() {
			MUTE = prefs.getBoolean(MUTE_ID, false);
			VOLUME = prefs.getFloat(VOL_ID, 1);
			MUTEMUSIC = prefs.getBoolean(MUSIC_MUTE_ID, false);
			MUSICVOLUME = prefs.getFloat(MUSIC_VOL_ID, 1);
		}

		public static void save() {
			prefs.putBoolean(MUTE_ID, MUTE);
			prefs.putFloat(VOL_ID, VOLUME);
			prefs.putBoolean(MUSIC_MUTE_ID, MUTEMUSIC);
			prefs.putFloat(MUSIC_VOL_ID, MUSICVOLUME);
			prefs.flush();
		}
	}

	public static boolean start = false;

	public static void dispose() {
		batch.dispose();
		shape.dispose();
	}

}