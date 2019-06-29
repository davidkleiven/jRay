package com.github.jray;

import java.util.ArrayList;

import org.omg.CORBA.UNKNOWN;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

enum GMSHSection{
    UNKNOWN,
    NODES,
    ELEMENTS
}

public class TriangleMesh
{
    private ArrayList<Point> nodes = new ArrayList<Point>();
    private ArrayList<int[]> nodeIds = new ArrayList<int[]>();

    /**
     * 
     * @param fname GMSH mesh filename
     */
    public void readGMSH(String fname)
    {
        GMSHSection section = GMSHSection.UNKNOWN;

        try(BufferedReader br = Files.newBufferedReader(Paths.get(fname))){
            String line;

            while((line = br.readLine()) != null){
                // Parse section
                if (line.startsWith("$Nodes")){
                    section = GMSHSection.NODES;
                    continue;
                }
                else if (line.startsWith("$End")){
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
                        break;
                    case ELEMENTS:
                        break;
                    case UNKNOWN:
                        break;
                }
            }
        } catch(IOException e){
            System.err.format("IOException: %s%n", e);
        }
    }
}