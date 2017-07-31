package com.connectike.energycrisis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.connectike.energycrisis.screens.MenuScreen;
import com.connectike.energycrisis.screens.PlayScreen;

public class EnergyCrisisGame extends Game {
	
	public SpriteBatch batch;

	@Override
	public void create() {
		
		// TODO: Set config.resizable = false
		
		batch = new SpriteBatch();
		
		Gdx.graphics.setTitle("Energy Crisis");
		this.setScreen(new MenuScreen(this));
		
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
