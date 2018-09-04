package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

public class Player extends Movable implements KeyListener {

    public Player(String spritePath) throws SlickException {
        super(spritePath);
    }


    @Override
    public void keyPressed(int i, char c) {
        if(i == Input.KEY_D){
            this.setSpeed(new Vector(speed.getX()+Game.ACCELERATION, speed.getY()));
        } else if(i == Input.KEY_A){
            this.setSpeed(new Vector(speed.getX()-Game.ACCELERATION, speed.getY()));
        }

    }

    @Override
    public void keyReleased(int i, char c) {
        if(i == Input.KEY_D){
            this.setSpeed(new Vector(speed.getX()-Game.ACCELERATION, speed.getY()));
        } else if(i == Input.KEY_A){
            this.setSpeed(new Vector(speed.getX()+Game.ACCELERATION, speed.getY()));
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
