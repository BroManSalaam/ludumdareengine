package com.connectike.energycrisis.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player implements Entity {

	private int hp;
	private int def;
	private int spd;
	private int atk;

	private Texture spriteSheet;

	// animations for differant directions
	public Animation<TextureRegion> upAnimation;
	public Animation<TextureRegion> leftAnimation;
	public Animation<TextureRegion> downAnimation;
	public Animation<TextureRegion> rightAnimation;
	public Animation<TextureRegion> deadAnimation;
	public Animation<TextureRegion> idleAnimation;

	public enum State {
		UP, LEFT, DOWN, RIGHT, IDLE, DEAD
	};

	public State currentState = State.IDLE;
	public State previousState = State.IDLE;
	public boolean isAnimationLooping = false;

	private TextureRegion[] upFrames;
	private TextureRegion[] leftFrames;
	private TextureRegion[] downFrames;
	private TextureRegion[] rightFrames;

	public Player(int hp, int def, int spd, int atk) {

		this.hp = hp;
		this.def = def;
		this.spd = spd;
		this.atk = atk;

		// initialize texture region arrays with 3 frames, so 3 texture regions
		// in each array
		upFrames = new TextureRegion[3];
		leftFrames = new TextureRegion[3];
		downFrames = new TextureRegion[3];
		rightFrames = new TextureRegion[3];

		spriteSheet = new Texture(Gdx.files.internal("spritesheets/player.png"));

		// splits sprite sheet up into a 32x32 grid and returns a
		// multidimensional array to reference the tiles
		// example: upTemp[1][2] would get the second column third row
		// formats in [row, column]
		TextureRegion[][] spriteGrid = TextureRegion.split(spriteSheet, 32, 32);

		// up
		int frameIndex = 0;

		// loop through 3 columns
		for (int i = 0; i < 3; i++) {
			// index for the three different frames in the animation
			upFrames[frameIndex++] = spriteGrid[3][i];
		}

		// left
		frameIndex = 0;

		for (int i = 0; i < 3; i++) {
			leftFrames[frameIndex++] = spriteGrid[1][i];
		}

		// down
		frameIndex = 0;

		for (int i = 0; i < 3; i++) {
			downFrames[frameIndex++] = spriteGrid[0][i];
		}

		// right
		frameIndex = 0;

		for (int i = 0; i < 3; i++) {
			rightFrames[frameIndex++] = spriteGrid[2][i];
		}

		// 0.33f - amount of time each frame will play before it switches to the
		// next
		upAnimation = new Animation<TextureRegion>(30f / 60f, upFrames);
		downAnimation = new Animation<TextureRegion>(0.33f, downFrames);
		leftAnimation = new Animation<TextureRegion>(0.33f, leftFrames);
		rightAnimation = new Animation<TextureRegion>(0.33f, rightFrames);
		idleAnimation = new Animation<TextureRegion>(0.33f, downFrames[1]);
		
	}

	/**
	 * return the animation the player is currently doing
	 * 
	 * @return
	 */
	public Animation<TextureRegion> getCurrentAnimation() {
		return (currentState == State.UP ? upAnimation
				: (currentState == State.LEFT ? leftAnimation
						: (currentState == State.DOWN ? downAnimation
								: (currentState == State.RIGHT ? rightAnimation
										: (currentState == State.DEAD ? deadAnimation
												: (currentState == State.IDLE ? idleAnimation : null))))));

	}

	public void update() {

	}

	public void dispose() {
		spriteSheet.dispose();
	}
}
