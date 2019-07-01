package com.github.jray;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;

public class Monitor
{
    private ArrayList<Vector> amplitudeReal = new ArrayList<Vector>();
    private ArrayList<Vector> amplitudeImag = new ArrayList<Vector>();

    public void setSize(int size)
    {
        for (int i=0;i<size;i++){
            amplitudeReal.add(new Vector(0.0, 0.0, 0.0));
            amplitudeImag.add(new Vector(0.0, 0.0, 0.0));
        }
    }

    public ArrayList<Vector> getReal()
    {
        return new ArrayList<Vector>(amplitudeReal);
    }

    public ArrayList<Vector> getImag()
    {
        return new ArrayList<Vector>(amplitudeImag);
    }

    public void update(int element, Ray ray)
    {
        double opl = ray.getWavenumber()*ray.opticalPathLength;
        Vector realPart = ray.getAmplitude().mult(Math.cos(opl));
        Vector imagPart = ray.getAmplitude().mult(Math.sin(opl));
        amplitudeReal.get(element).iadd(realPart);
        amplitudeImag.get(element).iadd(imagPart);
    }

    public void save(String fname)
    {
        try{
            FileWriter writer = new FileWriter(fname);
            writer.write("# RealX,RealY,RealZ,ImagX,ImagY,ImagZ\n");
            for (int i=0;i<amplitudeReal.size();i++)
            {
                Vector re = amplitudeReal.get(i);
                Vector im = amplitudeImag.get(i);
				writer.write(String.format("%f,%f,%f,%f,%f,%f\n", re.getX(), re.getY(),re.getZ(), im.getX(), im.getY(), im.getZ()));
            }
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void load(String fname)
    {
        amplitudeReal.clear();
        amplitudeImag.clear();

        try(BufferedReader br = Files.newBufferedReader(Paths.get(fname))){
            String line;

            while ((line = br.readLine()) != null){
                if (line.startsWith("#")){
                    continue;
                }

                String[] partitioned = line.split(",");
                double reX = Double.parseDouble(partitioned[0]);
                double reY = Double.parseDouble(partitioned[1]);
                double reZ = Double.parseDouble(partitioned[2]);
                double imX = Double.parseDouble(partitioned[3]);
                double imY = Double.parseDouble(partitioned[4]);
                double imZ = Double.parseDouble(partitioned[5]);

                amplitudeReal.add(new Vector(reX, reY, reZ));
                amplitudeImag.add(new Vector(imX, imY, imZ));
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }
}