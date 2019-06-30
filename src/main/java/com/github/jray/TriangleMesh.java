package com.github.jray;

import java.util.ArrayList;



public class TriangleMesh
{
    private ArrayList<Point> nodes = new ArrayList<Point>();
    private ArrayList<int[]> nodeIds = new ArrayList<int[]>();

    /**
     * Add a new point to the point array
     * @param uid ID in the of the node
     * @param p Point
     */
    public void addNode(Point p){
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
    public Point normal(int element)
    {
        int[] ids = nodeIds.get(element);
        Point p1 = nodes.get(ids[1]).distance(nodes.get(ids[0]));
        Point p2 = nodes.get(ids[2]).distance(nodes.get(ids[0]));
        Point p3 = p1.cross(p2);
        p3.divide(p3.length());
        return p3;
    }

    /**
     * Check if a ray intersects the element
     * @param ray
     * @param element
     * @return
     */
    public boolean rayIntersectsElement(Ray ray, int element)
    {
        int[] ids = nodeIds.get(element);

        // TODO: Precompute all normals
        Point normal = this.normal(element);

        // Ray is parametrised as x0 + t*d
        double denum = normal.dot(ray.getDirection());
        double eps = 1E-8;
        if (denum < eps){
            return false;
        }

        double t = (nodes.get(ids[0]).dot(normal) - normal.dot(ray.position))/denum;

        if (t < 0.0){
            return false;
        }

        double[] crd = new double[3];
        crd[0] = ray.position.getX() + t*ray.getDirection().getX();
        crd[1] = ray.position.getY() + t*ray.getDirection().getY();
        crd[2] = ray.position.getZ() + t*ray.getDirection().getZ();
        Point intersection = nodes.get(ids[0]).distance(new Point(crd[0], crd[1], crd[2]));

        // Find if the point is inside the triangle
        Point p1 = nodes.get(ids[0]).distance(nodes.get(ids[2]));
        Point p2 = nodes.get(ids[0]).distance(nodes.get(ids[1]));

        double[] y = {intersection.getX(), intersection.getY(), intersection.getZ()};
        double[][] X = {
            {p1.getX(), p2.getX()},
            {p1.getY(), p2.getY()},
            {p1.getZ(), p2.getZ()}
        };
        LeastSquares2Unknown solver = new LeastSquares2Unknown();
        double[] coeff = solver.solve(X, y);
        return (coeff[0] <= 1.0) && (coeff[1] <= 1.0) && (coeff[0] >= 0.0) && (coeff[1] >= 0.0);
    }
}