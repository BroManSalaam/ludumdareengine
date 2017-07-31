package com.connectike.energycrisis.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.connectike.energycrisis.screens.PlayScreen;
import com.connectike.energycrisis.tools.Constants;

public class HUD {
	
	public Stage stage;
	public Viewport viewport;
	private BitmapFont font;
	
	private Texture gameOver;
	private Texture fuelIcon;
	
	private Button  retryButton;
	
	private Color c;
	private float alphaIncrease = 0.0f;
	
	
	private static ArrayList<Object[]> messages = new ArrayList<Object[]>();
	
	/**
	 * An overlay on the screen to display different
	 * statistics and variable values.
	 * 
	 * @param sb
	 * a SpriteBatch object
	 */
	public HUD(SpriteBatch sb) {
		
		viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT);
		stage = new Stage(viewport, sb);
		font = new BitmapFont();
		
		gameOver = new Texture(Gdx.files.internal("img/gameOver.png"));
		fuelIcon = new Texture(Gdx.files.internal("img/fuelIcon.png"));
		
		retryButton = new Button(viewport.getScreenWidth() / 2.0f, (viewport.getScreenHeight() / 5.0f) * 3.0f, 150.0f, 75.0f);
		retryButton.initialize("img/menus/main_menu/retryButton.png", new Vector2(200, 100));
		
	}
	
	/**
	 * Should be called inside the <code>render()</code>
	 * method and will update the HUD.
	 * 
	 * @param delta
	 * the delta time
	 * @param ps
	 * the PlayScreen object, needed to get the statistics
	 * of different aspects of the game
	 */
	public void update(float delta, PlayScreen ps) {
		
		// Will be true is the batch didn't start off as drawing
		boolean shouldEnd = false;
		
		if(!stage.getBatch().isDrawing()) {
			stage.getBatch().begin();
			shouldEnd = true;
		}
		
		// Voltage
		font.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		font.draw(stage.getBatch(), "Voltage", viewport.getWorldWidth() / 2.0f, viewport.getWorldHeight() * 4.7f);
		font.draw(stage.getBatch(), String.valueOf(ps.getPlayer().getVoltage()), viewport.getWorldWidth() / 2.0f, viewport.getWorldHeight() * 4.5f);
		
		// Health
		font.setColor(1.0f, 0.5f, 0.5f, 1.0f);
		font.draw(stage.getBatch(), "Health", viewport.getWorldWidth() * 2.0f, viewport.getWorldHeight() * 4.7f);
		font.draw(stage.getBatch(), String.valueOf(ps.getPlayer().getHp()), viewport.getWorldWidth() * 2.0f, viewport.getWorldHeight() * 4.5f);
		
		// Wave Count
		font.setColor(0.7f, 0.7f, 0.7f, 1.0f);
		font.draw(stage.getBatch(), "Wave", viewport.getWorldWidth() * 3.5f, viewport.getWorldHeight() * 4.7f);
		font.draw(stage.getBatch(), removeDecimal(PlayScreen.waveCount), viewport.getWorldWidth() * 3.5f, viewport.getWorldHeight() * 4.5f);
		
		// Fuel icon
		if(ps.getPlayer().getHasFuel()) {
			stage.getBatch().draw(fuelIcon, viewport.getScreenWidth() + (viewport.getScreenWidth() / 8), viewport.getScreenHeight() - (viewport.getScreenHeight() / 10), 40, 40);
		}
		
		// Message
		for(int m = 0; m < messages.size(); m++) {
			
			Object[] info = messages.get(m);
			
			// 0 - message
			// 1 - color
			// 2 - size multiplier
			// 3 - time
			
			if((Float) info[3] > 0) {
				
				int size = (Integer) info[2];
				int scrollPadding = (messages.size() - m - 1) * 30;
				
				font.setColor((Color) info[1]); 
				font.getData().setScale(size);
				font.draw(stage.getBatch(), info[0].toString(), (viewport.getScreenWidth() / 2) + (font.getScaleX() * info[0].toString().length() / 2), viewport.getScreenHeight() / 11 * 9 - scrollPadding);
				
				info[3] = (Float) info[3] - delta;
				font.getData().setScale(1);
				messages.set(m, info);
				
			} else {
				messages.remove(m);
				m++;
			}
			
		}
		
		// Game over overlay
		if(ps.isGameOver()) {			
			if(alphaIncrease < 1.0f) {
				//Gdx.app.log("Going up", "" + alphaIncrease);
				c = stage.getBatch().getColor();
				stage.getBatch().setColor(c.r, c.g, c.b, alphaIncrease);
				
				alphaIncrease = alphaIncrease + 0.01f;
			}
			stage.getBatch().draw(gameOver, 0, 0);
			
			// Retry button
			stage.getBatch().draw(retryButton.getTexture(), retryButton.getBounds().x, Constants.W_HEIGHT - retryButton.getCenteredPosition().y, retryButton.getBounds().getWidth(), retryButton.getBounds().getHeight());
			if(retryButton.getState()) {
				ps.shouldReset = true;
			}
		}
		
		
		if(shouldEnd) {
			stage.getBatch().end();
		}
	}
	
	/**
	 * Adds a new message to the HUD list to be
	 * displayed the next time the update method
	 * is called. The message is created with
	 * the default parameter: white color,
	 *  default size, and a show time of three
	 *  seconds.
	 *  
	 * @param msg
	 * the message to be displayed
	 */
	public static void displayMessage(String msg) {
		displayMessage(msg, new Color(1.0f, 1.0f, 1.0f, 1.0f), 1, 3.0f);
	}
	
	/**
	 * Adds a new message to the HUD list to be
	 * displayed the next time the update method
	 * is called. The message is created with
	 * the default parameter: white color,
	 *  default size, and a show time of three
	 *  seconds.
	 *  
	 * @param msg
	 * the message to be displayed
	 * @param color
	 * the color of the text
	 * @param sizeMultipler
	 * will increase the size of the text by this value
	 * @param time
	 * the time this message will appear before
	 * disappearing in seconds
	 */
	public static void displayMessage(String msg, Color color, int sizeMultipler, float time) {
		
		Object info[] = new Object[4];
		
		info[0] = msg;
		info[1] = color;
		info[2] = sizeMultipler; // Size multiplier
		info[3] = time; // Time to show (in milliseconds)
		
		messages.add(info);
	}
	
	/**
	 * Removes all of the messages being displayed
	 * via the HUD.
	 */
	public static void clearMessages() {
		messages.clear();
	}
	
	private String removeDecimal(float num) {
		String str = String.valueOf(num);
		return str.substring(0, str.indexOf("."));
	}
	
	/**
	 * Disposes of all resources of this class.
	 */
	public void dispose() {
		
		// Objects
		stage.dispose();
		font.dispose();
		
		// Textures / Buttons
		gameOver.dispose();
		fuelIcon.dispose();
		retryButton.dispose();
		
	}
	
}
