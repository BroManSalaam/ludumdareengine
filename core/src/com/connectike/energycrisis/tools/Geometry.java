package com.connectike.energycrisis.tools;

import com.badlogic.gdx.math.Vector2;

public class Geometry {

	public static double distance(Vector2 object1, Vector2 object2){
	    return Math.sqrt(Math.pow((object2.x - object1.x), 2) + Math.pow((object2.y - object1.y), 2));
	}
}
