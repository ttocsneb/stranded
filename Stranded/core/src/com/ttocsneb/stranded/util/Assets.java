package com.ttocsneb.stranded.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneAsyncLoader;

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
	public AssetRube rubes;

	private static final String[] ATLASES = {
		"skins/uiskin.atlas"
	};

	private static void loadRube(AssetManager am) {
		am.setLoader(RubeScene.class, new RubeSceneAsyncLoader(new InternalFileHandleResolver()));
		am.load("rube/testShip.json", RubeScene.class);
	}
	
	public class AssetRube {
		
		public final AssetManager am;
		
		public static final String testScene = "rube/testShip.json";
		
		public AssetRube(AssetManager am) {
			this.am = am;
		}
		
		public RubeScene loadScene(String scene) {
			return am.get(scene, RubeScene.class);
		}
		
	}
	
	/**
	 * Contains all Ui Skins.
	 * 
	 * @author TtocsNeb
	 *
	 */
	public class AssetSkin {

		public final Skin skin;

		public AssetSkin(AssetAtlas aa) {
			skin = new Skin(Gdx.files.internal("skins/uiskin.json"),
					aa.uiskin);
		}

	}

	/**
	 * Contains all atlases.
	 * 
	 * @author TtocsNeb
	 *
	 */
	public class AssetAtlas {

		public final TextureAtlas uiskin;
		public final TextureAtlas textures;

		public AssetAtlas(AssetManager am) {
			textures = am.get(Global.TEXTURE_ATLAS);

			uiskin = am.get(ATLASES[0]);
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
		public final AtlasRegion background;

		public AssetTextures(TextureAtlas atlas) {

			spaceShip = atlas.findRegion("tmpship");
			background = atlas.findRegion("background");

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

		
		 private static final String arial = "fonts/asenine.ttf";
		 
		 public final BitmapFont small; public final BitmapFont med; public
		 final BitmapFont large; public final BitmapFont huge;
		 

		public AssetFonts() {
			
			 FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(arial));
			 FreeTypeFontParameter par = new FreeTypeFontParameter();
			 
			 par.size = 32; small = gen.generateFont(par);
			 
			 par.size = 64; med = gen.generateFont(par);
			 
			 par.size = 128; large = gen.generateFont(par);
			 
			 par.size = 256; huge = gen.generateFont(par);
			 
			 gen.dispose();
			 
			 small.getRegion().getTexture() .setFilter(TextureFilter.Linear, TextureFilter.Linear);
			 med.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			 large.getRegion().getTexture() .setFilter(TextureFilter.Linear, TextureFilter.Linear);
			 huge.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			 
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
		
		loadRube(assetManager);

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
		rubes = new AssetRube(assetManager);
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
