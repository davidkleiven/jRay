package com.github.jray;

public class PhysicalMedium
{
    private double refractiveIndex = 1.0;
    private static int nextID = 1; // Zero is reserved for VACUUM
    private int uid = 0;
    public TriangleMesh mesh = null;

    public PhysicalMedium(double n)
    {
        refractiveIndex = n;
        uid = nextID;
        nextID += 1;
    }

    public int getID()
    {
        return uid;
    }

    public double getRefractiveIndex()
    {
        return refractiveIndex;
    }
}