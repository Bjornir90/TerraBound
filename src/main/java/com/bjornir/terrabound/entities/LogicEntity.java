package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.Side;

public class LogicEntity extends Entity {

    public LogicEntity(Entity entity){
        super(entity);
    }

    @Override
    protected void onTerrainCollision(Side side) {

    }
}
