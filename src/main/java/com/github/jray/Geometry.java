package com.github.jray;
import java.util.TreeMap;

public class Geometry
{
    private TreeMap<Integer, PhysicalMedium> mediums = new TreeMap<Integer, PhysicalMedium>();
    private PhysicalMedium vacuum = new PhysicalMedium(1.0);

    public PhysicalMedium getMedium(int uid)
    {
        if (uid == 0){
            return vacuum;
        }
        return mediums.get(uid);
    }

    public void addMedium(PhysicalMedium medium)
    {
        mediums.put(medium.getID(), medium);
    }

    public TreeMap<Integer, PhysicalMedium> allMediums(){
        return mediums;
    }
    
}