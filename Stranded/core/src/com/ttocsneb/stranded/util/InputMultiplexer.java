package com.ttocsneb.stranded.util;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

/**
 * This will allow you to have multiple input processors.
 * 
 * @author TtocsNeb
 *
 */
public class InputMultiplexer implements InputProcessor {

	private Array<InputProcessor> processors;

	public InputMultiplexer() {
		this((InputProcessor[]) null);
	}

	public InputMultiplexer(InputProcessor... processors) {
		this.processors = new Array<InputProcessor>();
		if (processors != null) this.processors.addAll(processors);
	}
	
	public void add(InputProcessor processor) {
		processors.add(processor);
	}
	
	public boolean remove(InputProcessor processor) {
		return processors.removeValue(processor, true);
	}

	@Override
	public boolean keyDown(int keycode) {
		boolean processed = false;
		for(InputProcessor p : processors) {
			processed = processed || p.keyDown(keycode);
		}
		return processed;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean processed = false;
		for(InputProcessor p : processors) {
			processed = processed || p.keyUp(keycode);
		}
		return processed;
	}

	@Override
	public boolean keyTyped(char character) {
		boolean processed = false;
		for(InputProcessor p : processors) {
			processed = processed || p.keyTyped(character);
		}
		return processed;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean processed = false;
		for(InputProcessor p : processors) {
			processed = processed || p.touchDown(screenX, screenY, pointer, button);
		}
		return processed;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean processed = false;
		for(InputProcessor p : processors) {
			processed = processed || p.touchUp(screenX, screenY, pointer, button);
		}
		return processed;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		boolean processed = false;
		for(InputProcessor p : processors) {
			processed = processed || p.touchDragged(screenX, screenY, pointer);
		}
		return processed;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		boolean processed = false;
		for(InputProcessor p : processors) {
			processed = processed || p.mouseMoved(screenX, screenY);
		}
		return processed;
	}

	@Override
	public boolean scrolled(int amount) {
		boolean processed = false;
		for(InputProcessor p : processors) {
			processed = processed || p.scrolled(amount);
		}
		return processed;
	}

}
