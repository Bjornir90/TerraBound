package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public abstract class Entity {
    protected Vector acceleration, speed, position;
    private HashMap<Integer, Vector> centerOfSides;
    private float scale, scaledWidth, scaledHeight;
    protected Image sprite;
    public static int TOP = 0, BOTTOM = 1, LEFT = 2, RIGHT = 3, NODIRECTION = 4, COLLISION_TOLERANCE = 5;
    protected boolean debug = false;
    protected float mass, angle;
    protected Graphics g;
    protected int[] collisionSides;
    private ArrayList<Vector> futureBoundaries;
    private int timeSinceLastPrint = 0;


    public Entity(float scale, float mass) {
        this.mass = mass;
        acceleration = new Vector(Game.GRAVITY.multiplyScalar(mass));
        speed = new Vector();
        position = new Vector();
        futureBoundaries = new ArrayList<>();
        this.scale = scale;
        this.centerOfSides = calculateCentersOfSides();
        collisionSides = new int[4];
        angle = 0.0f;
        for(int i = 0; i<4; i++){
            collisionSides[i] = 0;
        }
    }

    private HashMap<Integer, Vector> calculateCentersOfSides(){
        HashMap<Integer, Vector> centersOfSides = new HashMap<>();
        centersOfSides.put(TOP, new Vector(scaledWidth/2, 0));
        centersOfSides.put(LEFT, new Vector(0, scaledHeight/2));
        centersOfSides.put(BOTTOM, new Vector(scaledWidth/2, scaledHeight));
        centersOfSides.put(RIGHT, new Vector(scaledWidth, scaledHeight/2));
        return centersOfSides;
    }

    protected Vector calculateFutureCoords(int delta){
        Vector futureCoords = new Vector(position);
        futureCoords = handleMovement(delta, futureCoords);
        correctCoordinates(futureCoords);
        return futureCoords;
    }

    protected void correctCoordinates(Vector coords){
        if(coords.getX() < 0){
            coords.setX(1);
        } else if(coords.getX() > MapUtils.getMapWidth()){
            coords.setX(MapUtils.getTileWidth()-1);
        }
        if(coords.getY() < 0){
            coords.setY(1);
        } else if(coords.getY() > MapUtils.getMapHeight()){
            coords.setY(MapUtils.getTileHeight()-1);
        }
    }

    private Vector handleMovement(int delta, Vector originPosition){
        return originPosition.addVector(speed.multiplyScalar(delta));
    }

    public void drawBounds(){
        g.setColor(Color.cyan);
        for(Vector p : futureBoundaries){
            g.fillRect(p.getX(), p.getY(), 1, 1);
        }
    }

    protected void calculateFutureBounds(int delta){
        //Vector position is top left of sprite
        //Create an ArrayList of the coordinates of each pixels on the boundaries
        ArrayList<Vector> pixelsOnBoundaries = new ArrayList<>();
        Vector bottomLeft = new Vector(position);
        bottomLeft.addY(scaledHeight);
        Vector topRight = new Vector(position);
        topRight.addX(scaledWidth);
        Vector center = calculateCenter();

        //Get the sign of the speed for the two axis
        //int speedXSign = (speed.getX() >= 0)?1:-1, speedYSign = (speed.getY() >= 0)?1:-1;


        //We check the absolute value of y against the absolute value of the speed
        //We increment or decrement y, depending on the direction of the speed : if the speed is negative, we want to go towards the negative and vice-versa
        //for(float y = 0; Math.abs(y)<=Math.abs(speed.multiplyScalar(delta).getY()); y+=speedYSign) {
            for (int x = COLLISION_TOLERANCE; x <= scaledWidth-COLLISION_TOLERANCE; x++) {
                Vector pixelOnTopBoundary = new Vector(position);
                pixelOnTopBoundary.addX(x);
                pixelOnTopBoundary.rotateSelf(center, angle);

                Vector pixelOnBottomBoundary = new Vector(bottomLeft);
                pixelOnBottomBoundary.addX(x);
                pixelOnBottomBoundary.rotateSelf(center, angle);

                pixelsOnBoundaries.add(pixelOnTopBoundary);
                pixelsOnBoundaries.add(pixelOnBottomBoundary);
            }
        //}

        //Same thing, same reasons
        //for(float x = 0; Math.abs(x)<=Math.abs(speed.multiplyScalar(delta).getX()); x+=speedXSign) {
            for (int y = COLLISION_TOLERANCE; y <= scaledHeight-COLLISION_TOLERANCE; y++) {
                Vector pixelOnLeftBoundary = new Vector(position);
                pixelOnLeftBoundary.addY(y);
                pixelOnLeftBoundary.rotateSelf(center, angle);

                Vector pixelOnRightBoundary = new Vector(topRight);
                pixelOnRightBoundary.addY(y);
                pixelOnRightBoundary.rotateSelf(center, angle);

                pixelsOnBoundaries.add(pixelOnLeftBoundary);
                pixelsOnBoundaries.add(pixelOnRightBoundary);
            }
      //  }

        pixelsOnBoundaries.forEach(vector -> {
            //handleMovement(delta, vector);
            correctCoordinates(vector);
        });
        futureBoundaries = pixelsOnBoundaries;
    }

    public void update(int delta){
        speed.addSelfVector(acceleration.multiplyScalar(delta));
        if(speed.getY() > Game.MAX_SPEED)
            speed.setY(Game.MAX_SPEED);
        onUpdate(delta);
        calculateFutureBounds(delta);
        int [] oldCollisionSides = collisionSides.clone();
        for (int i = 0; i < 4; i++) {
            collisionSides[i] = 0;//Reset on every direction
        }
        for(Vector futurePixel : futureBoundaries){
            if(futurePixel.getX() < 0 || futurePixel.getX() >= MapUtils.getMapWidth()){
                speed.setX(0);
                acceleration.setX(0);
            }
            if(futurePixel.getY() < 0 || futurePixel.getY() >= MapUtils.getMapHeight()){
                speed.setY(0);
                acceleration.setY(0);
            }

            if(MapUtils.collidesWithTerrain(futurePixel)){
                //Won't work at very high speed, where the speed on an axis per update is higher than half the size of the player on this axis
                //Remove the corners, because they detect a collision on the wrong sides
                if(collisionSides[TOP] != 1 && futurePixel.getY() == position.getY() && futurePixel.getX() > position.getX()+COLLISION_TOLERANCE && futurePixel.getX() < position.getX()+scaledWidth-COLLISION_TOLERANCE){
                    collisionSides[TOP] = 1;
                } else if(collisionSides[BOTTOM] != 1 && futurePixel.getY() == position.getY()+scaledHeight && futurePixel.getX() > position.getX()+COLLISION_TOLERANCE && futurePixel.getX() < position.getX()+scaledWidth-COLLISION_TOLERANCE){
                    collisionSides[BOTTOM] = 1;
                } else if(collisionSides[LEFT] != 1 && futurePixel.getX() == position.getX() && futurePixel.getY() > position.getY()+COLLISION_TOLERANCE && futurePixel.getY() < position.getY()+scaledHeight-COLLISION_TOLERANCE){
                    collisionSides[LEFT] = 1;
                } else if(collisionSides[RIGHT] != 1 && futurePixel.getX() == position.getX()+scaledWidth && futurePixel.getY() > position.getY()+COLLISION_TOLERANCE && futurePixel.getY() < position.getY()+scaledHeight-COLLISION_TOLERANCE) {
                    collisionSides[RIGHT] = 1;
                }

            }
        }
        for(int i = 0; i<4; i++){
            if(oldCollisionSides[i] != collisionSides[i]) {
                onCollisionSideChange(i, collisionSides[i] == 1);
                break;//only one collision per tick
            }
            if(collisionSides[i] == 1){
                onTerrainCollision(i);//Not called for every pixels that collides with the terrain
            }
        }
        //We calculate the futurecoords again to take into account the collisions detected above
        Vector updatedFutureCoords = calculateFutureCoords(delta);
        position = updatedFutureCoords;
    }

    /**
     * Is called by update() method in Entity when a collision is detected with the terrain
     * Allow for different behaviors when colliding with the terrain
     */
    public abstract void onTerrainCollision(int side);

    public abstract void onUpdate(int delta);

    public abstract void onCollisionSideChange(int newSide, boolean colliding);

    protected Vector calculateCenter(){
        Vector center = new Vector(position);
        center.addX(scaledWidth/2);
        center.addY(scaledHeight/2);
        return center;
    }


    public Vector getSpeed() {
        return speed;
    }

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

    public Vector getPosition() {
        return position;
    }

    public float getScaledWidth() {
        return scaledWidth;
    }

    public float getScaledHeight() {
        return scaledHeight;
    }

    public void setG(Graphics g) {
        this.g = g;
    }

    protected void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    protected void setSpeed(Vector vector){
        speed = vector;
    }

    public Vector getAcceleration() {//TODO delete
        return acceleration;
    }

    public void rotateSprite(float angle){
        this.sprite.rotate(-angle);
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void loadSprite(String spritePath) throws SlickException {
        sprite = new Image(spritePath);
        scaledWidth = sprite.getWidth()*scale;
        scaledHeight = sprite.getHeight()*scale;
        sprite.setCenterOfRotation(position.getX()+scaledWidth/2, position.getY()+scaledHeight/2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        if (speed != null ? !speed.equals(entity.speed) : entity.speed != null) return false;
        return position != null ? position.equals(entity.position) : entity.position == null;
    }

    @Override
    public int hashCode() {
        int result = speed != null ? speed.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }
}
