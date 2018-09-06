package com.bjornir.terrabound.utils;

public class RayCaster {


    public static boolean raycastTerrain(float height, float width, Vector origin){
        for(float x = origin.getX(); x < origin.getX()+width; x++){
            for(float y = origin.getY(); y < origin.getY()+height; y++){
                if(MapUtils.collidesWithTerrain(x, y)){
                    return true;
                }
            }
        }
        return false;
    }
}
