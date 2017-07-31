package com.connectike.energycrisis.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.connectike.energycrisis.tools.Constants;

public class TutorialMenu implements MenuInterface {
	
	private Stage stage;
	private Viewport viewport;
	
	private int slide = 0;
	Texture slides[];
	private float touchTime = 0.0f;
	
	private boolean visible = false;
	private boolean wasInitialized = false;
	
	public TutorialMenu(SpriteBatch sb) {
		
		viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		stage = new Stage(viewport, sb);
		
	}
	
	
	@Override
	public void initialize() {
		
		slides = new Texture[8];
		
		for(int s = 0; s < 8; s++) {
			slides[s] = new Texture(Gdx.files.internal("img/tutorial/slide" + (s + 1) + ".png"));
		}
		
		wasInitialized = true;
	}
	
	@Override
	public void update(float delta) {
		
		
		if(!wasInitialized) {
			Gdx.app.log("update", "MainMenu was not initialized!");
			return;
		}
		if(!visible) {
			// If not visible, don't update anything
			return;
		}
		
		boolean shouldEnd = false;
		
		if(!stage.getBatch().isDrawing()) {
			stage.getBatch().begin();
			shouldEnd = true;
		}
		stage.getBatch().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		// If the user clicks, advance the slide
		touchTime = touchTime + delta;
		if((Gdx.input.justTouched()) && (touchTime > 0.5f)) {
			slide++;
			touchTime = 0.0f;
		}
		
		if(slide < 8) {
			stage.getBatch().draw(slides[slide], 0.0f, 0.0f);
		}
		
		if(shouldEnd) {
			stage.getBatch().end();
		}
		
	}
	
	/**
	 * Method that will be called in the screen's update
	 * method. It will return a String command
	 * corresponding to a certain action that
	 * should be taken based on the inputs from the menu.
	 * 
	 * @return
	 * A String command.
	 */
	public String getNextAction() {
		
		if(slide >= 8) {
			return "switch: PlayScreen";
		}
		
		return "";
	}
	
	@Override
	public boolean getVisibility() {
		return visible;
	}

	@Override
	public void setVisibility(boolean visible) {
		this.visible = visible;
	}
	
}
