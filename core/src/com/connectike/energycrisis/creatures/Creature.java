package com.connectike.energycrisis.creatures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.connectike.energycrisis.entities.Entity;
import com.connectike.energycrisis.screens.PlayScreen;

/**
 * something that can move and is animated in 4 directions
 * @author louck
 *
 */
public abstract class Creature extends Entity {

	protected float hp = 10;
	protected float def = 2;
	protected float spd = 15;
	protected float atk = 5;
	protected long atkbuf = 1000;
	
	protected boolean isAlreadyDead = false;
	
	public Body body = null;
	
	public CircleShape atkRadius;
	protected World world;

	protected Texture spriteSheet;
	
	protected boolean isAttacking = false;
	protected boolean isAttackBuffering = false;

	// animations for differant directions
	public Animation<TextureRegion> upAnimation;
	public Animation<TextureRegion> leftAnimation;
	public Animation<TextureRegion> downAnimation;
	public Animation<TextureRegion> rightAnimation;
	public Animation<TextureRegion> deadAnimation;
	
	public Animation<TextureRegion> leftAttackAnimation;
	public Animation<TextureRegion> downAttackAnimation;
	public Animation<TextureRegion> rightAttackAnimation;
	public Animation<TextureRegion> upAttackAnimation;
	

	public enum State {
		UP, LEFT, DOWN, RIGHT, DEAD
	};
	

	public State currentState = State.DOWN;
	public boolean isAnimationLooping = true;
	public float elapsedTime = 0.0f;

	protected TextureRegion[] upFrames;
	protected TextureRegion[] leftFrames;
	protected TextureRegion[] downFrames;
	protected TextureRegion[] rightFrames;
	protected TextureRegion[] deadFrames;
	
	protected TextureRegion[] upAttackFrames;
	protected TextureRegion[] leftAttackFrames;
	protected TextureRegion[] downAttackFrames;
	protected TextureRegion[] rightAttackFrames;

	/**
	 * set creature stats
	 * @param hp
	 * @param def
	 * @param spd
	 * @param atk
	 */
	public Creature() {
	}

	/**
	 * return the animation the player is currently doing
	 * 
	 * @return
	 */
	public Animation<TextureRegion> getCurrentAnimation() {
		
		if(isAttacking) {
			
			if(currentState == State.UP) {
				return upAttackAnimation;
			}
			if(currentState == State.DOWN) {
				return downAttackAnimation;
			}
			if(currentState == State.LEFT) {
				return leftAttackAnimation;
			}
			if(currentState == State.RIGHT) {
				return rightAttackAnimation;
			}
			if(currentState == State.DEAD) {
				return deadAnimation;
			}
			
		} else {
			
			if(currentState == State.UP) {
				return upAnimation;
			}
			if(currentState == State.DOWN) {
				return downAnimation;
			}
			if(currentState == State.LEFT) {
				return leftAnimation;
			}
			if(currentState == State.RIGHT) {
				return rightAnimation;
			}
			if(currentState == State.DEAD) {
				return deadAnimation;
			}
		}
		
		System.out.println("POINT OF NO RETURN");
		return null;
	}
	
	
	public void die() {
				
		if(!isAlreadyDead) {
									
			currentState = State.DEAD;
			body.setLinearVelocity(new Vector2(0,0));
			isAnimationLooping = false;
			world.destroyBody(body);
			
			isAlreadyDead = true;
			
			return;
		}
		
		if(getCurrentAnimation().getKeyFrameIndex(elapsedTime) == 7) {
			
			System.out.println("removing from list...");
			
			PlayScreen.enemyList.remove(this);
			
			System.gc();
		}
	}
	
	public abstract void update(float delta);
	
	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public float getSpd() {
		return spd;
	}

	public void setSpd(int spd) {
		this.spd = spd;
	}

	public float getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public float getAtkbuf() {
		return atkbuf;
	}

	public void setAtkbuf(int atkbuf) {
		this.atkbuf = atkbuf;
	}

	public Texture getSpriteSheet() {
		return spriteSheet;
	}

