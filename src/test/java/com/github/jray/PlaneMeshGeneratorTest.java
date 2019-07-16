package com.github.jray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

public class PlaneMeshGeneratorTest
{
    @Test
    public void nonOrthogonalVectors(){
        boolean raisedIllegalArgumentException = false;
        try{
            new PlaneMeshGenerator().generate(
                0.1, new Vector(1.0, 0.0, 0.0), new Vector(0.5, 0.5, 0.0), new Vector(0.0, 0.0, 0.0));
        } catch (IllegalArgumentException e){
            raisedIllegalArgumentException = true;
        }
        assertTrue(raisedIllegalArgumentException);
    }

    @Test
    public void testSimpleXYMesh()
    {
        TriangleMesh mesh = new PlaneMeshGenerator().generate(
            0.1, new Vector(1.0, 0.0, 0.0), new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 0.0));
        assertEquals(100, mesh.numNodes());
    }
}