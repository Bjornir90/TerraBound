package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Movable {
    protected Vector speed, position;
    private float scale;
    private Image sprite;

    public Movable(String spritePath, float scale) throws SlickException {
        sprite = new Image(spritePath);
        speed = new Vector();
        position = new Vector();
        this.scale = scale;
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
        if(MapUtils.collidesWithTerrain(futureCoords.getX(), futureCoords.getY())){
            onTerrainCollision();
            System.out.printf("Collision detected");
        } else {
            position = futureCoords;
            speed.setY(speed.getY() + Game.GRAVITY*delta);
        }
    }

    /**
     * Is called by update() method in Movable when a collision is detected with the terrain
     * Allow for different behaviors when colliding with the terrain
     */
    public abstract void onTerrainCollision();

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
