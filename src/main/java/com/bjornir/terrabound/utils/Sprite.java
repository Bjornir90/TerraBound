package com.bjornir.terrabound.utils;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.HashMap;

public class Sprite extends Image {

    public static HashMap<String, Sprite> LoadedSprites;

    public Sprite(String ref) throws SlickException {
        super("sprites/"+ref);
    }

    public Sprite(Image sprite){
        super(sprite);
    }

    public void drawWithShader(float x, float y, float width, float height){
        this.texture.bind();

        GL.glTranslatef(x, y, 0.0F);

        if (this.angle != 0.0F) {
            GL.glTranslatef(this.centerX, this.centerY, 0.0F);
            GL.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
            GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
        }

        GL.glTranslatef(-x, -y, 0.0F);


        GL.glBegin(7);
        this.drawEmbedded(x, y, width, height);
        GL.glEnd();

        GL.glTranslatef(x, y, 0.0F);

        if (this.angle != 0.0F) {
            GL.glTranslatef(this.centerX, this.centerY, 0.0F);
            GL.glRotatef(-this.angle, 0.0F, 0.0F, 1.0F);
            GL.glTranslatef(-this.centerX, -this.centerY, 0.0F);
        }

        GL.glTranslatef(-x, -y, 0.0F);

    }

    public Sprite copy(){
        return new Sprite(this);
    }
}
