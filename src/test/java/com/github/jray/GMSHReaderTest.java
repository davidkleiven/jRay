
package com.github.jray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GMSHReaderTest
{
    @Test
    public void testNumNodes()
    {
        GMSHReader reader = new GMSHReader();
        TriangleMesh mesh = reader.readGMSH("res/sphere_v415.msh");
        assertEquals(mesh.numNodes(), 123);
    }
}