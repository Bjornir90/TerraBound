package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.SlickException;

import static org.junit.jupiter.api.Assertions.*;

class ArrowTest {

	@org.junit.jupiter.api.Test
	void formatForSending() throws SlickException {
		Arrow a = new Arrow(1);
		a.setSpeed(new Vector(1, 2));
		a.setPosition(new Vector(25, 33));
		a.setAngle(5);
		String expected = "25.0:33.0:1.0:2.0:5.0";
		assertEquals(expected, a.formatForSending());
		a.setAngle(3.5f);
		a.setPosition(new Vector(35.2f, 43.9f));
		expected = "35.2:43.9:1.0:2.0:3.5";
		assertEquals(expected, a.formatForSending());
	}

	@org.junit.jupiter.api.Test
	void updateFromRemote() throws SlickException {
		Arrow original = new Arrow(1);
		original.setPosition(new Vector(4, 12));
		original.setSpeed(new Vector(0.2f, 0.8f));
		original.setAngle(34.8f);
		String data = original.formatForSending();
		Arrow copy = new Arrow(1);
		copy.updateFromRemote(data);
		assertEquals(original, copy);
	}

	@org.junit.jupiter.api.Test
	void createFromRemote(){
		Arrow original = new Arrow(1);
		original.setPosition(new Vector(4.9f, 124.8f));
		original.setSpeed(new Vector(0.2f, 0.8f));
		original.setAngle(34.8f);
		String data = original.formatForSending();
		Arrow copy = Arrow.createFromRemote(data);
		assertEquals(original, copy);
	}
}