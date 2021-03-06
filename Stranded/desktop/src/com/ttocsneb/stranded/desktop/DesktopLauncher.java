package com.ttocsneb.stranded.desktop;

import java.io.File;
import java.io.FileNotFoundException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.ttocsneb.stranded.Stranded;

/**
 * 
 * The main Entry class for the game.
 * 
 * @author TtocsNeb
 *
 */
public class DesktopLauncher {

	private static final boolean pack = false, debug = false;
	private static final String textureDir = "../../raw/";

	public static void main(String[] arg) throws FileNotFoundException {

		// check if we should re-pack the textures, and if the directory is
		// valid.
		if (pack) {
			if (new File(textureDir).isDirectory() == true) {

				// Go through the items of the texture folder
				for (File file : new File(textureDir).listFiles()) {
					// check if the file is a directory
					if (file.isDirectory()
							&& !file.getName().equals("rawTextures")) {
						// Pack the textures in the current directory
						Settings settings = new Settings();
						settings.maxWidth = 2048;
						settings.maxHeight = 2048;
						settings.debug = debug;
						TexturePacker.process(settings, file.getPath(),
								"../core/assets/textures", file.getName());
					}
				}
			} else {
				// If the directory is invalid, throw an exceptional exception.
				throw new FileNotFoundException(textureDir
						+ " is not a real directory!");
			}
			
			System.out.println("Done Exporting images!");

		} else {

			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.width = LwjglApplicationConfiguration
					.getDesktopDisplayMode().width;
			config.height = LwjglApplicationConfiguration
					.getDesktopDisplayMode().height;
			config.fullscreen = true;
			new LwjglApplication(new Stranded(), config);
		}
	}
}
