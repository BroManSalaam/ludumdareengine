package com.connectike.energycrisis.buildings;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.connectike.energycrisis.creatures.Player;
import com.connectike.energycrisis.tools.Constants;

public class Generator implements BuildingInterface {

	public static float chargeRate = 4/60f;
	public static Body body;
	
	/**
	 * Building that is the center of the game
	 */
	public Generator() {
		
	}
	
	public static void reduceCharge(float atk) {
		if(chargeRate - atk >= 0) {
			chargeRate -= atk;
		} else {
			chargeRate = 0;
		}
	}
	
	
	@Override
	public void interact(Player player) {
		
		if(player.getHasFuel()) {
			if(player.getVoltage() < Constants.VOLTAGE_MAX) {
				Generator.chargeRate += Constants.FUEL_VOLTAGE_GENERATION;
				player.setHasFuel(false);
				
				if(player.getVoltage() > Constants.VOLTAGE_MAX) {
					player.setVoltage(Constants.VOLTAGE_MAX);
				}
			}
		}
		
	}

	@Override
	public void initBodyFixture(World world, Rectangle bounds) {
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		Fixture fixture;
		
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(bounds.getX(), bounds.getY());
		body = world.createBody(bodyDef);
		
		shape.setAsBox(bounds.getWidth() / 2, bounds.getHeight() / 2);
		fixDef.shape = shape;
		fixDef.filter.categoryBits = Constants.BIT_COLLIDE;
		fixDef.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_ENEMY;
		
		fixture = body.createFixture(fixDef);
		fixture.setUserData(this);
		// Will this building to be identified later
		
	}
	
	
	
}
