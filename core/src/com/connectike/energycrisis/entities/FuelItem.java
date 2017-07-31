package com.connectike.energycrisis.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.connectike.energycrisis.creatures.Player;
import com.connectike.energycrisis.tools.Constants;

public class FuelItem implements PickupItem {
	
	/**
	 * Initializes a fuel item at the world
	 * coordinates of (0, 0) and the default
	 * fuel texture. This method requires
	 * {@linkplain #initBodyFixture(World)} to
	 * be called after the constructor is called.
	 * 
	 * @see
	 * {@linkplain #initBodyFixture(World)}
	 */
	public FuelItem() {
		
	}
	
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
		player.setHasFuel(true);
	}
	
	
	
}
