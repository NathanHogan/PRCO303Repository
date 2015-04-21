package com.example.nathan.prco303app_foodr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 21/04/2015.
 */
public class detailClass {
    private String address;
    private String phone;
    private List<String> weekTimes;
    private Boolean openNow;

    public detailClass()
    {
        address="";
        phone="";
        weekTimes = new ArrayList<String>();
        openNow = false;
    }

    public detailClass(String newAddress, String newNumber, List<String>newWeek, Boolean newOpen)
    {
        address = newAddress;
        phone = newNumber;
        weekTimes = newWeek;
        openNow = newOpen;
    }

    public void setAddress(String newAddress)
    {
        address = newAddress;
    }

    public void setPhone(String newPhone)
    {
        phone = newPhone;
    }

    public void setWeekTimes(List<String> newWeek)
    {
        weekTimes = newWeek;
    }

    public void setOpenNow(Boolean open)
    {
        openNow = open;
    }

    public String getAddress()
    {
        return address;
    }

    public String getPhone()
    {
        return phone;
    }

    public Boolean getOpenNow()
    {
        return openNow;
    }

    public List<String> getWeekTimes()
    {
        return weekTimes;
    }


}
