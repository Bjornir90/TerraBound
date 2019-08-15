package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.Side;
import com.bjornir.terrabound.utils.Vector;

public class Arrow extends Entity {
    private static final int millisecondsOfFlyingStraight = 250;
    private int timeFromCreation;
    private boolean isLanded;

    public Arrow(String spritePath, int width, int height) {
        super(spritePath, width, height);
        timeFromCreation = 0;
        isPhysical = false;
        isLanded = false;
    }


    @Override
    protected void onTerrainCollision(Side side){
        speed = new Vector();
        isPhysical = false;
        isLanded = true;
    }

    @Override
    public void update(int delta){
        super.update(delta);
        timeFromCreation += delta;
        if(timeFromCreation >= millisecondsOfFlyingStraight && !isLanded)
            isPhysical = true;


        if(!speed.isNullVector()){
            setAngle(speed.getAngle());
        }

    }


}
