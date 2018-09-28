package com.bjornir.terrabound.utils;

import com.bjornir.terrabound.entities.Movable;

public class RayCaster {


    public static boolean raycastTerrain(float width, float height, Vector origin, int axis){
        float x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        if(axis == Movable.TOP){
            x1 = origin.getX()-width/2;
            x2 = origin.getX()+width/2;
            y1 = origin.getY()-height;
            y2 = origin.getY();
        } else if(axis == Movable.LEFT){
            x1 = origin.getX()-width;
            x2 = origin.getX();
            y1 = origin.getY()-height/2;
            y2 = origin.getY()+height/2;
        } else if(axis == Movable.RIGHT){
            x1 = origin.getX();
            x2 = origin.getX()+width;
            y1 = origin.getY()-height/2;
            y2 = origin.getY()+height/2;
        } else if(axis == Movable.BOTTOM){
            x1 = origin.getX()-width/2;
            x2 = origin.getX()+width/2;
            y1 = origin.getY();
            y2 = origin.getY()+height;
        } else {
            System.err.println("You shouldn't see this, raycastTerrain() has been called with wrong axis argument");
            System.exit(0);
        }
        return raycastTerrainAbsolute(x1, x2, y1, y2);
    }

    private static boolean raycastTerrainAbsolute(float x1, float x2, float y1, float y2){
        for(float x = x1; x < x2; x++){
            for(float y = y1; y < y2; y++){
                if(MapUtils.collidesWithTerrain(x, y)){
                    return true;
                }
            }
        }
        return false;
    }
}
