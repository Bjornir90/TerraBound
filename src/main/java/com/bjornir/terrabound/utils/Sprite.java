package com.bjornir.terrabound.utils;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Sprite extends Image {

    public Sprite(String ref) throws SlickException {
        super(ref);
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
}
