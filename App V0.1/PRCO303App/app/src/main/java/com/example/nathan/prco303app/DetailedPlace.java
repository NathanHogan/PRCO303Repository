package com.example.nathan.prco303app;

import java.util.List;

/**
 * Created by Nathan on 16/04/2015.
 */
public class DetailedPlace {

    public String formattedAddress;
    public String formattedPhone;
    public List<String> weekdayText;
    public Boolean openNow;

    public DetailedPlace(String newAddress, String newPhone, List<String>newWeek, Boolean newOpen)
    {
        formattedPhone = newPhone;
        formattedAddress = newAddress;
        weekdayText = newWeek;
        openNow = newOpen;
    }

    public void setFormattedAddress(String newAddress)
    {
        formattedAddress = newAddress;
    }

    public void setFormattedPhone(String newPhone)
    {
        formattedPhone = newPhone;
    }

    public void setWeekdayText(List<String> newWeekText)
    {
        weekdayText = newWeekText;
    }

    public void setOpenNow(Boolean newOpen)
    {
        openNow = newOpen;
    }

    public String getFormattedAddress()
    {
        return formattedAddress;
    }

    public String getFormattedPhone()
    {
        return formattedPhone;
    }

    public List<String> getWeekdayText()
    {
        return weekdayText;
    }

    public Boolean getOpenNow()
    {
        return openNow;
    }
}
