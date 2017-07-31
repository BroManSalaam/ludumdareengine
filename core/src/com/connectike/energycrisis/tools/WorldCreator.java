package com.connectike.energycrisis.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.connectike.energycrisis.buildings.Generator;
import com.connectike.energycrisis.entities.FuelItem;
import com.connectike.energycrisis.entities.VoltageItem;

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
		
		createWorld();
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
		
		// For walls (Rectangle)
		for (MapObject object : map.getLayers().get(getLayerIndex("Walls")).getObjects().getByType(RectangleMapObject.class)) {
			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			// set properties
			
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
			
			// Create body into the world
			body = world.createBody(bdef);
			
			shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
			fdef.shape = shape;
			fdef.filter.categoryBits = Constants.BIT_COLLIDE;
			fdef.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_ENEMY;
			
			// Create the specified fixture
			body.createFixture(fdef);
		}
		
		// Walls (Polyline)
		for (MapObject object : map.getLayers().get(getLayerIndex("Walls")).getObjects().getByType(PolylineMapObject.class)) {
			
			Polyline line = ((PolylineMapObject) object).getPolyline();
			float[] vertices = line.getTransformedVertices();
			ChainShape chainShape = new ChainShape();
			
			// set properties
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set(line.getOriginX(), line.getOriginY());
			
			// Create body into the world
			body = world.createBody(bdef);
			
			chainShape.createChain(vertices);
			//shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
			fdef.shape = chainShape;
			fdef.filter.categoryBits = Constants.BIT_COLLIDE;
			fdef.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_ENEMY;
			
			// Create the specified fixture
			body.createFixture(fdef);
		}
		
		// For voltage items
		for (MapObject object : map.getLayers().get(getLayerIndex("Voltage Items")).getObjects().getByType(RectangleMapObject.class)) {
			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new VoltageItem().initBodyFixture(world, rect);
			
		}
		
		// For fuel items
		for (MapObject object : map.getLayers().get(getLayerIndex("Fuel Items")).getObjects().getByType(RectangleMapObject.class)) {
			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new FuelItem().initBodyFixture(world, rect);
			
		}
		
		// TODO: Adjust generator position
		// Create the generator
		Rectangle generatorRect = new Rectangle(2752, 1600, 64, 64);
		new Generator().initBodyFixture(world, generatorRect);
		
	}
	
	/**
	 * Gets the index of the desired layer based on the
	 * given name. Format all names as they appear
	 * in Tiled editor.
	 * 
	 * @param name
	 * the name of the desired layer index
	 * @return
	 * The index of the layer with the matching name.
	 * Will return -1 if no match is found
	 */
	private int getLayerIndex(String name) {
		
		FileHandle handler = Gdx.files.internal("maps/main_map.tmx");
		String content = handler.readString();
		int nameIndex = 0; // Index of "name"
		String layerName = ""; // Current parsed layer name
		int layerCount = 0; // Incrememnet this for every layer
		
		while(nameIndex != -1) {
			
			// +6 to remove name="   and +1 to get the next name
			nameIndex = content.indexOf("name=\"", nameIndex + 1) + 6;
			if(nameIndex != -1) {
				layerName = content.substring(nameIndex, content.indexOf("\"", nameIndex));
				
				if(layerName.equalsIgnoreCase(name)) {
					return layerCount;
				} else {
					layerCount++;
				}
			}
			
		}
		
		Gdx.app.log("getLayerIndex", "Layer couldn't be found with name: " + name);
		return -1;
	}
	
	private float distance(float x1, float y1, float x2, float y2) {
		
		double temp = Math.pow((x2 - x1), 2);
		temp = temp + Math.pow((y2 - y1), 2);
		return (float) Math.sqrt(temp);
		
	}
	
}
