package com.connectike.energycrisis.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {
	
	private TextureRegion texture[] = new TextureRegion[2];
	private Rectangle position;
		
	/**
	 * Class used for user interactions in
	 * menus.
	 * 
	 * @param x
	 * the bottom-left x position of this button
	 * @param y
	 * the bottom-left y position of this button
	 * @param width
	 * the width of this button
	 * @param height
	 * the height of this button
	 */
	public Button(float x, float y, float width, float height) {
		
		position = new Rectangle(x, y, width, height);
		
	}
	
	/**
	 * Does basic house-keeping things, such as
	 * loading the textures of this button.
	 * 
	 * @param pathToImage
	 * path to the button sprite-sheet
	 * @param size
	 * the size of a <u>singular</u> button image,
	 * not the size of <code>pathToImage</code>
	 */
	public void initialize(String pathToImage, Vector2 size) {
		
		Texture spritesheet = new Texture(Gdx.files.internal(pathToImage));
		
		texture[0] = new TextureRegion(spritesheet, 0, 0, 200, 100);
		
		// If a hovering texture was loaded
		if(spritesheet.getHeight() > size.y) {
			texture[1] = new TextureRegion(spritesheet, 0, 100, 200, 100);
		}
	}
		
	/**
	 * Gets the texture of this button, which
	 * may depend on if the user is hovering
	 * over the button or not.
	 * 
	 * @return
	 * The texture of this button.
	 */
	public TextureRegion getTexture() {
		if((isHovering()) && (texture[1] != null)) return texture[1];
		return texture[0];
	}
	
	/**
	 * Returns a rectangle encompassing
	 * the button.
	 * 
	 * @return
	 * A rectangle with the x, y position of
	 * the button, along with the width and height.
	 */
	public Rectangle getBounds() {
		return position;
	}
	
	/**
	 * Returns the center of this button by
	 * adding (to the x and y position) half
	 * the width and height.
	 * 
	 * @return
	 * A Vector2 object representing the
	 * centered coorindate system of this button. 
	 */
	public Vector2 getCenteredPosition() {
		return new Vector2(position.x + position.width / 2, position.y + position.height);
	}
	
	/**
	 * Returns true if the mouse pointer is 
	 * within the bounds of this button.
	 * 
	 * @return
	 * True, if the mouse if hovering over
	 * the button. Otherwise, false.
	 */
	public boolean isHovering() {
		return position.contains(Gdx.input.getX(), Gdx.input.getY());
	}
	
	/**
	 * This method will return a value based
	 * on if this button has been pressed since
	 * initialization.
	 * 
	 * @return
	 * True, if the button has been pressed.
	 * Otherwise, false.
	 */
	public boolean getState() {
		// If the mouse is within the button bounds,
		// and was just clicked
		if((isHovering()) && (Gdx.input.justTouched())) return true;
		return false;
	}
	
	/**
	 * Disposes of the texture(s) for this button.
	 */
	public void dispose() {
		texture[0].getTexture().dispose();
		if(texture[1].getTexture() != null) {
			texture[1].getTexture().dispose();
		}
	}
	
}
