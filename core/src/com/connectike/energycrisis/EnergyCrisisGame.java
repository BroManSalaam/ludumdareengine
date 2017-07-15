package com.connectike.energycrisis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.connectike.energycrisis.screens.PlayScreen;

public class EnergyCrisisGame extends Game {

	public SpriteBatch batch;

	@Override
	public void create() {

		batch = new SpriteBatch();

		this.setScreen(new PlayScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
