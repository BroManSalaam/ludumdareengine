package com.connectike.energycrisis.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.connectike.energycrisis.EnergyCrisisGame;
import com.connectike.energycrisis.tools.Constants;
import com.connectike.energycrisis.ui.MainMenu;
import com.connectike.energycrisis.ui.TutorialMenu;

public class MenuScreen implements Screen {
	
	private EnergyCrisisGame energyCrisis;
	private Viewport view;
	private OrthographicCamera cam;
	
	private String command = "";
	private String activeMenu = "";
	
	// Menus
	private MainMenu mainMenu;
	private TutorialMenu tutorial;
	
	public MenuScreen(EnergyCrisisGame game) {
		
		this.energyCrisis = game;
		
		cam = new OrthographicCamera();
		view = new FitViewport(Constants.W_WIDTH, Constants.W_HEIGHT, cam);
		
		view.apply();
		cam.translate(Constants.W_WIDTH / 2.0f, Constants.W_HEIGHT / 2.0f);
		cam.setToOrtho(false, Constants.W_WIDTH, Constants.W_HEIGHT);
		
		mainMenu = new MainMenu(game.batch);
		mainMenu.initialize();
		mainMenu.setVisibility(true);
		
		tutorial = new TutorialMenu(game.batch);
		tutorial.initialize();
		tutorial.setVisibility(false);
		
		activeMenu = "Main";
	}
	
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.update();
		
		energyCrisis.batch.setProjectionMatrix(cam.combined);
		energyCrisis.batch.begin();
		
		mainMenu.update(delta);
		tutorial.update(delta);
		
		// Get the command from the active menu
		if(activeMenu.equalsIgnoreCase("main")) {
			command = mainMenu.getNextAction();
		} else if(activeMenu.equalsIgnoreCase("tutorial")) {
			command = tutorial.getNextAction();
		}
		
		if(command.contains("switch")) {
			
			// Play Screen
			if(command.contains("PlayScreen")) {
				energyCrisis.setScreen(new PlayScreen(energyCrisis));
			}
		} else if(command.contains("show")) {
			
			// Tutorial
			if(command.contains("TutorialMenu")) {
				mainMenu.setVisibility(false);
				tutorial.setVisibility(true);
				activeMenu = "Tutorial";
			}
		}
		
		energyCrisis.batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		view.update(width, height);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}
	
}
