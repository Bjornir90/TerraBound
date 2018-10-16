package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.RayCaster;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public abstract class Movable {
    protected Vector acceleration, speed, position;
    private HashMap<Integer, Vector> centerOfSides;
    private float scale, scaledWidth, scaledHeight;
    private Image sprite;
    public static int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3;
    protected boolean debug = false;
    private Graphics g;


    public Movable(String spritePath, float scale) throws SlickException {
        sprite = new Image(spritePath);
        acceleration = new Vector(Game.GRAVITY);
        speed = new Vector();
        position = new Vector();
        this.scale = scale;
        scaledWidth = sprite.getWidth()*scale;
        scaledHeight = sprite.getHeight()*scale;
        this.centerOfSides = calculateCentersOfSides();
    }

    private HashMap<Integer, Vector> calculateCentersOfSides(){
        float width = sprite.getWidth()*scale;
        float height = sprite.getHeight()*scale;
        HashMap<Integer, Vector> centersOfSides = new HashMap<>();
        centersOfSides.put(TOP, new Vector(width/2, 0));
        centersOfSides.put(LEFT, new Vector(0, height/2));
        centersOfSides.put(BOTTOM, new Vector(width/2, height));
        centersOfSides.put(RIGHT, new Vector(width, height/2));
        return centersOfSides;
    }

    protected Vector calculateFutureCoords(int delta){
        Vector futureCoords = new Vector();
        futureCoords.setX(position.getX());
        futureCoords.setY(position.getY());
        futureCoords.addSelfVector(speed.multiplyScalar(delta));
        return futureCoords;
    }

    public Vector update(int delta){
        Vector futureCoords = calculateFutureCoords(delta);
        for(int side = 0; side<4; side++){
	        Vector center = centerOfSides.get(side);
	        Vector futureCenter = center.addVector(futureCoords);
	        if(futureCoords.getX() < 0 || futureCoords.getX() >= MapUtils.getMapWidth()){
	        	speed.setX(0);
	        }
	        if(futureCoords.getY() < 0 || futureCoords.getY() >= MapUtils.getMapHeight()){
	        	speed.setY(0);
	        }
	        if (debug) {
		        RayCaster.prepareRayDraw(scaledWidth, scaledHeight, center.addVector(position), side);
	        }
	        if(MapUtils.collidesWithTerrain(futureCenter.getX(), futureCenter.getY())){
		        if(RayCaster.raycastTerrain(scaledWidth, scaledHeight, center.addVector(position), side)){
			        onTerrainCollision(side);
		        }
	        }
        }
        //We calculate the futurecoords again to take into account the collisions detected above
        Vector updatedFutureCoords = calculateFutureCoords(delta);
        position = updatedFutureCoords;
        //Immutable speed
        Vector newSpeed = speed.addVector(acceleration.multiplyScalar(delta));
        System.out.println("newSpeed = " + newSpeed);
        //Apply gravity to object
        System.out.println("acceleration.getY() = " + acceleration.getY());
        acceleration = Game.GRAVITY.addVector(acceleration.getXProjection());
        //Friction, to bring the character to a stop
        newSpeed.setX(newSpeed.getX()*0.7f);
        //Limit objects speed
        if(Math.abs(newSpeed.getX()) <= Game.MAX_SPEED){
            //Set speed to 0 if close enough (rounding error)
            if(Math.abs(newSpeed.getX()) < 0.0001f)
                newSpeed.setX(0);
            speed = newSpeed;
        } else if(newSpeed.getX() > 0){
            speed = new Vector(Game.MAX_SPEED, newSpeed.getY());
        } else if(newSpeed.getX() < 0){
            speed = new Vector(-Game.MAX_SPEED, newSpeed.getY());
        }
        return speed;
    }

    /**
     * Is called by update() method in Movable when a collision is detected with the terrain
     * Allow for different behaviors when colliding with the terrain
     */
    public abstract void onTerrainCollision(int side);

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public void draw(){
        sprite.draw(position.getX(), position.getY(), scale);
    }

    public void setX(float x) {
        this.position.setX(x);
    }

    public void setY(float y) {
        this.position.setY(y);
    }

    public Vector getPosition() {
        return position;
    }

    public float getScaledWidth() {
        return scaledWidth;
    }

    public float getScaledHeight() {
        return scaledHeight;
    }

	public void setG(Graphics g) {
		this.g = g;
	}

    protected void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    protected void setSpeed(Vector vector){
        speed = vector;
    }

    public Vector getAcceleration() {//TODO delete
        return acceleration;
    }
}
