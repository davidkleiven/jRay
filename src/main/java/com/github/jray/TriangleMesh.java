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

    public int numNodes()
    {
        return nodes.size();
    }
}