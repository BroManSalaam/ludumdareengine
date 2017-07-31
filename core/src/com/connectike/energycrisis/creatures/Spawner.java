package com.connectike.energycrisis.creatures;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.connectike.energycrisis.screens.PlayScreen;
import com.connectike.energycrisis.tools.Constants;

public class Spawner {

	private World w;
	private Player p;
	ArrayList<Enemy> enemyList;
	Viewport view;

	public Spawner(World w, Player p, ArrayList<Enemy> enemyList, Viewport view) {
		this.w = w;
		this.p = p;
		this.enemyList = enemyList;
		this.view = view;
	}

	// /**
	// * Will create a random number of enemies
	// * (0-MAX) along the edge of the screen.
	// */
	// public static void spawnEnemies(ArrayList<Enemy> enemyList, Player p,
	// World worldS) {
	//
	// Random rand = new Random();
	// final int MAX = 1;
	// final int CHANCE = 100; // Makes the chance 1 in CHANCE
	// int num = 0;
	//
	// for(int s = 0; s < MAX; s++) {
	// num = rand.nextInt(CHANCE);
	//
	// // If the number can be divided by the multiplier
	// // without a remainder
	// if(num % CHANCE == 0) {
	// enemyList.add(new Raider(p, world));
	// enemy = enemyList.get(enemyList.size() - 1);
	// enemy.setRandomPosition(view.getScreenHeight());
	// }
	// }
	//
	// }
	
	/**
	 * Will spawn a horde / group of monsters at
	 * a random point on the map.
	 * 
	 * @param count
	 * A positive integer that will dictate the
	 * size of this horde. The suggested value is
	 * <code>PlayScreen.currentWave * Constants.HORDE_MULTIPLIER</code>.
	 * If the variable is set to 0 or a negative
	 * number, it will be set to the suggested
	 * value.
	 */
	public void spawnHorde(int count) {

		Random rand = new Random();
		Enemy e;
		ArrayList<Enemy> hordeList = new ArrayList<Enemy>();
		
		
		if(count <= 0) {
			count = PlayScreen.currentWave * Constants.HORDE_MULTIPLIER;
		}
		
		for (int i = 0; i < count; i++) {
			
			// 1 in 6 chance to spawn a Bandit
			if (rand.nextInt(Constants.BANDIT_SPAWN_FREQUENCY) == 0) {
				e = new Bandit(w);
				// spawn on generator
				e.body.setTransform(2752, 1600, e.body.getAngle());
				hordeList.add(e);
				
			} else {
				
				// a random spawn point from the top 80% of the map
				final Vector2 spawnPoint = getRandomPosition(0, 3200, 0, 3200);

				if (spawnPoint.x > 1500 && spawnPoint.x < 1650) {
					spawnPoint.x *= 1.35f;
				}

				if (spawnPoint.y < 1750 && spawnPoint.y > 1500) {
					spawnPoint.y *= 1.35f;
				}
				
				e = new Raider(p, w);
				e.body.setTransform(spawnPoint, e.body.getAngle());
				hordeList.add(e);
			}
		}
		
		enemyList.addAll(hordeList);
	}

	public void spawnWave() {
		
		Enemy e = new Raider(p, w);
		e.body.setTransform(new Vector2(1600, 1600), e.body.getAngle());
		enemyList.add(e);
	}

	public void setAtTile(int row, int col, Enemy e) {
		e.body.getPosition().set(new Vector2(row * Constants.TILE_SIZE, col * Constants.TILE_SIZE));
	}

	/**
	 * Sets this enemy at a random location.
	 */
	public Vector2 getRandomPosition(int min1, int max1, int min2, int max2) {
		Random rand = new Random();
		return new Vector2(rand.nextInt((max1 - min1) + 1) + min1, rand.nextInt((max2 - min2) + 2) + min2);
	}
}
