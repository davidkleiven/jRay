package com.github.jray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTest{
    private static final double EPS = 1E-6;
    @Test
    public void testDistance()
    {
        Vector p1 = new Vector(1.0, -2.0, 3.0);
        Vector p2 = new Vector(3.0, 1.0, 2.0);
        Vector distance = p1.distance(p2);
        assertEquals(distance.getX(), 2.0, EPS);
        assertEquals(distance.getY(), 3.0, EPS);
        assertEquals(distance.getZ(), -1.0, EPS);
    }

    @Test
    public void testCrossProduct()
    {
        Vector p1 = new Vector(1.0, 2.0, 3.0);
        Vector p2 = new Vector(2.0, -1.0, 4.0);
        Vector cross = p1.cross(p2);

        assertEquals(cross.getX(), 11.0, EPS);
        assertEquals(cross.getY(), 2.0, EPS);
        assertEquals(cross.getZ(), -5.0, EPS);
    }

    @Test
    public void testLength()
    {
        Vector p1 = new Vector(1.0, 2.0, -3.0);
        assertEquals(p1.length(), Math.sqrt(14.0), EPS);
    }

    @Test
    public void testDot()
    {
        Vector p1 = new Vector(1.0, 2.0, 3.0);
        Vector p2 = new Vector(-1.0, 2.0, -2.0);
        assertEquals(p1.dot(p2), -3.0, EPS);
    }
}