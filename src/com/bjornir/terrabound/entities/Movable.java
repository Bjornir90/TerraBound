package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Movable {
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
        futureCoords.addVector(speed);
        return futureCoords;
    }

    public void update(int delta){
        position = calculateFutureCoords(delta);
        speed.setY(speed.getY() + Game.GRAVITY*delta);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(){
        sprite.draw(x, y, scale);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
