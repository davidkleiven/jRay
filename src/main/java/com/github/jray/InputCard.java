package com.github.jray;

import org.json.JSONObject;

public abstract class InputCard
{
    abstract void fromJSON(JSONObject obj);
}