package com.github.jray;

public class Vector{
    private double[] crd = new double[3];

    /**
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public Vector(double x, double y, double z)
    {
        crd[0] = x;
        crd[1] = y;
        crd[2] = z;
    };

    public void set(double x, double y, double z)
    {
        crd[0] = x;
        crd[1] = y;
        crd[2] = z;
    }

    public double getX() {return crd[0];};
    public double getY() {return crd[1];};
    public double getZ() {return crd[2];};
    public double[] getCrd() {return crd;};

    /**
     * 
     * @param other Vector to which the distance should be computed
     * @return Vector representing the distance to the other Vector
     */
    public Vector distance(Vector other)
    {
        double x = other.getX() - this.getX();
        double y = other.getY() - this.getY();
        double z = other.getZ() - this.getZ();
        return new Vector(x, y, z);
    }

    public Vector subtract(Vector other)
    {
        double x = this.getX() - other.getX();
        double y = this.getY() - other.getY();
        double z = this.getZ() - other.getZ();
        return new Vector(x, y, z);
    }

    public Vector add(Vector other)
    {
        double x = this.getX() + other.getX();
        double y = this.getY() + other.getY();
        double z = this.getZ() + other.getZ();
        return new Vector(x, y, z);
    }

    /**
     * @param other Vector to make the cross product with
     * @return Vector representing the cross product
     */
    public Vector cross(Vector other)
    {
        double x = this.getY()*other.getZ() - this.getZ()*other.getY();
        double y = -(this.getX()*other.getZ() - this.getZ()*other.getX());
        double z = this.getX()*other.getY() - this.getY()*other.getX();
        return new Vector(x, y, z);
    }

    public double length()
    {
        return Math.sqrt(crd[0]*crd[0] + crd[1]*crd[1] + crd[2]*crd[2]);
    }

    public void idivide(double factor)
    {
        crd[0] /= factor;
        crd[1] /= factor;
        crd[2] /= factor;
    }

    public Vector divide(double factor)
    {
        double x = crd[0]/factor;
        double y = crd[1]/factor;
        double z = crd[2]/factor;
        return new Vector(x, y, z);
    }

    public double dot(Vector other)
    {
        double value = 0.0;
        for (int i=0;i<3;i++){
            value += crd[i]*other.crd[i];
        }
        return value;
    }
}