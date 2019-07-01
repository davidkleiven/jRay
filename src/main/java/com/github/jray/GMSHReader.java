package com.github.jray;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

enum GMSHSection{
    UNKNOWN,
    NODES,
    ELEMENTS
}

public class GMSHReader
{
    /**
     * Parse the number of nodes from GMSH file. This method is called
     * right after $Nodes is found
     * 
     * @param line Frist line after $Nodes is found
     */
    protected int parseNumNodes(String line)
    {
        String numNodes = line.split(" ")[1];
        return Integer.parseInt(numNodes);
    }

    /**
     * Extract the number in block
     * @param line
     * @return Number of Vectors in the block
     */
    protected int numNodesInBlock(String line)
    {
        String[] partitioned = line.split(" ");
        String numInBlock = partitioned[partitioned.length - 1];
        return Integer.parseInt(numInBlock);
    }

    /**
     * Extract a Vector from a line in the file
     * @param line
     * @return
     */
    protected Vector getVector(String line)
    {
        String[] partitioned = line.split(" ");
        double x = Double.parseDouble(partitioned[1]);
        double y = Double.parseDouble(partitioned[2]);
        double z = Double.parseDouble(partitioned[3]);
        return new Vector(x, y, z);
    }

    /**
     * Extract the element type
     * @param line
     * @return Element type
     */
    protected int elementType(String line)
    {
        String elemType = line.split(" ")[2];
        return Integer.parseInt(elemType);
    }

    /**
     * Extract 0-indexed ID of nodes
     * @param line
     * @return
     */
    protected int[] triangleElementNodeIds(String line)
    {
        String[] partitioned = line.split(" ");
        int[] nodeIds = new int[3];
        for (int i=1;i<partitioned.length;i++){
            nodeIds[i-1] = Integer.parseInt(partitioned[i]) - 1;
        }
        return nodeIds;
    }

    /**
     * @param fname GMSH mesh filename
     */
    public TriangleMesh readGMSH(String fname)
    {
        GMSHSection section = GMSHSection.UNKNOWN;
        TriangleMesh mesh = new TriangleMesh();
        int numNodes = -1;
        int numElements = -1;
        int elementType = -1;

        try(BufferedReader br = Files.newBufferedReader(Paths.get(fname))){
            String line;
            int numInBlock = -1;
            int numReadFromBlock = 0;
            while((line = br.readLine()) != null){
                // Parse section
                if (line.startsWith("$Nodes")){
                    section = GMSHSection.NODES;
                    continue;
                }
                else if (line.startsWith("$End")){
                    numInBlock = -1;
                    numReadFromBlock = 0;
                    section = GMSHSection.UNKNOWN;
                    continue;
                }
                else if (line.startsWith("$Elements")){
                    section = GMSHSection.ELEMENTS;
                    continue;
                }

                // Parse content
                switch (section){
                    case NODES:
                        if (numNodes == -1){
                            numNodes = this.parseNumNodes(line);
                            continue;
                        }

                        // Read how many entries there is in a block
                        if (numInBlock == -1){
                            numInBlock = this.numNodesInBlock(line);
                            numReadFromBlock = 0;
                            continue;
                        }

                        // Check if we have read the correct number of lines
                        if (numInBlock == numReadFromBlock){
                            numInBlock = this.numNodesInBlock(line);
                            numReadFromBlock = 0;
                            continue;
                        }
                        mesh.addNode(this.getVector(line));
                        numReadFromBlock += 1;
                        break;
                    case ELEMENTS:
                        if (numElements == -1){
                            numElements = this.parseNumNodes(line);
                            continue;
                        }

                        // Find element type
                        if ((numInBlock == -1) || (numReadFromBlock == numInBlock)){
                            elementType = this.elementType(line);
                            numInBlock = this.numNodesInBlock(line);
                            numReadFromBlock = 0;
                            continue;
                        }
                        
                        // We only read the triangles
                        if (elementType == 2){
                            mesh.addElement(this.triangleElementNodeIds(line));
                        }
                        numReadFromBlock += 1;
                        break;
                    case UNKNOWN:
                        break;
                }
            }
        } catch(IOException e){
            System.err.format("IOException: %s%n", e);
        }
        return mesh;
    }
}