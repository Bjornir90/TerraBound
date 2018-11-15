package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

public class Player extends Movable implements KeyListener {

    private boolean onPlatform;

    public Player(String spritePath, float scale) throws SlickException {
        super(spritePath, scale);
        onPlatform = false;
    }

    @Override
    public void onTerrainCollision(int side) {
        if(side == Movable.BOTTOM){
            onPlatform = true;
            if(this.speed.getY()>0)
                this.speed.setY(0);
            if(this.acceleration.getY()>0)
                this.acceleration.setY(0);
            //get out of the terrain tile
            this.position.addY(-this.position.getY()%MapUtils.getTileHeight());
        }else if(side == Movable.TOP) {
            if(this.speed.getY()<0)
                this.speed.setY(0);
            if(this.acceleration.getY()<0)
                this.acceleration.setY(0);
            //get out of the terrain tile
            this.position.addY(MapUtils.getTileHeight()-this.position.getY()%MapUtils.getTileHeight());
        } else if(side == Movable.LEFT || side == Movable.RIGHT){
            this.speed.setX(0);
            this.acceleration.setX(0);
        }
    }

    @Override
    public void onUpdate(int delta) {
        Vector newSpeed = new Vector(speed);
        //Apply gravity to player
        acceleration = Game.GRAVITY.addVector(acceleration.getXProjection());
        //Friction, to bring the character to a stop
        newSpeed.setX(newSpeed.getX()/(delta*0.08f));
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
    }


    @Override
    public void keyPressed(int i, char c) {
        System.out.println("Key pressed : " + c);
        switch(i){
            case Input.KEY_D:
                this.setAcceleration(new Vector(Game.ACCELERATION, acceleration.getY()));
                break;
            case Input.KEY_A:
                this.setAcceleration(new Vector(-Game.ACCELERATION, acceleration.getY()));
                break;
            case Input.KEY_SPACE:
                if(onPlatform) {
                    this.setAcceleration(new Vector(acceleration.getX(), -0.08f));
                    onPlatform = false;
                }
                break;
        }

    }

    @Override
    public void keyReleased(int i, char c) {
        System.out.println("Key released : " + c);
        if(i == Input.KEY_D || i == Input.KEY_A){
            this.setAcceleration(new Vector(0, acceleration.getY()));
        }
    }

    @Override
    public void setInput(Input input) {
        input.addKeyListener(this);
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }



}
