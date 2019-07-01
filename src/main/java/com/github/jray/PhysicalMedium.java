package com.github.jray;

import javax.naming.directory.InvalidAttributeValueException;

public class PhysicalMedium
{
    private double refractiveIndex = 1.0;
    private static int nextID = 1; // Zero is reserved for Vacuum
    private int uid = 0;
    private Monitor monitor = null;
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

    public void useMonitor() throws InvalidAttributeValueException
    {
        if (mesh == null){
            throw new InvalidAttributeValueException("Mesh is not set!");
        }

        monitor = new Monitor();
        monitor.setSize(mesh.numElements());
    }

    public void updateMonitor(int element, Ray ray)
    {
        if (monitor == null){
            return;
        }
        monitor.update(element, ray);
    }
}