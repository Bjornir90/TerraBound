package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.utils.Arena;
import com.bjornir.terrabound.utils.Side;
import com.bjornir.terrabound.utils.Sprite;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public abstract class Entity {
    //Center of the sprite
    protected Vector position, speed;
    protected Sprite sprite;
    protected int height, width;
    protected float scale, angle;
    protected boolean isPhysical;

    public Entity(String spritePath, int width, int height){
        try {
            sprite = new Sprite(spritePath);
        } catch (SlickException e) {
            System.err.println("Could not load sprite "+spritePath);
            e.printStackTrace();
        }

        this.width = width;
        this.height = height;

        position = new Vector();
        speed = new Vector();
        isPhysical = true;
        angle = 0.0f;

        setScale(1.0f);

        sprite.setCenterOfRotation(width/2.0f, height/2.0f);
        sprite.setRotation(angle);
    }

    public Entity(Entity other){
        position = new Vector(other.position);
        speed = new Vector(other.speed);
        sprite = other.sprite;
        height = other.height;
        width = other.width;
        scale = other.scale;
        isPhysical = other.isPhysical;
        angle = other.angle;
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
        sprite.drawWithShader(position.getX(), position.getY());
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
        sprite.rotate(oldAngle-angle);
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

}