	public void setSpriteSheet(Texture spriteSheet) {
		this.spriteSheet = spriteSheet;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

	public void setAttacking(boolean isAttacking) {
		this.isAttacking = isAttacking;
	}

	public boolean isAttackBuffering() {
		return isAttackBuffering;
	}

	public void setAttackBuffering(boolean isAttackBuffering) {
		this.isAttackBuffering = isAttackBuffering;
	}

	public Animation<TextureRegion> getUpAnimation() {
		return upAnimation;
	}

	public void setUpAnimation(Animation<TextureRegion> upAnimation) {
		this.upAnimation = upAnimation;
	}

	public Animation<TextureRegion> getLeftAnimation() {
		return leftAnimation;
	}

	public void setLeftAnimation(Animation<TextureRegion> leftAnimation) {
		this.leftAnimation = leftAnimation;
	}

	public Animation<TextureRegion> getDownAnimation() {
		return downAnimation;
	}

	public void setDownAnimation(Animation<TextureRegion> downAnimation) {
		this.downAnimation = downAnimation;
	}

	public Animation<TextureRegion> getRightAnimation() {
		return rightAnimation;
	}

	public void setRightAnimation(Animation<TextureRegion> rightAnimation) {
		this.rightAnimation = rightAnimation;
	}

	public Animation<TextureRegion> getDeadAnimation() {
		return deadAnimation;
	}

	public void setDeadAnimation(Animation<TextureRegion> deadAnimation) {
		this.deadAnimation = deadAnimation;
	}

	public Animation<TextureRegion> getLeftAttackAnimation() {
		return leftAttackAnimation;
	}

	public void setLeftAttackAnimation(Animation<TextureRegion> leftAttackAnimation) {
		this.leftAttackAnimation = leftAttackAnimation;
	}

	public Animation<TextureRegion> getDownAttackAnimation() {
		return downAttackAnimation;
	}

	public void setDownAttackAnimation(Animation<TextureRegion> downAttackAnimation) {
		this.downAttackAnimation = downAttackAnimation;
	}

	public Animation<TextureRegion> getRightAttackAnimation() {
		return rightAttackAnimation;
	}

	public void setRightAttackAnimation(Animation<TextureRegion> rightAttackAnimation) {
		this.rightAttackAnimation = rightAttackAnimation;
	}

	public Animation<TextureRegion> getUpAttackAnimation() {
		return upAttackAnimation;
	}

	public void setUpAttackAnimation(Animation<TextureRegion> upAttackAnimation) {
		this.upAttackAnimation = upAttackAnimation;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public boolean isAnimationLooping() {
		return isAnimationLooping;
	}

	public void setAnimationLooping(boolean isAnimationLooping) {
		this.isAnimationLooping = isAnimationLooping;
	}

	public TextureRegion[] getUpFrames() {
		return upFrames;
	}

	public void setUpFrames(TextureRegion[] upFrames) {
		this.upFrames = upFrames;
	}

	public TextureRegion[] getLeftFrames() {
		return leftFrames;
	}

	public void setLeftFrames(TextureRegion[] leftFrames) {
		this.leftFrames = leftFrames;
	}

	public TextureRegion[] getDownFrames() {
		return downFrames;
	}

	public void setDownFrames(TextureRegion[] downFrames) {
		this.downFrames = downFrames;
	}

	public TextureRegion[] getRightFrames() {
		return rightFrames;
	}

	public void setRightFrames(TextureRegion[] rightFrames) {
		this.rightFrames = rightFrames;
	}

	public TextureRegion[] getUpAttackFrames() {
		return upAttackFrames;
	}

	public void setUpAttackFrames(TextureRegion[] upAttackFrames) {
		this.upAttackFrames = upAttackFrames;
	}

	public TextureRegion[] getLeftAttackFrames() {
		return leftAttackFrames;
	}

	public void setLeftAttackFrames(TextureRegion[] leftAttackFrames) {
		this.leftAttackFrames = leftAttackFrames;
	}

	public TextureRegion[] getDownAttackFrames() {
		return downAttackFrames;
	}

	public void setDownAttackFrames(TextureRegion[] downAttackFrames) {
		this.downAttackFrames = downAttackFrames;
	}

	public TextureRegion[] getRightAttackFrames() {
		return rightAttackFrames;
	}

	public void setRightAttackFrames(TextureRegion[] rightAttackFrames) {
		this.rightAttackFrames = rightAttackFrames;
	}
}
