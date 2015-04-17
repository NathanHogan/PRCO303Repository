package com.example.nathan.prco303app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 17/04/2015.
 */
public class UserSettings {
    public int radius;
    public List<String> types;

    public UserSettings()
    {
        radius = 0;
        types = new ArrayList<String>();
    }

    public UserSettings(int newRad, List<String>newTypes)
    {
        radius = newRad;
        types = newTypes;
    }

    public void setRadius(int setRad)
    {
        radius = setRad;
    }

    public void setTypes(List<String> setTypes)
    {
        types = setTypes;
    }

    public int getRadius()
    {
        return radius;
    }

    public List<String> getTypes()
    {
        return types;
    }
}
