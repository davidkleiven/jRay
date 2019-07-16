package com.github.jray;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;

import org.junit.Test;

public class MonitorTest
{
    private static final double EPS = 1E-6;

    @Test
    public void saveLoad()
    {
        Monitor monitor = new Monitor();
        monitor.setSize(10);

        Ray ray = new Ray();

        // Insert some data
        for (int i=0;i<40;i++)
        {
            ray.opticalPathLength = 0.1*i;
            monitor.update(i%10, ray);
        }
        monitor.save("testMonitor.csv");

        ArrayList<Vector> real = monitor.getReal();
        ArrayList<Vector> imag = monitor.getImag();

        monitor.load("testMonitor.csv");

        ArrayList<Vector> real2 = monitor.getReal();
        ArrayList<Vector> imag2 = monitor.getImag();

        for (int i=0;i<real.size();i++){
            assertEquals(real.get(i).getX(), real2.get(i).getX(), EPS);
            assertEquals(real.get(i).getY(), real2.get(i).getY(), EPS);
            assertEquals(real.get(i).getZ(), real2.get(i).getZ(), EPS);
            assertEquals(imag.get(i).getX(), imag2.get(i).getX(), EPS);
            assertEquals(imag.get(i).getY(), imag2.get(i).getY(), EPS);
            assertEquals(imag.get(i).getZ(), imag2.get(i).getZ(), EPS);
        }
    }

    @Test
    public void testXYZFile()
    {
        Monitor monitor = new Monitor();
        TriangleMesh mesh = new TriangleMesh();
        mesh.addNode(new Vector(0.0, 0.0, 0.0));
        mesh.addNode(new Vector(-1.0, 0.0, 0.0));
        mesh.addNode(new Vector(0.0, 1.0, 0.0));
        int[] ids = {0, 1, 2};
        mesh.addElement(ids);
        monitor.setSize(1);
    
        String fname = "monitorXYZ.csv";
        monitor.save(fname, mesh);

        // Try to read the file
        try(BufferedReader br = Files.newBufferedReader(Paths.get(fname))){
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null){
                if (lineNo == 0){
                    assertEquals(line, "# x,y,z,RealX,RealY,RealZ,ImagX,ImagY,ImagZ");
                }
                else if (lineNo == 1){
                    String[] partitioned = line.split(",");
                    double x = Double.parseDouble(partitioned[0]);
                    double y = Double.parseDouble(partitioned[1]);
                    double z = Double.parseDouble(partitioned[2]);
                    double reX = Double.parseDouble(partitioned[3]);
                    double reY = Double.parseDouble(partitioned[4]);
                    double reZ = Double.parseDouble(partitioned[5]);
                    double imX = Double.parseDouble(partitioned[6]);
                    double imY = Double.parseDouble(partitioned[7]);
                    double imZ = Double.parseDouble(partitioned[8]);
                    assertEquals(-1.0/3.0, x, EPS);
                    assertEquals(1.0/3.0, y, EPS);
                    assertEquals(0.0, z, EPS);
                    assertEquals(0.0, reX, EPS);
                    assertEquals(0.0, reY, EPS);
                    assertEquals(0.0, reZ, EPS);
                    assertEquals(0.0, imX, EPS);
                    assertEquals(0.0, imY, EPS);
                    assertEquals(0.0, imZ, EPS);
                }
                else{
                    throw new RuntimeException("Too many lines!");
                }
                lineNo += 1;
            }
        } catch(IOException e){
            e.printStackTrace();
            fail("Error occured during reading!");
        } catch(RuntimeException e){
            e.printStackTrace();
            fail("Too many lines in the file.");
        }
    }
}