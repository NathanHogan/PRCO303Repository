package com.example.nathan.prco303app;

/**
 * Created by Nathan on 16/04/2015.
 */
public class Place {
    public String id;
    public String name;

    public Place(String newID, String newName)
    {
        id = newID;
        name = newName;
    }

    public void setId(String newID)
    {
        id = newID;
    }

    public void setName(String newName){
        name = newName;
    }

    public String getId()
    {
        return id;
    }

    public String getName(){
        return name;
    }
}
