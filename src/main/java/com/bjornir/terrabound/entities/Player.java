package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.Side;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public class Player extends Entity implements KeyListener {

    public static final float MAX_HORIZONTAL_SPEED = 1.0f, HORIZONTAL_ACCELERATION = 0.2f;

    private HorizontalDirection currentMovingState;

    public Player(String spritePath, int width, int height) {
        super(spritePath, width, height);
        currentMovingState = HorizontalDirection.NOTMOVING;
    }

    @Override
    public void update(int delta){
        super.update(delta);

        switch(currentMovingState){
            case LEFT:
                speed.addX(-HORIZONTAL_ACCELERATION);
                if(speed.getX()<-MAX_HORIZONTAL_SPEED)
                    speed.setX(-MAX_HORIZONTAL_SPEED);
                break;

            case RIGHT:
                speed.addX(HORIZONTAL_ACCELERATION);
                if(speed.getX()>MAX_HORIZONTAL_SPEED)
                    speed.setX(MAX_HORIZONTAL_SPEED);
                break;

            case NOTMOVING:

                if(speed.getX() < 0.0f) {

                    speed.addX(HORIZONTAL_ACCELERATION);
                    if(speed.getX() > 0.0f)
                        speed.setX(0.0f);

                } else if(speed.getX() > 0.0f) {

                    speed.addX(-HORIZONTAL_ACCELERATION);
                    if(speed.getX() < 0.0f)
                        speed.setX(0.0f);

                }
                break;

        }

    }

    @Override
    protected void onTerrainCollision(Side side) {

    }

    @Override
    public void keyPressed(int i, char c) {
        switch(i){
            case Input.KEY_D:
                currentMovingState = HorizontalDirection.RIGHT;
                speed = new Vector();
                break;
            case Input.KEY_Q:
                currentMovingState = HorizontalDirection.LEFT;
                speed = new Vector();
                break;
        }
    }

    @Override
    public void keyReleased(int i, char c) {
        switch(i){
            case Input.KEY_D:
                if(currentMovingState == HorizontalDirection.RIGHT)
                    currentMovingState = HorizontalDirection.NOTMOVING;
                break;
            case Input.KEY_Q:
                if(currentMovingState == HorizontalDirection.LEFT)
                    currentMovingState = HorizontalDirection.NOTMOVING;
                break;
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


enum HorizontalDirection{
    LEFT,
    NOTMOVING,
    RIGHT
}