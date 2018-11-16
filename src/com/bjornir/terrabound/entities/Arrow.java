package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.SlickException;

public class Arrow extends Movable {

	float angle;

	public Arrow(String spritePath, float scale) throws SlickException {
		super(spritePath, scale, 0.2f);
		angle = 0.0f;
	}

	@Override
	public void onTerrainCollision(int side) {
		speed = new Vector(0,0);
		acceleration = new Vector(0,0);
	}

	@Override
	public void onUpdate(int delta) {
		float oldAngle = this.angle;
		this.angle = this.speed.getAngle();
		float angleIncrement = this.angle-oldAngle;
		if(!speed.isNullVector())
			rotateSprite(-angleIncrement);
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
}
