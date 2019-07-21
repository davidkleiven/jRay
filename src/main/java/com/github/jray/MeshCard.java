package com.github.jray;

import org.json.JSONObject;

public class MeshCard extends InputCard
{
    private String fname;
    private TriangleMesh mesh;
    private double dx = 0.0;
    private double dy = 0.0;
    private double dz = 0.0;

    private void translateNodeCoordinates()
    {
        Vector vec = new Vector(dx, dy, dz);
        mesh.translate(vec);
    }

    @Override
    public void fromJSON(JSONObject obj)
    {
        this.fname = obj.getString("file");
        this.dx = obj.getDouble("dx");
        this.dy = obj.getDouble("dy");
        this.dz = obj.getDouble("dz");
        GMSHReader reader = new GMSHReader();
        this.mesh = reader.readGMSH(this.fname);
        translateNodeCoordinates();
    }

    public String getMeshFile(){
        return fname;
    }

    public Vector getTranslationVector()
    {
        return new Vector(dx, dy, dz);
    }
}