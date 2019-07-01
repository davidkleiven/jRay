package com.github.jray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Inverse3x3Test
{
    @Test
    public void testInvert()
    {
        double[][] X = {
            {1.0, 2.0, 3.0},
            {-1.0, -2.0, 1.0},
            {0.0, 1.0, 2.0}
        };

        double[][] expect = {
            {5.0/4.0, 1.0/4.0, -2.0},
            {-0.5, -0.5, 1.0},
            {0.25, 0.25, 0.0}
        };

        Inverse3x3 solver = new Inverse3x3();
        solver.invert(X);

        for (int i=0;i<3;i++)
        for (int j=0;j<3;j++){
            assertEquals(expect[i][j], X[i][j], 1E-6);
        }
    }
}