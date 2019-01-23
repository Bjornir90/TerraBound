package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.utils.Vector;
import engine.lighting.LightSource;
import engine.lighting.LightingCore;
import engine.particle.ParticleEmitter;
import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Torch extends Entity {
    private LightSource lightSource;
    private ParticleEmitter particleEmitter;

    public Torch(float scale, float mass, float x , float y) {
        super(scale, mass);
        this.position = new Vector(x, y);
        try{
            init();
        } catch (SlickException e){
            Sys.alert("Error", "Could not load sprite MetalTorch");
            e.printStackTrace();
        }
    }

    private void init() throws SlickException {
        lightSource = new LightSource(new Color( 230.0f/255.0f, 160.0f/255.0f, 20.0f/255.0f), 60.0f, getX()+getScaledWidth()+10, getY()+1);
        lightSource.turnOn();
        particleEmitter = new ParticleEmitter(0.000001f, 0.0f, 0.00002f, 2.0f, Color.orange, null, getX()+getScaledWidth()+10, getY()+2, 2000);
        particleEmitter.setInterval(1000);
        particleEmitter.setEmission(0.0f, -0.003f, 0.003f, 0.0005f);
        this.loadSprite("sprites/MetalTorch.png");
    }

    @Override
    public void onTerrainCollision(int side) {
        //Nothing
    }

    @Override
    public void onUpdate(int delta) {
        particleEmitter.updateParticles(delta);
    }

    public void draw(Graphics g){
        LightingCore.startTexRendering();
        super.draw();
        LightingCore.endTexRendering();
        LightingCore.startPrimRendering();
        particleEmitter.drawParticles(g);
        LightingCore.endPrimRendering();
    }
    @Override
    public void onCollisionSideChange(int newSide, boolean colliding) {
        //Nothing
    }


}
