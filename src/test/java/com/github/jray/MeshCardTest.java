package com.github.jray;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import static org.junit.Assert.fail;

public class MeshCardTest
{
    @Test
    public void testRead()
    {
        MeshCard meshc = new MeshCard();
        
        // Open the JSON file
        try{
            FileReader reader = new FileReader("res/meshCardEx.json");
            JSONTokener parser = new JSONTokener(reader);
            JSONObject obj = new JSONObject(parser);
            meshc.fromJSON(obj);
            assertEquals("box.msh", meshc.getMeshFile());
        } catch (FileNotFoundException e){
            fail("Could not open JSON file");
        }
    }
}
