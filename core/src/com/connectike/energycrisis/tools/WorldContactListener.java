package com.connectike.energycrisis.tools;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.connectike.energycrisis.buildings.Generator;
import com.connectike.energycrisis.creatures.Enemy;
import com.connectike.energycrisis.creatures.Player;
import com.connectike.energycrisis.entities.FuelItem;
import com.connectike.energycrisis.entities.PickupItem;

public class WorldContactListener implements ContactListener {
	
	private TiledMap map;
	private Player player;
	
	/**
	 * Class t handle collisions.
	 * 
	 * @param player
	 * player object, used to adjust the statistics and
	 * and other player attributes
	 */
	public WorldContactListener(TiledMap map, Player player) {
		
		this.map = map;
		this.player = player;
		
	}
	
	@Override
	public void beginContact(Contact contact) {
				
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		//Gdx.app.log("" + fixA.getUserData(), "" + fixB.getUserData());
		
		if((fixA.getUserData() == "full-body") || (fixB.getUserData() == "full-body")) {
			Fixture body = fixA.getUserData() == "full-body" ? fixA : fixB;
			Fixture object = (body == fixA) ? fixB : fixA;
			
			// ---- Items ----
			if(object.getUserData() instanceof PickupItem) {
				
				// Fuel
				if((object.getUserData() instanceof FuelItem) && (player.getHasFuel())) {
					return;
					// If the player already has fuel, ignore the item
				}
				
				((PickupItem) object.getUserData()).pickUp(player);
				
				// Set these properties so it will be ignored next collision
				Filter filter = new Filter();
				filter.categoryBits = Constants.BIT_DEFAULT;
				object.setFilterData(filter);
				
				getCell(object.getBody(), 1).setTile(null);
			}
			// ---- Generator ----
			if(object.getUserData() instanceof Generator) {
				((Generator) object.getUserData()).interact(player);
			}
			
//			// ---- Enemy ----
//			if(object.getUserData() instanceof Enemy) {
//				((Enemy) object.getUserData()).setAttacking(true);
//				((Enemy) object.getUserData()).body.setLinearVelocity(new Vector2(0.0f, 0.0f));
//			}
			
		} else if(fixA.getUserData() == "spear" || fixB.getUserData() == "spear") {
			Fixture spear = fixA.getUserData() == "spear" ? fixA : fixB;
			Fixture target = (spear == fixA) ? fixB : fixA;
			Enemy victim;
						
			victim = ((Enemy) target.getUserData());
			
			victim.setHp(victim.getHp() - (player.getAtk() - victim.getDef()));
			player.enemiesKilled++;
			spear.setUserData(null);
		}
			
		
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
	
	/**
	 * Gets the cells / tile on the map and returns it.
	 * This can be used to change the texture of a 
	 * certain cell, i.e. after picking up an item.
	 * 
	 * @param body
	 * the body of the tile <code>fixture.getBody()</code>
	 * @param layerIndex
	 * the index of the tile layer of the desired cell
	 * @return
	 * A cell that corresponds to the position specified
	 * by the given body.
	 */
	private TiledMapTileLayer.Cell getCell(Body body, int layerIndex) {
		
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerIndex);
		return layer.getCell((int) body.getPosition().x / Constants.TILE_SIZE, 
				(int) body.getPosition().y / Constants.TILE_SIZE);
	}
	
}
