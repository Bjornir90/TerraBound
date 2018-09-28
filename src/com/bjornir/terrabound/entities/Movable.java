package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.RayCaster;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public abstract class Movable {
    protected Vector speed, position;
    private HashMap<Integer, Vector> centerOfSides;
    private float scale, scaledWidth, scaledHeight;
    private Image sprite;
    public static int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3;

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
        for(Vector center : centerOfSides.values()){
            Vector futureCenter = center.addVector(futureCoords);
            if(MapUtils.collidesWithTerrain(futureCenter.getX(), futureCenter.getY())){
                for(int side = 0; side<4; side++){
                    if(RayCaster.raycastTerrain(scaledWidth, scaledHeight, center, side)){
                        onTerrainCollision(side);
                        return;
                    }
                }
            }
        }
        position = futureCoords;
        speed.setY(speed.getY()+0.001f);
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
}
