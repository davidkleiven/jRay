package com.github.jray;

public class Point{
    private double[] crd = new double[3];

    /**
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public Point(double x, double y, double z)
    {
        crd[0] = x;
        crd[1] = y;
        crd[2] = z;
    };

    public double getX() {return crd[0];};
    public double getY() {return crd[1];};
    public double getZ() {return crd[2];};
    public double[] getCrd() {return crd;};

    /**
     * 
     * @param other Point to which the distance should be computed
     * @return Point representing the distance to the other point
     */
    public Point distance(Point other)
    {
        double x = other.getX() - this.getX();
        double y = other.getY() - this.getY();
        double z = other.getZ() - this.getZ();
        return new Point(x, y, z);
    }
}