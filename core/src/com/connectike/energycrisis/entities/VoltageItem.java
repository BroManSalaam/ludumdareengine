package com.connectike.energycrisis.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.connectike.energycrisis.creatures.Player;
import com.connectike.energycrisis.tools.Constants;

public class VoltageItem implements PickupItem {
	
	/**
	 * Initializes a voltage item at the world
	 * coordinates of (0, 0) and the default
	 * voltage texture. This method requires
	 * {@linkplain #initBodyFixture(World)} to
	 * be called after the constructor is called.
	 * 
	 * @see
	 * {@linkplain #initBodyFixture(World)}
	 */
	public VoltageItem() {
		
		// Must call initBodyFixture function after
	}
	
	/**
	 * Creates a new voltage items at the given
	 * world coordinates, x and y. It also will
	 * initialize a body with a fixture and add
	 * it to the world.
	 * 
	 * @param x
	 * world x position of this item
	 * @param y
	 * world y position of this item
	 * @param world
	 * the world that will contain this item
	 */
	public VoltageItem(float x, float y, World world) {
		
		// Must call initBodyFixture after
		
	}
	
	// Need another constructor for a Vector2?
	
	@Override
	public void initBodyFixture(World world, Rectangle bounds) {
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		Body body;
		Fixture fixture;
		
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(bounds.getX() + bounds.width / 2, bounds.getY() + bounds.getHeight() / 2);
		body = world.createBody(bodyDef);
		
		// Half the width and height, so the size is actually 32x32
		shape.setAsBox(bounds.getWidth() / 2, bounds.getHeight() / 2);
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.BIT_ITEM;
		fixtureDef.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_ENEMY;
		
		fixture = body.createFixture(fixtureDef);
		fixture.setUserData(this);
		
	}
	
	@Override
	public void pickUp() {
		
	}
	
	@Override
	public void pickUp(Player player) {
		if(player.getVoltage() < Constants.VOLTAGE_MAX) {
			player.addVoltage(3.0f);
			// TODO: Fine tune this value
			
			if(player.getVoltage() > Constants.VOLTAGE_MAX) {
				player.setVoltage(Constants.VOLTAGE_MAX);
			}
		}
	}
	
	// Get / Set methods
	
	
	
}
