package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.Arena;
import com.bjornir.terrabound.utils.Side;
import com.bjornir.terrabound.utils.Sprite;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Entity {
    //Center of the sprite
    protected Vector position, speed;
    protected Sprite sprite;
    protected int height, width;
    protected float scale, angle;
    protected boolean isPhysical, isLocal;
    protected long networkID;

    public Entity(){
        this.width = 0;
        this.height = 0;

        position = new Vector();
        speed = new Vector();
        isPhysical = true;
        isLocal = true;
        angle = 0.0f;

        setScale(1.0f);
    }

    public Entity(String spriteName, int width, int height){
        this();
        setSprite(spriteName);

        this.width = width;
        this.height = height;

        sprite.setCenterOfRotation(width/2.0f, height/2.0f);
        sprite.setRotation(angle);
    }

    public Entity(Entity other){
        position = new Vector(other.position);
        speed = new Vector(other.speed);
        sprite = other.sprite.copy();
        height = other.height;
        width = other.width;
        scale = other.scale;
        isPhysical = other.isPhysical;
        angle = other.angle;
        isLocal = other.isLocal;
    }

    private Side getClosestBound(Vector target){

        Vector distanceFromCenter = target.addVector(position.negateVector());

        if(distanceFromCenter.getX() > Math.abs(distanceFromCenter.getY()) ){
            return Side.RIGHT;
        } else if(-distanceFromCenter.getX() > Math.abs(distanceFromCenter.getY()) ){
            return Side.LEFT;
        } else if(-distanceFromCenter.getY() > Math.abs(distanceFromCenter.getX()) ){
            return Side.TOP;
        } else if(distanceFromCenter.getY() > Math.abs(distanceFromCenter.getX()) ){
            return Side.BOTTOM;
        } else {
            return Side.VOID;
        }
    }

    public void update(int delta){
        //Create a logical entity to perform calculations on, then replace current entity by this one
        //This avoids moving into walls, as the entity's position is "immutable" and won't change unless the path is clear
        Entity futureEntity = new LogicEntity(this);

        if(isPhysical)
            speed.addY(0.1f);

        if(speed.getY() > 1.0f)
            speed.setY(1.0f);

        //Move the logical entity to this entity's future position
        futureEntity.moveBy(speed.multiplyScalar(delta));

        Vector terrainCollisionPosition = Arena.Collides(futureEntity, Game.CurrentMap);

        if(terrainCollisionPosition != null){ //There is a collision with the terrain

            Side collisionSide = futureEntity.getClosestBound(terrainCollisionPosition);

            if(collisionSide != Side.VOID) {

                float tileHeight = Game.CurrentMap.getTileHeight();
                float tileWidth = Game.CurrentMap.getTileWidth();

                switch(collisionSide){
                    case TOP:
                        if(speed.getY()<0)
                            speed.setY(0.0f);

                        //Replace the entity to the closest position possible that won't intersect with the terrain
                        float distanceToGetOutOfTile = (futureEntity.position.getY() - height / 2.0f) % tileHeight;
                        futureEntity.position.addY(tileHeight - distanceToGetOutOfTile);

                        break;
                    case BOTTOM:
                        if(speed.getY()>0)
                            speed.setY(0.0f);

                        distanceToGetOutOfTile = (futureEntity.position.getY() + height / 2.0f) % tileHeight;
                        futureEntity.position.addY(-distanceToGetOutOfTile);

                        break;
                    case LEFT:
                        if(speed.getX()<0)
                            speed.setX(0.0f);

                        distanceToGetOutOfTile = (futureEntity.position.getX() - width / 2.0f) % tileWidth;
                        futureEntity.position.addX(tileWidth - distanceToGetOutOfTile);

                        break;
                    case RIGHT:
                        if(speed.getX()>0)
                            speed.setX(0.0f);

                        distanceToGetOutOfTile = (futureEntity.position.getX() + width / 2.0f) % tileWidth;
                        futureEntity.position.addX(-distanceToGetOutOfTile);

                        break;
                }

                onTerrainCollision(collisionSide);

            }

        }

        if(!position.equals(futureEntity.position))
        position = futureEntity.position;

    }

    public void draw(){
        sprite.drawWithShader(position.getX()-width/2.0f, position.getY()-height/2.0f, width, height);
    }

    public void drawBounds(Graphics g){
        ArrayList<Vector> corners = getBoundCorners();

        Vector topLeft = corners.get(0);
        Vector topRight = corners.get(1);
        Vector botLeft = corners.get(2);
        Vector botRight = corners.get(3);

        Color oldColor = g.getColor();
        g.setColor(Color.magenta);

        g.drawLine(topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY());
        g.drawLine(topLeft.getX(), topLeft.getY(), botLeft.getX(), botLeft.getY());
        g.drawLine(botRight.getX(), botRight.getY(), topRight.getX(), topRight.getY());
        g.drawLine(botLeft.getX(), botLeft.getY(), botRight.getX(), botRight.getY());

        g.setColor(oldColor);
    }

    public ArrayList<Vector> getBoundCorners(){
        ArrayList<Vector> corners = new ArrayList<>();
        Vector topLeft = position.addVector(new Vector(-width/2.0f, -height/2.0f)),
                topRight = position.addVector(new Vector(width/2.0f, -height/2.0f)),
                botLeft = position.addVector(new Vector(-width/2.0f, height/2.0f)),
                botRight = position.addVector(new Vector(width/2.0f, height/2.0f));

        topLeft.rotateSelfRelative(position, angle);
        topRight.rotateSelfRelative(position, angle);
        botLeft.rotateSelfRelative(position, angle);
        botRight.rotateSelfRelative(position, angle);

        corners.add(topLeft);
        corners.add(topRight);
        corners.add(botLeft);
        corners.add(botRight);

        return corners;
    }

    //Leave the implementation to the child classes
    protected abstract void onTerrainCollision(Side side);

    public Vector moveBy(Vector displacement){
        position.addSelfVector(displacement);
        return position;
    }

    public void setAngle(float angle){
        float oldAngle = this.angle;
        this.angle = angle;
        if(sprite != null)
            sprite.rotate(angle-oldAngle);
    }

    public void setSprite(String name){
        sprite = Sprite.LoadedSprites.get(name).copy();
    }

    public void moveTo(Vector target){
        position = new Vector(target);
    }

    public void moveTo(float x, float y){
        position = new Vector(x, y);
    }

    public float getX(){
        return position.getX();
    }

    public float getY(){
        return position.getY();
    }

    public float getLeftBound(){
        return position.getX()-(float)(width)/2.0f;
    }

    public float getRightBound(){
        return position.getX()+(float)(width)/2.0f;
    }

    public float getTopBound(){
        return position.getY()-(float)(height)/2.0f;
    }

    public float getBottomBound(){
        return position.getY()+(float)(height)/2.0f;
    }

    public Vector getPosition(){
        return position;
    }

    public float getScale(){
        return scale;
    }

    public void setScale(float scale){
        this.scale = scale;
        this.width = Math.round(width*scale);
        this.height = Math.round(height*scale);
    }

    public void setSpeed(Vector speed) {
        this.speed = speed;
    }

    protected void generateNetworkID(){//Only collision possible : arrows spawned at the same time, at the same place, with the same direction
        networkID = hashCode()*System.nanoTime();
    }

    public long getNetworkID() {
        return networkID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return height == entity.height &&
                width == entity.width &&
                Float.compare(entity.scale, scale) == 0 &&
                Float.compare(entity.angle, angle) == 0 &&
                isPhysical == entity.isPhysical &&
                Objects.equals(position, entity.position) &&
                Objects.equals(speed, entity.speed) &&
                Objects.equals(sprite, entity.sprite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, speed, sprite, height, width, scale, angle, isPhysical);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "position=" + position +
                ", speed=" + speed +
                ", scale=" + scale +
                ", angle=" + angle +
                ", isPhysical=" + isPhysical +
                '}';
    }
}
