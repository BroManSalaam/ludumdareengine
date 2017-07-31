package com.connectike.energycrisis.creatures;

import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Creature {
		
	public Enemy(World w) {
		super();
		defineEnemy(w);
		this.world = w;
	}
	
	protected abstract void defineEnemy(World world);

	@Override
	public abstract void update(float delta);
	
	public abstract void loadAnimations();
		
}
