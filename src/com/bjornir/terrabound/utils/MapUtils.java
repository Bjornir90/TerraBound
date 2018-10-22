package com.bjornir.terrabound.utils;

import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

import java.util.Map;

public class MapUtils {
    private static TiledMap map;
    private static int tileWidth, tileHeight;

    public static void setMap(TiledMap map) {
        MapUtils.map = map;
        MapUtils.tileWidth = map.getTileWidth();
        MapUtils.tileHeight = map.getTileHeight();
    }

    public static boolean collidesWithTerrain(Vector toTest){
        return collidesWithTerrain(toTest.getX(), toTest.getY());
    }

    public static boolean collidesWithTerrain(float x, float y){
        Image tile = MapUtils.map.getTileImage((int)  x/MapUtils.tileWidth, (int) y/MapUtils.tileHeight, 0);
        return tile != null;
    }

    public static int getMapWidth(){
        return map.getWidth()*map.getTileWidth();
    }

    public static int getMapHeight(){
        return map.getHeight()*map.getTileHeight();
    }
}
