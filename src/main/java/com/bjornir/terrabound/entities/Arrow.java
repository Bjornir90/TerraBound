package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.Side;
import com.bjornir.terrabound.utils.Vector;

public class Arrow extends Entity {

    public Arrow(String spritePath, int width, int height) {
        super(spritePath, width, height);
    }


    @Override
    protected void onTerrainCollision(Side side){
        speed = new Vector();
        isPhysical = false;
    }
}
