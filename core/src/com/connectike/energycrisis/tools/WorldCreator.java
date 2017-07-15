package com.connectike.energycrisis.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * a class for creating different levels and the objects within them
 * 
 * @author louck
 *
 */
public class WorldCreator {

	private TiledMap map;
	private World world;
	
	public WorldCreator(TiledMap map, World world) {
		this.map = map;
		this.world = world;
	}
	

	public void createWorld() {

		/**
		 * all of the indices for the objects in the TMX file
		 * 
		 * example:
		 * 
		 * Tiled Layer: background - 3; 
		 * Object Layer: Walls - 2; 
		 * Tiled Layer: NPC's - 1;
		 * Object Layer: Houses - 0
		 * 
		 * 
		 * we would loop through those two object layers
		 * 
		 */

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		PolygonShape shape = new PolygonShape();
		Body body;
		
		// example for rectangles
		for (MapObject object : map.getLayers().get(0).getObjects().getByType(RectangleMapObject.class)) {
			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			// set properties
			
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set(rect.width / 2, rect.height / 2);
			
			
			shape.setAsBox(rect.width / 2, rect.height / 2);
			fdef.shape = shape;
			
			// add create bodies in the world based on our definitions of them
			world.createBody(bdef).createFixture(fdef);

		}
	}

}
