package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.HookTargetWatcher;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class HookTarget extends Entity {

    private Animation anim;
    private boolean inRange;
    private HookTargetWatcher watcher;

    public HookTarget(float scale, float mass) {
        super(scale, mass);
        try {
            loadSprite("sprites/HookTarget.png");
        } catch (SlickException e) {
            System.err.println("Could not load sprite for HookTarget");
            e.printStackTrace();
        }
        SpriteSheet sheet = new SpriteSheet(this.sprite, 32, 32);
        anim = new Animation(sheet, 200);
        anim.setAutoUpdate(false);

        this.sprite = sheet.getSprite(0, 0);

        inRange = false;
    }

    @Override
    public void draw() {
        if(inRange)
            anim.getCurrentFrame().draw(this.position.getX(), this.position.getY());
        else
            super.draw();
    }

    @Override
    public void onTerrainCollision(int side) {
        //Nothing to do
    }

    @Override
    public void onUpdate(int delta) {
        if(inRange){
            anim.update(delta);
        }
    }

    @Override
    public void onCollisionSideChange(int newSide, boolean colliding) {
        //Nothing to do
    }

    public boolean isInRange() {
        return inRange;
    }

    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }
}
