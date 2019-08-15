package com.bjornir.terrabound.utils;

import java.util.ArrayList;

public class Vector {

    public static Vector getMeanPosition(ArrayList<Vector> list){
        Vector result = new Vector();
        for(Vector vec : list){
            result.addSelfVector(vec);
        }
        result.multiplySelfScalar(1.0f/list.size());
        return result;
    }

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

    public Vector negateVector(){
        Vector result = new Vector();
        result.setX(-this.getX());
        result.setY(-this.getY());
        return result;
    }

    public float norm(){
        return (float) Math.sqrt(Math.pow(this.x, 2)+Math.pow(this.y, 2));
    }

    public void normalizeSelf(){
        float norm = this.norm();
        this.x = this.x/norm;
        this.y = this.y/norm;
    }

    public float getAngle(){
        float angle = (float) Math.toDegrees(Math.acos(this.x/this.norm()));
        if(this.y < 0){
            angle = -angle;
        }

        return angle;
    }

    public void rotateSelf(Vector center, float angle){
        float angleRad = (float) Math.toRadians(angle);
        float thisAngleRad = (float)Math.toRadians(this.getAngle());

        this.addSelfVector(center.negateVector());
        float rotatedX = norm()*(float)Math.cos(thisAngleRad+angleRad), rotatedY = norm()*(float)Math.sin(thisAngleRad+angleRad);
        this.setX(rotatedX);
        this.setY(rotatedY);
        this.addSelfVector(center);
    }

    public boolean isNullVector(){
        return (this.x == 0 && this.y == 0);
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

    public void addX(float x){ this.x += x; }

    public void addY(float y){ this.y += y; }


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
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + getAngle() +
                ", norm=" + norm() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (Float.compare(vector.x, x) != 0) return false;
        return Float.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}
