package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.ArrowsList;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.*;

public class Player extends Movable implements KeyListener, MouseListener {

    private boolean onPlatform, dashing;
    private float timeSinceDashBeginning;
    private final float timeOfDash = 50, hookLength = 350;
    private float[] mouseCoords;

    public Player(float scale) throws SlickException {
        super(scale, 1.0f);
        onPlatform = false;
        dashing = false;
        timeSinceDashBeginning = 0;
        mouseCoords = new float[2];
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
        } else if(side == Movable.LEFT){
            if(this.speed.getX()<0)
                this.speed.setX(0);
            this.position.addX(MapUtils.getTileWidth()-this.position.getX()%MapUtils.getTileWidth());
        } else if( side == Movable.RIGHT){
            if(this.speed.getX()>0)
                this.speed.setX(0);
            this.position.addX(-this.position.getX()%MapUtils.getTileWidth());
        }
    }

    @Override
    public void onUpdate(int delta) {
        if(dashing){
            if(timeSinceDashBeginning>timeOfDash){
                dashing = false;
            }
            timeSinceDashBeginning += delta;
        }
        if(dashing){
            return;
        }
        Vector newSpeed = new Vector(speed);
        //Apply gravity to player
        acceleration = Game.GRAVITY.addVector(acceleration.getXProjection());
        //Friction, to bring the character to a stop
        float friction = delta*0.08f;
        //Delta too low might cause friction to get under 1 => it would accelerate infinitely
        friction = (friction<1)?1.005f:friction;
        newSpeed.setX(newSpeed.getX()/((friction)));
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
            case Input.KEY_F:
                this.position.addY(-200);
                speed = new Vector(0, 0);
                break;
            case Input.KEY_E:
                this.acceleration = new Vector(0, 0);
                this.speed = new Vector(4, 0);
                dashing = true;
                timeSinceDashBeginning = 0;
                break;
            case Input.KEY_Q:
                this.acceleration = new Vector(0, 0);
                this.speed = new Vector(-4, 0);
                dashing = true;
                timeSinceDashBeginning = 0;
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


    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mousePressed(int i, int i1, int i2) {
        Vector mouseDirection = this.position.negateVector().addVector(new Vector(i1, i2));
        if(i == Input.MOUSE_LEFT_BUTTON){
            ArrowsList list = ArrowsList.getInstance();
            try {
                Arrow a = new Arrow(1);
                a.loadSprite("sprites/Arrow.png");
                a.setX(this.getX());
                a.setY(this.getY());
                float angle = mouseDirection.getAngle();
                System.out.println("angle = " + angle);
                a.setAngle(angle);
                a.rotateSprite(-angle);
                mouseDirection.normalizeSelf();
                //arrowDirection.multiplySelfScalar(1.5f);
                a.setSpeed(mouseDirection);
                list.addLocal(a);
            } catch (SlickException e) {
                e.printStackTrace();
                System.err.println("Could not instanciate Arrow : files are probably missing or corrupted");
            }
        } else if (i == Input.MOUSE_RIGHT_BUTTON){
            mouseDirection.normalizeSelf();
            System.out.println("mouseDirection = " + mouseDirection);
            Vector currentlyCheckedPixel = new Vector(position);
            for(int j = 0; j<hookLength; j++){
                currentlyCheckedPixel.addSelfVector(mouseDirection);
                if(MapUtils.collidesWithTerrain(currentlyCheckedPixel)){
                    Arrow a = null;
                    try {
                        a = new Arrow(1);
                        a.loadSprite("sprites/Arrow.png");
                    } catch (SlickException e) {
                        e.printStackTrace();
                        System.err.println("Could not instanciate Arrow : files are probably missing or corrupted");
                    }
                    a.setPosition(currentlyCheckedPixel);
                    a.setSpeed(new Vector(0,0));
                    a.setAngle(mouseDirection.getAngle());
                    a.rotateSprite(-a.getAngle());
                    ArrowsList la = ArrowsList.getInstance();
                    la.addLocal(a);
                    break;
                }
            }
        }
    }

    @Override
    public void mouseReleased(int i, int i1, int i2) {

    }

    @Override
    public void mouseMoved(int i, int i1, int i2, int i3) {
        mouseCoords[0] = i2;
        mouseCoords[1] = i3;
    }

    @Override
    public void mouseDragged(int i, int i1, int i2, int i3) {

    }

    public Vector mouseVector(){
        Vector mousePos = new Vector(mouseCoords[0], mouseCoords[1]);
        Vector fromPlayerToMouse = position.negateVector().addVector(mousePos);
        return fromPlayerToMouse;
    }
}
