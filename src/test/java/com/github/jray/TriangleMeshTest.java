package com.github.jray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TriangleMeshTest
{
    private static final double EPS = 1E-6;
    @Test
    public void testNumIntersectCenter()
    {
        GMSHReader reader = new GMSHReader();
        TriangleMesh mesh = reader.readGMSH("res/sphere_v415.msh");
        Ray ray = new Ray();
        ray.position.set(0.01, 0.0, 0.0);

        int num_intersections = 0;
        for (int i=0;i<mesh.numElements();i++){
            if (mesh.rayIntersectsElement(ray, i)){
                num_intersections += 1;
            }
        }
        assertEquals(1, num_intersections);
    }

    @Test
    public void testNumOutside()
    {
        GMSHReader reader = new GMSHReader();
        TriangleMesh mesh = reader.readGMSH("res/sphere_v415.msh");
        Ray ray = new Ray();
        ray.position.set(2.0, 0.0, 0.0);
        ray.setDirection(1.0, 0.0, 0.0);

        int num_intersections = 0;
        for (int i=0;i<mesh.numElements();i++){
            if (mesh.rayIntersectsElement(ray, i)){
                num_intersections += 1;
            }
        }
        assertEquals(0, num_intersections);
    }

    @Test
    public void testCentroid()
    {
        TriangleMesh mesh = new TriangleMesh();
        mesh.addNode(new Vector(0.0, 0.0, 0.0));
        mesh.addNode(new Vector(-1.0, 0.0, 0.0));
        mesh.addNode(new Vector(0.0, 1.0, 0.0));
        int ids[] = {0, 1, 2};
        mesh.addElement(ids);

        Vector com = mesh.centroid(0);
        assertEquals(com.getX(), -1.0/3.0, EPS);
        assertEquals(com.getY(), 1.0/3.0, EPS);
        assertEquals(com.getZ(), 0.0, EPS);
    }
}