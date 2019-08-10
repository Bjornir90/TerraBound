package com.bjornir.terrabound.utils;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.entities.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

public class Arena {

    /**
     * Check every pixels of a sprite for a collision with the terrain
     * @param entity the entity to check collision for
     * @return null if there is not a collision with the terrain, the position of the pixel that collides otherwise
     */
    private static Vector checkForCollision(Entity entity, TiledMap map){

        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();

        if(entity.getX() >= map.getWidth()*tileWidth || entity.getX() < 0 || entity.getY() >= map.getHeight()*tileHeight || entity.getY() < 0)
            return null;

        ArrayList<Vector> collisionPoints = new ArrayList<>();

        for(int x = (int)entity.getLeftBound(); x < entity.getRightBound(); x++){
            for(int y = (int)entity.getTopBound(); y < entity.getBottomBound(); y++){

                Image tile = map.getTileImage(x / tileWidth, y / tileHeight, 0);

                if(tile != null) {
                    collisionPoints.add(new Vector(x, y));
                }
            }
        }

        if(collisionPoints.isEmpty())
            return null;

        return Vector.getMeanPosition(collisionPoints);
    }

    public static Vector Collides(Entity entity, TiledMap map){
        return checkForCollision(entity, map);
    }
}
