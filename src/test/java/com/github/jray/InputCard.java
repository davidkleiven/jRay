package com.github.jray;

import org.json.simple.JSONObject;

public abstract class InputCard
{
    abstract void fromJSON(JSONObject obj);
}