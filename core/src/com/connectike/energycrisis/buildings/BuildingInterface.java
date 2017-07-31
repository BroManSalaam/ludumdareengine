package com.connectike.energycrisis.buildings;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.connectike.energycrisis.creatures.Player;

public interface BuildingInterface {
		
	/**
	 * Method used when the player interacts with
	 * this building.
	 * 
	 * @param player
	 * the player object
	 */
	public void interact(Player player);
	
	/**
	 * Initialize the  body and fixture of
	 * this building.
	 * 
	 * @param world
	 * the world of this building
	 * @param bounds
	 * the dimensions of this building
	 */
	public void initBodyFixture(World world, Rectangle bounds);
	
	// Get / Set methods
	
	
}
