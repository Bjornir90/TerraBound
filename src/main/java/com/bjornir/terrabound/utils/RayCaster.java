package com.bjornir.terrabound.utils;

import com.bjornir.terrabound.entities.Movable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

public class RayCaster {

	private static ArrayList<Rectangle> boundariesToDraw;

	static{
		boundariesToDraw = new ArrayList<>();
	}

	public static void drawRays(Graphics g){
		Color[] colors = new Color[4];
		colors[0] = Color.cyan;//top
		colors[1] = Color.green;//left
		colors[2] = Color.orange;//bottom
		colors[3] = Color.magenta;//right
		int colorIndex = 0;
		for(Rectangle r : boundariesToDraw){
			g.setColor(colors[colorIndex]);
			colorIndex++;
			g.draw(r);
		}
		boundariesToDraw.clear();//To avoid redrawing the same boundaries over and over
	}

    public static void prepareRayDraw(float width, float height, Vector origin, int axis){
		Rectangle toDraw = calculateRayCastCoordinates(width, height, origin, axis);
	    boundariesToDraw.add(toDraw);
    }

    protected static Rectangle calculateRayCastCoordinates(float width, float height, Vector origin, int axis){ //The width and height are the size of the sprite, but the coordinates are the centers of the sides of the sprites
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
		    System.err.println("You shouldn't see this, calculateRayCastCoordinates() has been called with wrong axis argument");
		    System.exit(0);
	    }
	    Rectangle rectangle = new Rectangle(x1, y1, Math.abs(x1-x2), Math.abs(y1-y2));
	    return rectangle;
    }

	public static boolean raycastTerrain(float width, float height, Vector origin, int axis){
        float x1, x2, y1, y2;
        Rectangle rayBoundaries = calculateRayCastCoordinates(width, height, origin, axis);
        x1 = rayBoundaries.getX();
        x2 = x1+rayBoundaries.getWidth();
        y1 = rayBoundaries.getY();
        y2 = y1+rayBoundaries.getHeight();
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
