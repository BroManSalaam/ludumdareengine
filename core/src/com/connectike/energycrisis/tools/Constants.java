package com.connectike.energycrisis.tools;

import com.badlogic.gdx.math.Vector2;

public class Constants {

	// secs between each voltage update
	public static final int VOLTAGE_CHARGE_INTERVAL = 250;
	
	public static final int GENERATOR_MAX_CHARGE_RATE = 10;
	
	// Window dimensions
	public static final int W_WIDTH = 640;
	public static final int W_HEIGHT = 480;
	// Virtual dimensions (map dimensions)
	public static final int V_WIDTH = 100;
	public static final int V_HEIGHT = 100;
	public static final int DRAW_OFFSET = 18;
	
	public static final int TILE_SIZE = 32;
	public static final int PLAYER_SIZE = 40;
	public static final Vector2 PLAYER_SPAWN = new Vector2(2656, 1632);
	
	public static final int PPM = 50;
	
	// Bits
	public static final short BIT_DEFAULT = 2; // Is ignored by player
	public static final short BIT_PLAYER = 4;
	public static final short BIT_ENEMY = 8;
	public static final short BIT_COLLIDE = 16;
	public static final short BIT_ITEM = 32;
	public static final short BIT_AGGRO = 64;
	public static final short BIT_PROJ = 128; // For projectiles / weapons
	
	public static final float RAIDER_STAB_RANGE = 1.1f;
	
	public static final float BANDIT_STAB_RANGE = 20.0f;
	public static final int BANDIT_SPAWN_FREQUENCY = 2;
	
	// wave X multiplier
	public static final int HORDE_MULTIPLIER = 2;
	
	
	public static final float WAVE_INCREASE = 0.008f;
	// higher - less enemies
	public static final int WAVE_VOLTAGE_MULTIPLIER = 10;
	
	//15% of current voltage
	public static final float VOLTAGE_SPEED_MULTIPLIER = 1.15f;
	public static final int VOLTAGE_MAX = 100;
	
	public static final float FUEL_VOLTAGE_GENERATION = 0.05f;
	public static final int FUEL_DROP_RANGE = 10 ; // 1 in this chance
	
	public static final int MINIMUM_ENEMY_PERCENTAGE_TO_CONTINUE = 15;
	
}
