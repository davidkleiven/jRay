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

    public Point normal(int element)
    {
        int[] ids = nodeIds.get(element);
        Point p1 = nodes.get(ids[1]).distance(nodes.get(ids[0]));
        Point p2 = nodes.get(ids[2]).distance(nodes.get(ids[0]));
        Point p3 = p1.cross(p2);
        p3.divide(p3.length());
        return p3;
    }
}