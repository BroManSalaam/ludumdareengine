package com.connectike.energycrisis.ui;


public interface MenuInterface {
	
	
	/**
	 * Used to initialize all of the buttons
	 * and textures that will be used in this
	 * menu.
	 */
	public void initialize();
	
	/**
	 * This method should be called every render look
	 * and will be responsible for changing the 
	 * @param delta
	 */
	public void update(float delta);
	
	// Get / Set methods
	
	public boolean getVisibility();
	public void setVisibility(boolean visible);
	
}
