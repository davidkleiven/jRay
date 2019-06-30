package com.github.jray;

public class Ray
{
    private Point direction = new Point(1.0, 0.0, 0.0);
    private Point amplitude = new Point(0.0, 1.0, 0.0);
    public Point position = new Point(0.0, 0.0, 0.0);

    /**
     * Return the total amplitude
     * @return
     */
    public double amplitude()
    {
        return amplitude.length();
    }

    public Point getDirection()
    {
        return direction;
    }

    public void setDirection(double x, double y, double z)
    {
        direction.set(x, y, z);
    }
}