package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.Side;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

public class Player extends Entity implements KeyListener, MouseListener {

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
                speed.setX(0);
                break;
            case Input.KEY_Q:
                currentMovingState = HorizontalDirection.LEFT;
                speed.setX(0);
                break;
            case Input.KEY_SPACE:
                this.speed.setY(-1.8f);
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
        input.addMouseListener(this);
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

    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {
        Arrow arrow = new Arrow("sprites/Arrow.png", 32, 32);
        Vector toMouse = new Vector(i1, i2).addVector(position.negateVector());

        toMouse.normalizeSelf();
        arrow.speed = toMouse.multiplyScalar(2.5f);
        arrow.position = new Vector(this.position);
        Game.getInstance().entities.add(arrow);
    }

    @Override
    public void mousePressed(int i, int i1, int i2) {

    }

    @Override
    public void mouseReleased(int i, int i1, int i2) {

    }

    @Override
    public void mouseMoved(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mouseDragged(int i, int i1, int i2, int i3) {

    }
}


enum HorizontalDirection{
    LEFT,
    NOTMOVING,
    RIGHT
}