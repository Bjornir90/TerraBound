package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.Vector;

public class Arrow extends Entity {

	private float angle;
	private long networkID;

	public static Arrow createFromRemote(String data){
		Arrow a =  new Arrow(1);
		a.updateFromRemote(data);
		return a;
	}

	public Arrow(float scale) {
		super(scale, 0.2f);
		angle = 0.0f;
		generateNetworkID();
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


	/**
	 * Returns a String that contains all necessary data to draw the arrow on the other computer.
	 * The String is formatted to be used by the method @see updateFromRemote
	 * @return a specially formatted string
	 */
	public String formatForSending(){
		return position.getX()+":"+position.getY()+":"+speed.getX()+":"+speed.getY()+":"+angle;
	}

	/**
	 * Update this arrow attributes with data contained in the parameter.
	 * The string must be formatted by @see formatForSending
	 * @param data a specially formatted string containing the data relevant for drawing the arrow
	 */
	public void updateFromRemote(String data){
		final int expectedNumberOfData = 5;
		int currentEndIndex, currentDataIndex = 0;
		float[] dataNumbers = new float[expectedNumberOfData];
		for(; currentDataIndex<expectedNumberOfData; currentDataIndex++){
			currentEndIndex = data.indexOf(':');
			if(currentEndIndex == -1) {
				dataNumbers[currentDataIndex] = Float.parseFloat(data);
				break;
			}
			dataNumbers[currentDataIndex] = Float.parseFloat(data.substring(0, currentEndIndex));
			data = data.substring(currentEndIndex+1);
		}
		position.setX(dataNumbers[0]);
		position.setY(dataNumbers[1]);
		speed.setX(dataNumbers[2]);
		speed.setY(dataNumbers[3]);
		setAngle(dataNumbers[4]);
	}


	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}

	public long getNetworkID() {
		return networkID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Arrow arrow = (Arrow) o;

		return Float.compare(arrow.angle, angle) == 0;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (angle != +0.0f ? Float.floatToIntBits(angle) : 0);
		return result;
	}

	private void generateNetworkID(){//Only collision possible : arrows spawned at the same time, at the same place, with the same direction
		networkID = hashCode()*System.nanoTime();
	}
}
