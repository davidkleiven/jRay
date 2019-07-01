package com.github.jray;

import java.util.ArrayList;

public class Monitor
{
    private ArrayList<Vector> amplitudeReal = new ArrayList<Vector>();
    private ArrayList<Vector> amplitudeImag = new ArrayList<Vector>();

    public void setSize(int size)
    {
        for (int i=0;i<size;i++){
            amplitudeReal.add(new Vector(0.0, 0.0, 0.0));
            amplitudeImag.add(new Vector(0.0, 0.0, 0.0));
        }
    }

    public void update(int element, Ray ray)
    {
        double opl = ray.getWavenumber()*ray.opticalPathLength;
        Vector realPart = ray.getAmplitude().mult(Math.cos(opl));
        Vector imagPart = ray.getAmplitude().mult(Math.sin(opl));
        amplitudeReal.get(element).iadd(realPart);
        amplitudeImag.get(element).iadd(imagPart);
    }
}