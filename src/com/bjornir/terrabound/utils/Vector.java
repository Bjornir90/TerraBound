package com.bjornir.terrabound.utils;

public class Vector {
    private float x, y;

    public Vector(){
        x = 0;
        y = 0;
    }

    public Vector(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setCoordinates(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void addVector(Vector vector){
        this.x += vector.getX();
        this.y += vector.getY();
    }

    public void multiplyScalar(float k){
        this.x = this.x * k;
        this.y = this.y * k;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
