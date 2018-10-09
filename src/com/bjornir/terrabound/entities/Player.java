package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
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
            this.speed.setY(0);
        }else if(side == Movable.TOP) {
            this.speed.setY(0);
        } else if(side == Movable.LEFT || side == Movable.RIGHT){
            this.speed.setX(0);
        }
	    System.out.println("Collision with terrain detected : side = " + side);
    }


    @Override
    public void keyPressed(int i, char c) {
        if(i == Input.KEY_D){
            this.setSpeed(new Vector(Game.ACCELERATION, speed.getY()));
            friction = false;
        } else if(i == Input.KEY_A){
            this.setSpeed(new Vector(-Game.ACCELERATION, speed.getY()));
            friction = false;
        } else if(i == Input.KEY_SPACE && onPlatform){
            this.setSpeed(new Vector(speed.getX(), -1.3f));
            onPlatform = false;
        }

    }

    @Override
    public void keyReleased(int i, char c) {
        if(i == Input.KEY_D){
            this.setSpeed(new Vector(0, speed.getY()));
            friction = true;
        } else if(i == Input.KEY_A){
            this.setSpeed(new Vector(0, speed.getY()));
            friction = true;
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
