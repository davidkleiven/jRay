package com.github.jray;

import java.util.ArrayList;



public class TriangleMesh
{
    private ArrayList<Vector> nodes = new ArrayList<Vector>();
    private ArrayList<int[]> nodeIds = new ArrayList<int[]>();

    /**
     * Add a new Vector to the Vector array
     * @param uid ID in the of the node
     * @param p Vector
     */
    public void addNode(Vector p){
        nodes.add(p);
    }

    public void addElement(int[] ids)
    {
        nodeIds.add(ids);
    }

    public int numElements()
    {
        return nodeIds.size();
    }

    /**
     * Return the number of nodes
     * @return
     */
    public int numNodes()
    {
        return nodes.size();
    }

    /**
     * Calculate the normal of a facet
     * @param element
     * @return
     */
    public Vector normal(int element)
    {
        int[] ids = nodeIds.get(element);
        Vector p1 = nodes.get(ids[1]).distance(nodes.get(ids[0]));
        Vector p2 = nodes.get(ids[2]).distance(nodes.get(ids[0]));
        Vector p3 = p1.cross(p2);
        p3.idivide(p3.length());
        return p3;
    }

    private double[] barycentricTimeToHit(Ray ray, int element){
        int[] ids = nodeIds.get(element);

        // Find if the Vector is inside the triangle
        Vector p1 = nodes.get(ids[0]).distance(nodes.get(ids[2]));
        Vector p2 = nodes.get(ids[0]).distance(nodes.get(ids[1]));
        Vector rayOrigin = nodes.get(ids[0]).distance(ray.position);

        double[] y = {rayOrigin.getX(), rayOrigin.getY(), rayOrigin.getZ()};
        double[][] X = {
            {p1.getX(), p2.getX(), -ray.getDirection().getX()},
            {p1.getY(), p2.getY(), -ray.getDirection().getY()},
            {p1.getZ(), p2.getZ(), -ray.getDirection().getZ()}
        };
        Inverse3x3 solver = new Inverse3x3();
        double[] coeff = {0.0, 0.0, 0.0};
        try{
            solver.invert(X);
        } catch (RuntimeException e){
            double[] negImpactTime = {0.0, 0.0, -1.0};
            return negImpactTime;
        }
        
        for (int i=0;i<3;i++)
        for (int j=0;j<3;j++){
            coeff[i] += X[i][j]*y[j];
        }
        return coeff;
    }

    /**
     * Check if a ray intersects the element
     * @param ray
     * @param element
     * @return
     */
    public boolean rayIntersectsElement(Ray ray, int element)
    {
        double[] coeff = this.barycentricTimeToHit(ray, element);
        return (coeff[0] + coeff[1] <= 1.0) && (coeff[0] >= 0.0) && (coeff[1] >= 0.0) && (coeff[2] >= 0.0);
    }

    public double timeUntilHit(Ray ray, int element)
    {
        double[] coeff = this.barycentricTimeToHit(ray, element);
        return coeff[2];
    }
}