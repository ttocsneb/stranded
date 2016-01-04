package com.ttocsneb.stranded.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;


/**
 * @author TtocsNeb
 *
 */
public class InputListener implements InputProcessor {

	private final GameScreen game;
	
	public InputListener(GameScreen game) {
		this.game = game;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.ESCAPE) {
			game.back();
		} else if(keycode == Keys.E) {
			game.renderDebug = !game.renderDebug;
		} else if(keycode == Keys.R) {
			game.reload();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
