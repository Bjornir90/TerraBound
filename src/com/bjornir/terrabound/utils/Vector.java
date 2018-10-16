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

    public Vector(Vector v){
        this.x = v.getX();
        this.y = v.getY();
    }

    public void setCoordinates(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void addSelfVector(Vector vector){
        this.x += vector.getX();
        this.y += vector.getY();
    }

    public void multiplySelfScalar(float k){
        this.x = this.x * k;
        this.y = this.y * k;
    }

    public Vector addVector(Vector vector){
        Vector toReturn = new Vector();
        toReturn.setX(this.getX());
        toReturn.setY(this.getY());
        toReturn.addSelfVector(vector);
        return toReturn;
    }

    public Vector multiplyScalar(float k){
        Vector toReturn = new Vector();
        toReturn.setX(this.getX());
        toReturn.setY(this.getY());
        toReturn.multiplySelfScalar(k);
        return toReturn;
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

    /**
     * Creates a new Vector, that has this x coordinate and 0 as its y coordinate. It simply instantiate a new Vector using this Vector x coordinate.
     * @return the projection on the x axis of this Vector
     */
    public Vector getXProjection(){
        return new Vector(this.x, 0);
    }

    /**
     * Creates a new Vector, that has this y coordinate and 0 as its x coordinate. It simply instantiate a new Vector using this Vector y coordinate.
     * @return the projection on the y axis of this Vector
     */
    public Vector getYProjection(){
        return new Vector(0, this.y);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
