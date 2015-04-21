package com.example.nathan.prco303app_foodr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 20/04/2015.
 */
public class placeClass {
    private String placeName;
    private String placeID;
    private String placeLocation;
    private String placeVicinity;
    private List<String> placeTypes;

    public placeClass()
    {
        placeName = "";
        placeID = "";
        placeLocation = "";
        placeVicinity = "";
        placeTypes = new ArrayList<String>();
    }

    public placeClass(String name, String id, String location, String vicinity, String[]typesArr)
    {
        placeName = name;
        placeID = id;
        placeLocation = location;
        placeVicinity = vicinity;
        placeTypes = new ArrayList<String>();
        for(int i =0 ; i < typesArr.length; i++)
        {
            placeTypes.add(typesArr[i]);
        }
    }

    public void setPlaceName(String name)
    {
        placeName = name;
    }

    public void setPlaceID(String id)
    {
        placeID = id;

    }

    public void setPlaceLocation(String location)
    {
        placeLocation = location;

    }

    public void setPlaceVicinity(String vicinity)
    {
        placeVicinity = vicinity;

    }

    public void setPlaceTypes(String[] typesArr)
    {
        for(int i =0; i < typesArr.length; i++)
        {
           placeTypes.add(typesArr[i]);
        }

    }

    public String getPlaceName()
    {
        return placeName;
    }

    public String getPlaceID()
    {
        return placeID;
    }

    public String getPlaceLocation()
    {
        return placeLocation;
    }

    public String getPlaceVicinity()
    {
        return placeVicinity;
    }

    public List<String> getPlaceTypes()
    {
        return placeTypes;
    }
}
