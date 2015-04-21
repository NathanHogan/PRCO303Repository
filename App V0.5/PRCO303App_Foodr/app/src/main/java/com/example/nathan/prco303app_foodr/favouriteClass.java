package com.example.nathan.prco303app_foodr;

/**
 * Created by Nathan on 21/04/2015.
 */
public class favouriteClass {
    private String userID;
    private placeClass placeInformation;

    public favouriteClass()
    {
        userID = "";
        placeInformation = new placeClass();
    }

    public favouriteClass(String id, placeClass place)
    {
        userID = id;
        placeInformation = place;
    }

    public void setUserID(String id)
    {
        userID = id;
    }

    public void setPlaceInformation(placeClass place)
    {
        placeInformation = place;
    }

    public String getUserID()
    {
        return userID;
    }

    public placeClass getPlaceInformation()
    {
        return placeInformation;
    }


}
