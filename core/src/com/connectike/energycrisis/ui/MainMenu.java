package com.connectike.energycrisis.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.connectike.energycrisis.tools.Constants;

public class MainMenu implements MenuInterface {
	
	private Stage stage;
	private Viewport viewport;
	
	private Texture gameTitle;
	private Button playButton;
	private Button tutorialButton;
	
	private boolean visible = false;
	private boolean wasInitialized = false;
	
	public MainMenu(SpriteBatch sb) {
		
		viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		stage = new Stage(viewport, sb);
		
	}
	
	
	@Override
	public void initialize() {
		
		gameTitle = new Texture(Gdx.files.internal("img/menus/main_menu/title.png"));
		
		playButton = new Button(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2, 200, 100);
		playButton.initialize("img/menus/main_menu/playButton.png", new Vector2(200, 100));
		tutorialButton = new Button(viewport.getScreenWidth() / 2, viewport.getScreenHeight() - 100, 100, 50);
		tutorialButton.initialize("img/menus/main_menu/tutorialButton.png", new Vector2(200, 100));
		
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
		
		//stage.getBatch().draw(gameTitle, (Constants.W_WIDTH / 20) * 10 - gameTitle.getWidth() / 2, (Constants.W_HEIGHT / 20) * 16 - gameTitle.getHeight() / 2);
		stage.getBatch().draw(gameTitle, 0.0f, 0.0f);
		stage.getBatch().draw(playButton.getTexture(), playButton.getBounds().x, Constants.W_HEIGHT - playButton.getCenteredPosition().y, playButton.getBounds().getWidth(), playButton.getBounds().getHeight());
		stage.getBatch().draw(tutorialButton.getTexture(), tutorialButton.getBounds().x, Constants.W_HEIGHT - tutorialButton.getCenteredPosition().y, tutorialButton.getBounds().getWidth(), tutorialButton.getBounds().getHeight());
		
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
		
		if(playButton.getState()) {
			return "switch: PlayScreen";
		}
		if(tutorialButton.getState()) {
			return "show: TutorialMenu";
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
