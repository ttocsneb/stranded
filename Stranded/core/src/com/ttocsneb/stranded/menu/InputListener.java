package com.ttocsneb.stranded.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;


/**
 * @author TtocsNeb
 *
 */
public class InputListener implements InputProcessor {

	private final MenuScreen menu;
	
	public InputListener(MenuScreen menu) {
		this.menu = menu;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.ESCAPE) {
			Gdx.app.debug("InputListener", "Exiting Stranded..");
			Gdx.app.exit();
		}
		menu.stage.keyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		menu.stage.keyUp(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		menu.stage.keyTyped(character);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		menu.stage.touchDown(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		menu.stage.touchUp(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		menu.stage.touchDragged(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		menu.stage.mouseMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		menu.stage.scrolled(amount);
		return false;
	}

}
