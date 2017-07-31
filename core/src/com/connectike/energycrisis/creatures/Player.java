package com.connectike.energycrisis.creatures;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.connectike.energycrisis.Assets;
import com.connectike.energycrisis.buildings.Generator;
import com.connectike.energycrisis.screens.PlayScreen;
import com.connectike.energycrisis.tools.Constants;

public class Player extends Creature {
	
	public static float voltage = 10;
	public static float voltageDecay = 3/60f;
	
	// hp/second = regen * 1/60
	public float regen = 10;

	private boolean hasFuel = false;
	private float spearLength = 35.0f;
	
	private boolean isInvincible = false;
	private long invincibilityDuration = 500;
	// Time that player has been attacking
	private float attackTime = 0.0f;
	public int enemiesKilled = 0;
	
	private float calcVoltTime = 0.0f;
	private float waveTime = 0.0f;
	private float invincTime = 0.0f;
	private boolean isSoundPlaying = false;
	
	public Player(World world) {

		super();
		
		this.hp = 100;
		this.atk = 500;
		this.def = 5;
		this.spd = 5 * Constants.VOLTAGE_SPEED_MULTIPLIER;
		
		loadAnimations();
		definePlayer(world);
		
	}
	
	@Override
	public void update(float delta) {
		
		// Calculate voltage
		calcVoltTime = calcVoltTime + delta;
		if(calcVoltTime > (Constants.VOLTAGE_CHARGE_INTERVAL / 1000.0f)) {
			calculateVoltage();
			calcVoltTime = 0.0f;
		}
		
		// Wave counter
		waveTime = waveTime + delta;
		// equation for time
		//	number of iterations * ms
		// 1/WAVE_INCREASE * VOLTAGE/1000 = TIME
		if(waveTime > (10000.0f / voltage / 1000.0f)) {
			if(!(hp <= 0)) {
				PlayScreen.waveCount += Constants.WAVE_INCREASE;

				// more voltage - shorter sleep - faster wave
				
				if(voltage >= 40) {
					
				}
				
				if(voltage >= 65) {
					
				}
				
				if(voltage >= 90) {
					
				}
			} else {
				System.out.println("wave clock has stopped");
			}
			
			waveTime = 0.0f;
		}
		
		// Invincibility
		invincTime = invincTime + delta;
		if(invincTime > invincibilityDuration / 1000.0f) {
			isInvincible = false;
			invincTime = 0.0f;
		}
		
		
		if(isAttacking()) {
			

			
			attackTime = attackTime + delta;
			
			// Create the spear
			if(attackTime > 0.75f) {
				if(body.getFixtureList().size < 3) {
					FixtureDef spearFixture = new FixtureDef();
					Vector2 spearEdge = null;
					
					if(this.currentState == Player.State.UP) {
						spearEdge = new Vector2(0.0f, spearLength);
					} else if(this.currentState == Player.State.RIGHT) {
						spearEdge = new Vector2(spearLength, 0.0f);
					} else if(this.currentState == Player.State.DOWN) {
						spearEdge = new Vector2(0.0f, -spearLength);
					} else if(this.currentState == Player.State.LEFT) {
						spearEdge = new Vector2(-spearLength, 0.0f);
					} else { // To prevent a null in case the player is dead
						spearEdge = new Vector2(0.0f, -spearLength);
					}
					
					EdgeShape spearShape = new EdgeShape();
					spearShape.set(new Vector2(0.0f, 0.0f ), spearEdge);
					
					spearFixture.shape = spearShape;
					spearFixture.isSensor = true;
					spearFixture.filter.categoryBits = Constants.BIT_PROJ;
					spearFixture.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_ENEMY;
					
					body.createFixture(spearFixture).setUserData("spear");;
				}
				
			}
			
			if(attackTime >= (atkbuf / 1000) - 0.50f && !isSoundPlaying) {
				System.out.println("playing sound");
				long attack = PlayScreen.playerAttack.play();
				PlayScreen.playerAttack.setVolume(attack, 0.2f);
				
				isSoundPlaying = true;
			}
			
			if(attackTime >= (atkbuf / 1000)) {
				setAttackBuffering(false);
				setAttacking(false);
				attackTime = 0.0f;

				isSoundPlaying = false;
				
				Fixture fixture = body.getFixtureList().get(body.getFixtureList().size - 1);
				if ((fixture.getUserData() == null) || (fixture.getUserData().equals("spear"))) {
					body.destroyFixture(body.getFixtureList().get(body.getFixtureList().size - 1));
				}
			}
		}
		
	}
	
