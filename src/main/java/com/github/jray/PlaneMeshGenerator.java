package com.github.jray;

public class PlaneMeshGenerator
{
    /**
     * @param dx Discretisation size
     * @param v1 One vector in plane
     * @param v2 Another vector in plane (has to orthogonal to the first)
     */
    public TriangleMesh generate(double dx, Vector v1, Vector v2, Vector origin) throws IllegalArgumentException
    {
        double dotProd = v1.dot(v2);

        if (Math.abs(dotProd) > 1E-8)
        {
            throw new IllegalArgumentException("The two in-plane vectors has to be orthogonal!");
        }

        int numAlongV1 = (int)(v1.length()/dx);
        int numAlongV2 = (int)(v2.length()/dx);

        // Normalize such that v1 and v2 are unit vectors
        v1.idivide(v1.length());
        v2.idivide(v2.length());

        TriangleMesh mesh = new TriangleMesh();

        // Add all nodes
        for (int i1=0;i1<numAlongV1;i1++)
        for (int i2=0;i2<numAlongV2;i2++){
            Vector node = new Vector(origin.getX(), origin.getY(), origin.getZ());
            node.iadd(v1.mult(i1*dx));
            node.iadd(v2.mult(i2*dx));
            mesh.addNode(node);
        }

        int numNodes = numAlongV1*numAlongV2;

        // Connect all elements
        for (int i=0;i<numNodes;i++){
            int row = i%numAlongV1;
            int col = (int)(i/numAlongV1);

            if ((row < numAlongV1-1) && (col < numAlongV2-1))
            {
                // Add triangle consisting of (r, c) - (r, c+1) - (r+1,c+1)
                int[] ids1 = {i, i+1, i+numAlongV1+1};
                mesh.addElement(ids1);
                // Add triangle consisting of (r, c) - (r+1, c) - (r+1,c+1)
                int[] ids2 = {i, i+numAlongV1, i+numAlongV1+1};
                mesh.addElement(ids2);
            }
        }
        return mesh;
    }
}