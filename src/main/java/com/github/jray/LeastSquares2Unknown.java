package com.github.jray;

public class LeastSquares2Unknown
{
    /**
     * Invert a 2x2 matrix
     * @param X 2x2 matrix
     */
    public void inv2x2(double[][] X)
    {
        double X00 = X[0][0];
        double X11 = X[1][1];
        double X01 = X[0][1];
        double X10 = X[1][0];

        double det = X00*X11 - X01*X10;
        X[0][0] = X11/det;
        X[1][1] = X00/det;
        X[1][0] = -X10/det;
        X[0][1] = -X01/det;
    }

    /**
     * 
     * @param X Design matrix Nx2
     * @param y y values length N
     * @return
     */
    public double[] solve(double[][] X, double[] y)
    {
        double[][] AtA = new double[2][2];
        AtA[0][0] = 0.0;
        AtA[0][1] = 0.0;
        AtA[1][0] = 0.0;
        AtA[1][1] = 0.0;

        for (int row=0;row<2;row++)
        for (int col=0;col<2;col++)
        for (int i=0;i<X.length;i++){
            AtA[row][col] += X[i][row]*X[i][col];
        }
        this.inv2x2(AtA);

        double[] Aty = {0.0, 0.0};
        for (int i=0;i<2;i++)
        for (int j=0;j<X.length;j++){
            Aty[i] += X[j][i]*y[j];
        }

        double[] res = {0.0, 0.0};
        for (int i=0;i<2;i++)
        for (int j=0;j<2;j++){
            res[i] += AtA[i][j]*Aty[j];
        }
        return res;
    }
}