	public void calulateWave() {
		// TODO Calculate wave
	}

	
	public void loadAnimations() {
		// itialize texture region arrays with 3 frames, so 3 texture regions
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
		

		spriteSheet = Assets.manager.get(Assets.playerSprite);

		// splits sprite sheet up into a 32x32 grid and returns a
		// multidimensional array to reference the tiles
		// example: upTemp[1][2] would get the second column third row
		// formats in [row, column]
		TextureRegion[][] spriteGrid = TextureRegion.split(spriteSheet, 64, 64);

		// UP
		int frameIndex = 0;

		// loop through 3 columns
		for (int i = 0; i < 9; i++) {
			// index for the three different frames in the animation
			upFrames[frameIndex++] = spriteGrid[8][i];
		}

		// LEFT
		frameIndex = 0;

		for (int i = 0; i < 9; i++) {
			leftFrames[frameIndex++] = spriteGrid[9][i];
		}

		// DOWN
		frameIndex = 0;

		for (int i = 0; i < 9; i++) {
			downFrames[frameIndex++] = spriteGrid[10][i];
		}
		
		// RIGHT
		frameIndex = 0;

		for (int i = 0; i < 9; i++) {
			rightFrames[frameIndex++] = spriteGrid[11][i];
		}

		// 0.33f - amount of time each frame will play before it switches to the
		// next
		upAnimation = new Animation<TextureRegion>(30f / 60f, upFrames);
		downAnimation = new Animation<TextureRegion>(0.33f, downFrames);
		leftAnimation = new Animation<TextureRegion>(0.33f, leftFrames);
		rightAnimation = new Animation<TextureRegion>(0.33f, rightFrames);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// ATTACK ANIMATIONS
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		frameIndex = 0;
		
		// loop through 3 columns
		for (int i = 0; i < 8; i++) {
			// index for the three different frames in the animation
			upAttackFrames[frameIndex++] = spriteGrid[4][i];
		}

		// LEFT
		frameIndex = 0;

		for (int i = 0; i < 8; i++) {
			leftAttackFrames[frameIndex++] = spriteGrid[5][i];
		}

		// DOWN
		frameIndex = 0;

		for (int i = 0; i < 8; i++) {
			downAttackFrames[frameIndex++] = spriteGrid[6][i];
		}

		// RIGHT
		frameIndex = 0;

		for (int i = 0; i < 8; i++) {
			rightAttackFrames[frameIndex++] = spriteGrid[7][i];
		}
		
		// DEAD
		
		frameIndex = 0;

		for (int i = 0; i < 6; i++) {
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
	
	/**
	 * takes values from all sources and set the players new voltage
	 */
	public void calculateVoltage() {		
		voltage -= voltageDecay;
		
		// if we dont go over the threshold
		if(voltage + Generator.chargeRate < Constants.VOLTAGE_MAX) {
			voltage += Generator.chargeRate;
		}
		
		spd = 5 * Constants.VOLTAGE_SPEED_MULTIPLIER;
	}
	
	private void definePlayer(World world) {
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		
		bodyDef.position.set((Constants.V_WIDTH * Constants.TILE_SIZE / 2.0f), (Constants.V_HEIGHT * Constants.TILE_SIZE / 2.0f) - 100);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodyDef);
		
		// Half width and half height
		shape.setAsBox(Constants.PLAYER_SIZE / 2, Constants.PLAYER_SIZE / 2);
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.BIT_PLAYER;
		fixtureDef.filter.maskBits = Constants.BIT_ENEMY | Constants.BIT_ITEM | Constants.BIT_COLLIDE | Constants.BIT_AGGRO | Constants.BIT_PROJ;
		body.createFixture(fixtureDef).setUserData("full-body");
		
		shape.setAsBox((Constants.PLAYER_SIZE / 2) + 5, (Constants.PLAYER_SIZE / 2) + 5);
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.BIT_PLAYER;
		fixtureDef.filter.maskBits = Constants.BIT_ENEMY | Constants.BIT_ITEM | Constants.BIT_COLLIDE | Constants.BIT_AGGRO | Constants.BIT_PROJ;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef).setUserData("full-body");;
		
	}
	
	/**
	 * Sets the location of the player
	 * 
	 * @param pos
	 * the new position of the player
	 */
	public void setLocation(Vector2 pos) {
		body.setTransform(pos, body.getAngle());
	}
	
	// Get / Set methods
	
	public float getVoltage() {
		return voltage;
	}
	
	public void takeDmg(float dmg) {
		
		if (isInvincible) return;
		
		isInvincible = true;
		this.hp -= (dmg - def);
		
	}


	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}
	
	public void addVoltage(float addition) {
		voltage = voltage + addition;
	}
	
	public void subtractVoltage(float subtraction) {
		voltage = voltage - subtraction;
	}


	public float getVoltageDecay() {
		return voltageDecay;
	}


	public void setVoltageDecay(float voltageDecay) {
		this.voltageDecay = voltageDecay;
	}
	
	public boolean getHasFuel() {
		return this.hasFuel;
	}
	
	public void setHasFuel(boolean hasFuel) {
		this.hasFuel = hasFuel;
	}
	
	public float getSpearLength() {
		return spearLength;
	}
	
	public void getSpearLength(float spearLength) {
		this.spearLength = spearLength;
	}
	
	public Body getBody() {
		return body;
	}


	public void setBody(Body body) {
		this.body = body;
	}



	
	
	
}
