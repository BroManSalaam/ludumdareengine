package com.connectike.energycrisis.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.connectike.energycrisis.creatures.Player;

public interface PickupItem {
	
	/**
	 * Attempts to load the specified sprite-sheet
	 * and isolate the given texture and load
	 * it into the main Texture variable.
	 */
	//public void loadTexture();	
	
	/**
	 * Defines the body definition of this item,
	 * as well the it's fixture definition.
	 * 
	 * @param world
	 * the world this body will be created in
	 */
	public void initBodyFixture(World world, Rectangle bounds);
	
	/**
	 * Method that will be called when this item
	 * has been picked up.
	 */
	public void pickUp();
	
	/**
	 * Method that can be used to adjust different
	 * attributes within the player class.
	 */
	public void pickUp(Player player);
	
	// Get / Set methods
	
//	public Texture getTexture();
//	public void setTexture(Texture newTex);
//	
//	public Vector2 getPosition();
//	public void setPosition(Vector2 newPos);
	
}
