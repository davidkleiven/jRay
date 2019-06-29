package com.github.jray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PointTest{
    private static final double EPS = 1E-6;
    @Test
    public void testDistance()
    {
        Point p1 = new Point(1.0, -2.0, 3.0);
        Point p2 = new Point(3.0, 1.0, 2.0);
        Point distance = p1.distance(p2);
        assertEquals(distance.getX(), 2.0, EPS);
        assertEquals(distance.getY(), 3.0, EPS);
        assertEquals(distance.getZ(), -1.0, EPS);
    }

    @Test
    public void testCrossProduct()
    {
        Point p1 = new Point(1.0, 2.0, 3.0);
        Point p2 = new Point(2.0, -1.0, 4.0);
        Point cross = p1.cross(p2);

        assertEquals(cross.getX(), 11.0, EPS);
        assertEquals(cross.getY(), 2.0, EPS);
        assertEquals(cross.getZ(), -5.0, EPS);
    }
}