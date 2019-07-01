package com.github.jray;

public class ScatteringInfo
{
    public TriangleMesh mesh;
    public int element = -1;
    public double newRefractiveIndex;
    public double currentRefractiveIndex;
    public Vector intersectionPoint = new Vector(0.0, 0.0, 0.0);
    public double time = 0.0;
    public PhysicalMedium scatteringMedium;
}