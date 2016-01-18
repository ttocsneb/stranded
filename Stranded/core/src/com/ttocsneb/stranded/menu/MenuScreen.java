package com.ttocsneb.stranded.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ttocsneb.stranded.ashley.Background;
import com.ttocsneb.stranded.game.GameScreen;
import com.ttocsneb.stranded.util.Assets;
import com.ttocsneb.stranded.util.Global;
import com.ttocsneb.stranded.util.screen.AbstractGameScreen;
import com.ttocsneb.stranded.util.screen.DirectedGame;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransition;
import com.ttocsneb.stranded.util.screen.transitions.ScreenTransitionPixelize;

/**
 * @author TtocsNeb
 *
 *
 */
public class MenuScreen extends AbstractGameScreen {

	InputListener input;

	Stage stage;
	
	Engine engine;
	
	public static Music music;

	/**
	 * @param game
	 * 
	 *            Initialize a new Menu Screen.
	 */
	public MenuScreen(DirectedGame game) {
		super(game);
		input = new InputListener(this);
		if(music == null) {
			music = Assets.instance.sounds.music;
			music.setLooping(true);
			if(!Global.Config.MUTEMUSIC) {
				music.setVolume(Global.Config.MUSICVOLUME);
				music.play();
			}
		}
	}

	@Override
	public void show() {
		initStage();
		
		engine = new Engine();
		
		Background background = new Background((OrthographicCamera)stage.getCamera(),
				Assets.instance.textures.background, true,
				Assets.instance.textures.background.getRegionWidth()*2,
				Assets.instance.textures.background.getRegionHeight()*2);
		engine.addSystem(background);

		Assets.instance.sounds.explode.stop();
		Assets.instance.sounds.rocket.stop();
		Assets.instance.sounds.shoot.stop();
		
	}

	/**
	 * Setup the stage, and populate it.
	 */
	private void initStage() {

		// Setup the Stage
		stage = new Stage(new ScreenViewport());
		
		
		Table t = new Table();
		t.setFillParent(true);
		stage.addActor(t);
		
		final Window settings = new Window("Settings", Assets.instance.skins.skin);
		settings.setWidth(400);
		settings.setHeight(400);
		settings.setPosition(1920/2f-200, 1080/2f-200);
		settings.setVisible(false);
		stage.addActor(settings);
		
		settings.add(new Label("Audio", Assets.instance.skins.skin)).colspan(2).row();
		final CheckBox mutesound = new CheckBox("sound", Assets.instance.skins.skin);
		mutesound.setChecked(!Global.Config.MUTE);
		final Slider volume = new Slider(0.1f, 1, 0.1f, false, Assets.instance.skins.skin);
		volume.setValue(Global.Config.VOLUME);
		settings.add(mutesound);
		settings.add(volume).padLeft(5).row();
		
		final CheckBox mutemusic = new CheckBox("music", Assets.instance.skins.skin);
		mutemusic.setChecked(!Global.Config.MUTEMUSIC);
		final Slider musicvolume = new Slider(0.1f, 1, 0.1f, false, Assets.instance.skins.skin);
		musicvolume.setValue(Global.Config.MUSICVOLUME);
		settings.add(mutemusic);
		settings.add(musicvolume).padLeft(5).row();
		
		settings.add(new Label("Graphics", Assets.instance.skins.skin)).colspan(2).row();
		
		final Slider shadowQual = new Slider(0.1f, 2, 0.1f, false, Assets.instance.skins.skin);
		shadowQual.setValue(Global.Config.SHADOW);
		settings.add(new Label("Shadows", Assets.instance.skins.skin));
		settings.add(shadowQual).padLeft(5).row();
		
		TextButton save = new TextButton("Apply", Assets.instance.skins.skin);
		save.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Global.Config.MUTE = !mutesound.isChecked();
				Global.Config.VOLUME = volume.getValue();
				Global.Config.MUTEMUSIC = !mutemusic.isChecked();
				Global.Config.MUSICVOLUME = musicvolume.getValue();
				Global.Config.SHADOW = shadowQual.getValue();
				Global.Config.save();
				
				music.setVolume(Global.Config.MUSICVOLUME);
				
				if(Global.Config.MUTEMUSIC) {
					music.pause();
				} else if(!music.isPlaying()){
					music.play();
				}
			}
			
		});
		settings.add(save);
		TextButton leave = new TextButton("Close", Assets.instance.skins.skin);
		leave.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				settings.setVisible(false);
			}
		});
		settings.add(leave);
		
		
		
		//Populate the stage with ... stuff.
		TextButton button = new TextButton("Play", Assets.instance.skins.skin);
		button.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ScreenTransition transition = ScreenTransitionPixelize.init(1);
				game.setScreen(new GameScreen(game), transition);
			}
		});
		t.add(button).row();
		
		TextButton set = new TextButton("Settings", Assets.instance.skins.skin);
		set.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				settings.setVisible(true);
			}
			
		});
		t.add(set).padTop(150);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
				| GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));
		
		Global.batch.setProjectionMatrix(stage.getCamera().combined);
		Global.batch.begin();
		engine.update(delta);
		TextureRegion reg = Assets.instance.textures.title;
		Global.batch.draw(reg, 1920/2-reg.getRegionWidth()/2, 1080*3/4f);
		Global.batch.end();
		
		stage.act(delta);
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {

	}

	@Override
	public void hide() {
		stage.dispose();
	}

	@Override
	public InputProcessor getInputProcessor() {
		return input;
	}

}
