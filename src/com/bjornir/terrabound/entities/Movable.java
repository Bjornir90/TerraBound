package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Movable {
    protected float x, y;
    protected Vector speed;
    private Image sprite;

    public Movable(String spritePath) throws SlickException {
        sprite = new Image(spritePath);
        speed = new Vector();
    }

    public void setSpeed(Vector vector){
        speed = vector;
    }

    public void update(int delta){
        x += speed.getX()*delta;
        y += speed.getY()*delta;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(){
        sprite.drawCentered(x, y);
    }
}
