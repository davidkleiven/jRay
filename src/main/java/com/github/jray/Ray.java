package com.github.jray;

public class Ray
{
    private Vector direction = new Vector(1.0, 0.0, 0.0);
    private Vector amplitude = new Vector(0.0, 1.0, 0.0);
    public Vector position = new Vector(0.0, 0.0, 0.0);
    public int mediumId = 0;

    /**
     * Return the total amplitude
     * @return
     */
    public double amplitude()
    {
        return amplitude.length();
    }

    public Vector getDirection()
    {
        return direction;
    }

    public void setDirection(double x, double y, double z)
    {
        direction.set(x, y, z);
    }

    public Vector getAmplitude()
    {
        return amplitude;
    }

    public void setAmplitude(double Ax, double Ay, double Az)
    {
        amplitude.set(Ax, Ay, Az);
    }
}