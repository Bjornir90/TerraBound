package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public abstract class Movable {
    protected Vector speed, position;
    private HashMap<String, Vector> offsetsFromTopLeftCorner;
    private float scale;
    private Image sprite;
    public static String TOPLEFT = "tl", TOPRIGHT = "tr", BOTTOMLEFT = "bl", BOTTOMRIGHT = "br";
    public static int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3;

    private HashMap<String, Vector> calculateCorners(){
        float width = sprite.getWidth()*scale;
        float height = sprite.getHeight()*scale;
        HashMap<String, Vector> offsetsFromTopLeftCorner = new HashMap<>();
        offsetsFromTopLeftCorner.put("tl", new Vector(0, 0));
        offsetsFromTopLeftCorner.put("tr", new Vector(width, 0));
        offsetsFromTopLeftCorner.put("bl", new Vector(0, height));
        offsetsFromTopLeftCorner.put("br", new Vector(width, height));
        return offsetsFromTopLeftCorner;
    }

    public Movable(String spritePath, float scale) throws SlickException {
        sprite = new Image(spritePath);
        speed = new Vector();
        position = new Vector();
        this.scale = scale;
        this.offsetsFromTopLeftCorner = calculateCorners();
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
        for(Vector offset : offsetsFromTopLeftCorner.values()) {
            Vector futureCorner = futureCoords.addVector(offset);
            if (MapUtils.collidesWithTerrain(futureCorner.getX(), futureCorner.getY())) {
                System.out.printf("Collision detected");
                //Now we determine on which side the collision occurred
                float width = sprite.getWidth()*scale, height = sprite.getHeight()*scale;

            } else {
                position = futureCoords;
                speed.setY(speed.getY() + Game.GRAVITY * delta);
            }
        }
    }

    /**
     * Is called by update() method in Movable when a collision is detected with the terrain
     * Allow for different behaviors when colliding with the terrain
     */
    public abstract void onTerrainCollision(int side);

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

    public Vector getCornerOffset(String corner){
        return offsetsFromTopLeftCorner.get(corner);
    }
}
