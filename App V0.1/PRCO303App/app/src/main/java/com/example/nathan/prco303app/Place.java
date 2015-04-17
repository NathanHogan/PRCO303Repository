package com.example.nathan.prco303app;

/**
 * Created by Nathan on 16/04/2015.
 */
public class Place {
    public String id;
    public String name;
    public String location;

    public Place(String newID, String newName, String newLocation)
    {
        id = newID;
        name = newName;
        location = newLocation;
    }

    public void setId(String newID)
    {
        id = newID;
    }

    public void setName(String newName){
        name = newName;
    }

    public void setLocation(String newLocation) { location = newLocation; }

    public String getId()
    {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getLocation() {return location;}
}
