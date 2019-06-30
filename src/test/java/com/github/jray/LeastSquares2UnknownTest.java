package com.github.jray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LeastSquares2UnknownTest{
    private static final double EPS = 1E-6;

    @Test
    public void testInvert2x2()
    {
        double[][] X = new double[2][2];
        X[0][0] = 1.0;
        X[0][1] = 2.0;
        X[1][0] = -1.0;
        X[1][1] = 1.0;

        LeastSquares2Unknown solver = new LeastSquares2Unknown();
        solver.inv2x2(X);
        assertEquals(X[0][0], 1.0/3.0, EPS);
        assertEquals(X[0][1], -2.0/3.0, EPS);
        assertEquals(X[1][0], 1.0/3.0, EPS);
        assertEquals(X[1][1], 1.0/3.0, EPS);
    }

    @Test
    public void testStraightLine(){
        double[][] X = {
            {1.0, 0.0},
            {1.0, 1.0},
            {1.0, 2.0},
            {1.0, 3.0}
        };
        double[] y = {-1.0, 1.0, 3.0, 5.0};

        LeastSquares2Unknown solver = new LeastSquares2Unknown();
        double[] coeff = solver.solve(X, y);
        assertEquals(-1.0, coeff[0], EPS);
        assertEquals(2.0, coeff[1], EPS);
    }
}