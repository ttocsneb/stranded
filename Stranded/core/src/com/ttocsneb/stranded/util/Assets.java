package com.ttocsneb.stranded.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

/**
 * Contains all of the assets for the game.
 * 
 * @author TtocsNeb
 *
 */
public class Assets implements Disposable, AssetErrorListener {

	private static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManager;
	private boolean loaded;

	public AssetTextures textures;
	public AssetSounds sounds;
	public AssetFonts fonts;
	public AssetAtlas atlases;
	public AssetSkin skins;
	public AssetParticles particles;

	private static final String[] ATLASES = {
		"skins/uiskin-1080.atlas"
	};

	/**
	 * Contains all Ui Skins.
	 * 
	 * @author TtocsNeb
	 *
	 */
	public class AssetSkin {

		public final Skin skin1080;

		public AssetSkin(AssetAtlas aa) {
			skin1080 = new Skin(Gdx.files.internal("skins/uiskin-1080.json"),
					aa.uiskin1080);
		}

	}

	/**
	 * Contains all atlases.
	 * 
	 * @author TtocsNeb
	 *
	 */
	public class AssetAtlas {

		public final TextureAtlas uiskin1080;
		public final TextureAtlas textures;

		public AssetAtlas(AssetManager am) {
			textures = am.get(Global.TEXTURE_ATLAS);

			uiskin1080 = am.get(ATLASES[0]);
		}

	}

	/**
	 * Contains all Textures
	 * 
	 * @author TtocsNeb
	 *
	 */
	public class AssetTextures {

		public final AtlasRegion spaceShip;

		public AssetTextures(TextureAtlas atlas) {

			spaceShip = atlas.findRegion("Ship");
			
		}
	}

	/**
	 * Contains all Particle effects.
	 * 
	 * @author TtocsNeb
	 *
	 */
	public class AssetParticles {

		// public final ParticleEffect triangleExp;

		public AssetParticles(TextureAtlas atlas) {
			// triangleExp = new ParticleEffect();
			// triangleExp.load(Gdx.files.internal("particles/explosionTriangle.p"),
			// atlas);

		}
	}

	private static void loadSounds(AssetManager am) {
		// am.load("music/Blip Stream.mp3", Music.class);
	}

	/**
	 * Contains all Sounds.
	 * 
	 * @author TtocsNeb
	 *
	 */
	public class AssetSounds {

		// public final Sound load;
		// public final Music music;

		public AssetSounds(AssetManager am) {
			// load = am.get("sounds/Load.wav", Sound.class);
			// music = am.get("music/Blip Stream.mp3", Music.class);
		}

	}

	/**
	 * Contains all fonts.
	 * 
	 * @author TtocsNeb
	 *
	 */
	public class AssetFonts implements Disposable {

		/*
		 * private static final String arial = "font/arialHeavy.ttf";
		 * 
		 * public final BitmapFont small; public final BitmapFont med; public
		 * final BitmapFont large; public final BitmapFont huge;
		 */

		public AssetFonts() {
			/*
			 * FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
			 * Gdx.files.internal(arial)); FreeTypeFontParameter par = new
			 * FreeTypeFontParameter();
			 * 
			 * par.size = 32; small = gen.generateFont(par);
			 * 
			 * par.size = 64; med = gen.generateFont(par);
			 * 
			 * par.size = 128; large = gen.generateFont(par);
			 * 
			 * par.size = 256; huge = gen.generateFont(par);
			 * 
			 * gen.dispose();
			 * 
			 * small.getRegion().getTexture() .setFilter(TextureFilter.Linear,
			 * TextureFilter.Linear); med.getRegion().getTexture()
			 * .setFilter(TextureFilter.Linear, TextureFilter.Linear);
			 * large.getRegion().getTexture() .setFilter(TextureFilter.Linear,
			 * TextureFilter.Linear); huge.getRegion().getTexture()
			 * .setFilter(TextureFilter.Linear, TextureFilter.Linear);
			 */
		}

		@Override
		public void dispose() {
			/*
			 * small.dispose(); med.dispose(); large.dispose(); huge.dispose();
			 */
		}
	}

	/**
	 * Initialize assets
	 * 
	 * @param assetManager
	 */
	public void init(AssetManager assetManager) {
		Gdx.app.debug("Assets", "Init Assets!");

		this.assetManager = assetManager;
		loaded = false;
		// set asset manager error handler.
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Global.TEXTURE_ATLAS, TextureAtlas.class);

		for (String s : ATLASES) {
			assetManager.load(s, TextureAtlas.class);
		}

		loadSounds(assetManager);

	}

	/**
	 * Check if loaded.
	 * 
	 * @return
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Waits until all assets are loaded.
	 */
	public void finishLoading() {
		assetManager.finishLoading();
		while (getProgress() != 1);
	}

	/**
	 * Get the progress in percent of completion.
	 * 
	 * <pre>
	 * <b>Note: no assets are loaded until returns 1.0</b>
	 * </pre>
	 * 
	 * @return the progress in percent of completion.
	 */
	public float getProgress() {
		// return 100% if the assets are loaded
		if (loaded) return 1;

		assetManager.update();

		// return the progress if not yet loaded.
		if (assetManager.getProgress() < 1) return assetManager.getProgress();

		loaded = true;

		Gdx.app.debug(TAG,
				"# of assets loadied: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		TextureAtlas atlas = assetManager.get(Global.TEXTURE_ATLAS);

		// enable texture filtering for pixel smoothing.
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		for (String s : ATLASES) {
			TextureAtlas a = assetManager.get(s);

			for (Texture t : a.getTextures()) {
				t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			}
		}

		// create game resource objects
		sounds = new AssetSounds(assetManager);
		textures = new AssetTextures(atlas);
		particles = new AssetParticles(atlas);
		atlases = new AssetAtlas(assetManager);
		skins = new AssetSkin(atlases);

		fonts = new AssetFonts();
		loaded = true;
		return 1;
	}

	@Override
	public void error(@SuppressWarnings("rawtypes") AssetDescriptor asset,
			Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'",
				throwable);
		Gdx.app.exit();
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.dispose();
		loaded = false;
	}

}
