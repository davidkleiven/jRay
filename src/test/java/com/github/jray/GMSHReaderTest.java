
package com.github.jray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GMSHReaderTest
{
    private static final double EPS = 1E-6;
    @Test
    public void testNumNodes()
    {
        GMSHReader reader = new GMSHReader();
        TriangleMesh mesh = reader.readGMSH("res/sphere_v415.msh");
        assertEquals(mesh.numNodes(), 123);
    }

    @Test
    public void testNumElements()
    {
        GMSHReader reader = new GMSHReader();
        TriangleMesh mesh = reader.readGMSH("res/sphere_v415.msh");
        assertEquals(mesh.numElements(), 242);
    }

    @Test
    public void normalVector()
    {
        GMSHReader reader  = new GMSHReader();
        TriangleMesh mesh = reader.readGMSH("res/sphere_v415.msh");
        Vector normal = mesh.normal(0);

        // Raw data extracted from the file corresponding to element 0
        Vector p1 = new Vector(-0.5562866502263529, 0.7109209691640017, -0.430274956722861);
        Vector p2 = new Vector(-0.7567388274803838, 0.6432409977866912, -0.1165648564104794);
        Vector p3 = new Vector(-0.4059781961839712, 0.9095246254563067, -0.08914404024820735);
        Vector p4 = p2.distance(p1);
        Vector p5 = p3.distance(p1);
        Vector p6 = p4.cross(p5);
        p6.idivide(p6.length());
        
        assertEquals(p6.getX(), normal.getX(), EPS);
        assertEquals(p6.getY(), normal.getY(), EPS);
        assertEquals(p6.getZ(), normal.getZ(), EPS);
    }
}