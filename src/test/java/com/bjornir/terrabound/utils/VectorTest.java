package com.bjornir.terrabound.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VectorTest {

    @org.junit.jupiter.api.Test
    void rotateSelf(){
        float delta = 0.0001f;
        Vector origin = new Vector(0, 0);
        Vector v = new Vector(3,3);
        float oldNorm = v.norm();
        v.rotateSelf(origin, 45);

        assertEquals(oldNorm, v.norm());
        assertEquals(3.0f*(float)Math.sqrt(2), v.getY(), delta);
        assertEquals(0.0f, v.getX(), delta);
        assertEquals(90.0f, v.getAngle(), delta);

        v.rotateSelf(origin, -45);

        assertEquals(oldNorm, v.norm());
        assertEquals(3.0f, v.getY(), delta);
        assertEquals(3.0f, v.getX(), delta);
        assertEquals(45.0f, v.getAngle(), delta);

        v.rotateSelf(origin, 180);

        assertEquals(oldNorm, v.norm());
        assertEquals(-3.0f, v.getY(), delta);
        assertEquals(-3.0f, v.getX(), delta);
        assertEquals(-135.0f, v.getAngle(), delta);

        v.rotateSelf(origin, -90);

        assertEquals(oldNorm, v.norm());
        assertEquals(3.0f, v.getY(), delta);
        assertEquals(-3.0f, v.getX(), delta);
        assertEquals(135.0f, v.getAngle(), delta);
    }

    @org.junit.jupiter.api.Test
    void angleTest(){
        float delta = 0.0001f;
        Vector v = new Vector(1, 0);
        assertEquals(0.0f, v.getAngle(), delta);
        v.setCoordinates(2.5f, -3);
        assertEquals(-50.1944f, v.getAngle(), delta);
        v.setCoordinates(0, 2);
        assertEquals(90.0f, v.getAngle(), delta);
        v.setCoordinates(-2, 4);
        assertEquals(116.565f, v.getAngle(), delta);
        v.setCoordinates(-1.5f, -0.85f);
        assertEquals(-150.4612f, v.getAngle(), delta);
    }

}
