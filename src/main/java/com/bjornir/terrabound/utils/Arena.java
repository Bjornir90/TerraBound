package com.bjornir.terrabound.utils;

import com.bjornir.terrabound.entities.Entity;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

public class Arena {

    /**
     * Check every pixels of a sprite for a collision with the terrain
     * @param entity the entity to check collision for
     * @return null if there is not a collision with the terrain, the position of the pixel that collides otherwise
     */
    private static Vector checkForCollision(Entity entity, TiledMap map){
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();

        for(int x = (int)entity.getLeftBound(); x < entity.getRightBound(); x++){
            for(int y = (int)entity.getTopBound(); y < entity.getBottomBound(); y++){

                Image tile = map.getTileImage(x / tileWidth, y / tileHeight, 0);

                if(tile != null)
                    return new Vector(x, y);

            }
        }

        return null;
    }

    public static Vector Collides(Entity entity, TiledMap map){
        return checkForCollision(entity, map);
    }
}
