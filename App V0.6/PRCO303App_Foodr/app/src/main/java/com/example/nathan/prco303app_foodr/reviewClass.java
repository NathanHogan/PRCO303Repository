package com.example.nathan.prco303app_foodr;

/**
 * Created by Nathan on 22/04/2015.
 */
public class reviewClass {
    private String reviewText;
    private String reviewPID;
    private String reviewUID;
    private int reviewRate;

    public reviewClass()
    {
        reviewText = "";
        reviewPID = "";
        reviewUID = "";
        reviewRate = 0;
    }

    public reviewClass(String newText, String newPID, String newUID, int newRate)
    {
        reviewText = newText;
        reviewPID = newPID;
        reviewUID = newUID;
        reviewRate = newRate;
    }

    public void setReviewText(String newText)
    {
        reviewText = newText;
    }

    public void setReviewPID(String newPID)
    {
        reviewPID = newPID;
    }

    public void setReviewUID(String newUID)
    {
        reviewUID = newUID;
    }

    public void setReviewRate(int newRate)
    {
        reviewRate = newRate;
    }

    public String getReviewText()
    {
        return reviewText;
    }

    public String getReviewPID()
    {
        return reviewPID;
    }

    public String getReviewUID()
    {
        return reviewUID;
    }

    public int getReviewRate()
    {
        return reviewRate;
    }
}
