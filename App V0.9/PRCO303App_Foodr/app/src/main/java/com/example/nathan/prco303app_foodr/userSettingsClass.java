package com.example.nathan.prco303app_foodr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 23/04/2015.
 */
public class userSettingsClass {
    private int radius;
    private List<String> types;
    private String UID = "";

    public userSettingsClass()
    {
        radius = 0;
        types = new ArrayList<String>();
    }

    public userSettingsClass(int newRad, ArrayList<String> newType)
    {
        radius = newRad;
        types = new ArrayList<String>();
        types = newType;
    }

    public void setRadius(int newRad)
    {
        radius = newRad;
    }

    public void setTypes(ArrayList<String> newType)
    {
        types = new ArrayList<String>();
        types = newType;
    }

    public void setUID(String newUID)
    {
        UID = newUID;
    }

    public int getRadius()
    {
        return radius;
    }

    public List<String> getTypes()
    {
        return types;
    }

    public String getUID()
    {
        return UID;
    }
}
