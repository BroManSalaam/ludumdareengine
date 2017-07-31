package com.connectike.energycrisis.creatures;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.connectike.energycrisis.Assets;
import com.connectike.energycrisis.buildings.Generator;
import com.connectike.energycrisis.tools.Constants;
import com.connectike.energycrisis.tools.Geometry;

public class Bandit extends Enemy {
	
	private float attackTime = 0.0f;
	
	public Bandit(World w) {
		
		super(w);
		
		this.hp = 10;
		this.atk = 0.025f;
		this.def = 5;
		this.spd = 10;
		this.atkbuf = 500;
		
		loadAnimations();
	}
	
	@Override
	public void update(float delta) {
		
		// Attack buffer
		// Attack buffer
		attackTime = attackTime + delta;
		if(attackTime >= atkbuf / 1000.0f) {
			isAttacking = false;
			isAttackBuffering = false;
			attackTime = 0.0f;
		}
		
		if(hp <= 0) {
			
			die();
			
		} else {
			
			if((Geometry.distance(Generator.body.getPosition(), body.getPosition()) < atkRadius.getRadius()
			        * Constants.BANDIT_STAB_RANGE) && !isAttackBuffering) {
				isAttacking = true;
				isAttackBuffering = true;
				
			}
			
			if(!isAttacking) {
				
				// RIGHT
				if(Generator.body.getPosition().x > body.getPosition().x) {
					body.applyLinearImpulse(new Vector2(spd, 0.0f), body.getWorldCenter(), true);
					currentState = State.RIGHT;
				}
				
				// LEFT
				if(Generator.body.getPosition().x < body.getPosition().x) {
					body.applyLinearImpulse(new Vector2(-spd, 0.0f), body.getWorldCenter(), true);
					currentState = State.LEFT;
				}
				
				// UP
				if(Generator.body.getPosition().y > body.getPosition().y) {
					body.applyLinearImpulse(new Vector2(0.0f, spd), body.getWorldCenter(), true);
					currentState = State.UP;
				}
				
				// DOWN
				if(Generator.body.getPosition().y < body.getPosition().y) {
					body.applyLinearImpulse(new Vector2(0.0f, -spd), body.getWorldCenter(), true);
					currentState = State.DOWN;
				}
				
			} else {
				
				// end of attack animation
				if(getCurrentAnimation().getKeyFrameIndex(elapsedTime) == 7) {
					Generator.reduceCharge(atk);
					
					isAttacking = false;
					elapsedTime = 0;
					
				}
			}
		}
	}
	
	@Override
	public void loadAnimations() {
		
		// initialize texture region arrays with 3 frames, so 3 texture regions
		// in each array
		upFrames = new TextureRegion[9];
		leftFrames = new TextureRegion[9];
		downFrames = new TextureRegion[9];
		rightFrames = new TextureRegion[9];
		deadFrames = new TextureRegion[6];
		
		upAttackFrames = new TextureRegion[8];
		leftAttackFrames = new TextureRegion[8];
		downAttackFrames = new TextureRegion[8];
		rightAttackFrames = new TextureRegion[8];
		
		spriteSheet = Assets.manager.get(Assets.banditSprite);
		
		TextureRegion[][] spriteGrid = TextureRegion.split(spriteSheet, 64, 64);
		
		// UP
		int frameIndex = 0;
		
		for(int i = 0; i < 9; i++) {
			upFrames[frameIndex++] = spriteGrid[8][i];
		}
		
		// LEFT
		frameIndex = 0;
		
		for(int i = 0; i < 9; i++) {
			leftFrames[frameIndex++] = spriteGrid[9][i];
		}
		
		// DOWN
		frameIndex = 0;
		
		for(int i = 0; i < 9; i++) {
			downFrames[frameIndex++] = spriteGrid[10][i];
		}
		
		// RIGHT
		frameIndex = 0;
		
		for(int i = 0; i < 9; i++) {
			rightFrames[frameIndex++] = spriteGrid[11][i];
		}
		
		upAnimation = new Animation<TextureRegion>(30f / 60f, upFrames);
		downAnimation = new Animation<TextureRegion>(0.33f, downFrames);
		leftAnimation = new Animation<TextureRegion>(0.33f, leftFrames);
		rightAnimation = new Animation<TextureRegion>(0.33f, rightFrames);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// ATTACK ANIMATIONS
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		frameIndex = 0;
		
		// loop through 3 columns
		for(int i = 0; i < 8; i++) {
			// index for the three different frames in the animation
			upAttackFrames[frameIndex++] = spriteGrid[4][i];
		}
		
		// LEFT
		frameIndex = 0;
		
		for(int i = 0; i < 8; i++) {
			leftAttackFrames[frameIndex++] = spriteGrid[5][i];
		}
		
		// DOWN
		frameIndex = 0;
		
		for(int i = 0; i < 8; i++) {
			downAttackFrames[frameIndex++] = spriteGrid[6][i];
		}
		
		// RIGHT
		frameIndex = 0;
		
		for(int i = 0; i < 8; i++) {
			rightAttackFrames[frameIndex++] = spriteGrid[7][i];
		}
		
		// DEAD
		
		frameIndex = 0;
		
		for(int i = 0; i < 6; i++) {
			deadFrames[frameIndex++] = spriteGrid[20][i];
		}
		
		// 0.15f - amount of time each frame will play before it switches to the
		// next
		upAttackAnimation = new Animation<TextureRegion>(0.15f, upAttackFrames);
		downAttackAnimation = new Animation<TextureRegion>(0.15f, downAttackFrames);
		leftAttackAnimation = new Animation<TextureRegion>(0.15f, leftAttackFrames);
		rightAttackAnimation = new Animation<TextureRegion>(0.15f, rightAttackFrames);
		deadAnimation = new Animation<TextureRegion>(0.25f, deadFrames);
	}
	
	@Override
	protected void defineEnemy(World world) {
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		atkRadius = new CircleShape();
		
		bodyDef.position.set((Constants.V_WIDTH * Constants.TILE_SIZE / 2.0f),
		        (Constants.V_HEIGHT * Constants.TILE_SIZE / 2.0f));
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodyDef);
		
		// Half width and half height
		shape.setAsBox(30 / 2, 30 / 2);
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.BIT_ENEMY;
		fixtureDef.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_ITEM
		        | Constants.BIT_COLLIDE | Constants.BIT_PROJ;
		// body.createFixture(fixtureDef).setUserData("enemy-body");
		body.createFixture(fixtureDef).setUserData(this);
		
		// attack radius
		atkRadius.setRadius(50);
		fixtureDef.shape = atkRadius;
		fixtureDef.filter.categoryBits = Constants.BIT_AGGRO;
		fixtureDef.filter.maskBits = Constants.BIT_PLAYER;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef).setUserData(this);
	}
	
}
