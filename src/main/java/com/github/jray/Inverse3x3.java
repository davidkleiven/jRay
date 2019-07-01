package com.github.jray;

import javax.management.RuntimeErrorException;

public class Inverse3x3{
    /**
     * Calculate the minor
     * @param X
     * @param row
     * @param col
     * @return
     */
    private double minor(double[][] X, int row, int col)
    {
        double value1 = X[(row+1)%3][(col+1)%3]*X[(row+2)%3][(col+2)%3];
        double value2 = X[(row+2)%3][(col+1)%3]*X[(row+1)%3][(col+2)%3];
        return value1 - value2;

    }

    /**
     * Calculate the determinant of a 3x3 matrix
     * @param X 3x3 matrix
     */
    private double determinant(double[][] X){
        double value = 0.0;
        for (int i=0;i<3;i++){
            value += X[0][i]*this.minor(X, 0, i);
        }
        return value;
    }

    /**
     * Invert a 3x3 matrix
     * @param X 3x3 matrix
     */
    public void invert(double[][] X) throws RuntimeException
    {
        double det = this.determinant(X);

        if (Math.abs(det) < 1E-8){
            throw new RuntimeException("Matrix is not invertible!");
        }
        double[][] cofactors = new double[3][3];

        for (int i=0;i<3;i++)
        for (int j=0;j<3;j++){
            cofactors[i][j] = this.minor(X, i, j);
        }

        for (int i=0;i<3;i++)
        for (int j=0;j<3;j++){
            X[i][j] = cofactors[j][i]/det;
        }
    }
}