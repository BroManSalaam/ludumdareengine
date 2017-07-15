package com.connectike.energycrisis;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.connectike.energycrisis.entities.Player;

public class PlayScreenInputHandler implements InputProcessor {

	public boolean isW;
	public boolean isA;
	public boolean isS;
	public boolean isD;

	public boolean scrollUp;
	public boolean scrollDown;

	private Player p;

	public PlayScreenInputHandler(Player p) {
		this.p = p;
	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Input.Keys.W) {
			isW = true;
			p.isAnimationLooping = true;
		}
		if (keycode == Input.Keys.A) {
			isA = true;
			p.isAnimationLooping = true;
		}
		if (keycode == Input.Keys.S) {
			isS = true;
			p.isAnimationLooping = true;
		}
		if (keycode == Input.Keys.D) {
			isD = true;
			p.isAnimationLooping = true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Input.Keys.W) {
			isW = false;
			p.isAnimationLooping = false;
		}
		if (keycode == Input.Keys.A) {
			isA = false;
			p.isAnimationLooping = false;
		}
		if (keycode == Input.Keys.S) {
			isS = false;
			p.isAnimationLooping = false;
		}
		if (keycode == Input.Keys.D) {
			isD = false;
			p.isAnimationLooping = false;
		}

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

		if (amount == 1) {
			scrollUp = true;
		}
		if (amount == -1) {
			scrollDown = true;
		}

		return false;
	}

}
