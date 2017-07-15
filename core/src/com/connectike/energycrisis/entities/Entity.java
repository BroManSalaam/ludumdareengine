package com.connectike.energycrisis.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * anything that is in the game that is not a pure visual graphic
 *
 */
public interface Entity {

	BodyDef body = null;
	FixtureDef fixture = null;

	Sprite sprite = null;

}
