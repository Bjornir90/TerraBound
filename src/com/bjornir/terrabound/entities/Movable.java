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
    protected Vector speed, position;
    private HashMap<Integer, Vector> centerOfSides;
    private float scale, scaledWidth, scaledHeight;
    private Image sprite;
    public static int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3;
    private boolean debug = true;
    private Graphics g;

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

    public Movable(String spritePath, float scale) throws SlickException {
        sprite = new Image(spritePath);
        speed = new Vector();
        position = new Vector();
        this.scale = scale;
        scaledWidth = sprite.getWidth()*scale;
        scaledHeight = sprite.getHeight()*scale;
        this.centerOfSides = calculateCentersOfSides();
    }

    public void setSpeed(Vector vector){
        speed = vector;
    }

    protected Vector calculateFutureCoords(int delta){
        Vector futureCoords = new Vector();
        futureCoords.setX(position.getX());
        futureCoords.setY(position.getY());
        futureCoords.addSelfVector(speed.multiplyScalar(delta));
        return futureCoords;
    }

    public void update(int delta){
        Vector futureCoords = calculateFutureCoords(delta);
        for(int side = 0; side<4; side++){
	        Vector center = centerOfSides.get(side);
	        Vector futureCenter = center.addVector(futureCoords);
	        if(futureCoords.getX() < 0 || futureCoords.getX() > MapUtils.getMapWidth()){
	        	speed.setX(0);
	        }
	        if(futureCoords.getY() < 0 || futureCoords.getY() > MapUtils.getMapHeight()){
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
        speed.setY(speed.getY()+Game.GRAVITY);
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
}
